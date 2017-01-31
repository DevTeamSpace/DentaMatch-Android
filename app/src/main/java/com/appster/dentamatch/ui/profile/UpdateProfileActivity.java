package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityUpdateProfileBinding;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.model.ProfileUpdatedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.profile.UpdateUserProfileRequest;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponseData;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by virender on 21/01/17.
 */
public class UpdateProfileActivity extends BaseActivity implements View.OnClickListener, ImageSelectedListener, JobTitleSelectionListener {
    private static final int REQUEST_CODE_LOCATION = 102;
    private static final String TAG = "UpdateProfile";

    private byte imageSourceType;
    private ActivityUpdateProfileBinding mBinding;
    private ProfileResponseData mProfileData;
    private String mAddress;
    private String mFilePath;
    private String mSelectedLat;
    private String mSelectedLng;
    private int mSelectedJobTitleID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

        if (getIntent().hasExtra(Constants.EXTRA_PROFILE_DATA)) {
            mProfileData = getIntent().getParcelableExtra(Constants.EXTRA_PROFILE_DATA);
        }

        mBinding.etLocation.setFocusableInTouchMode(false);
        mBinding.etLocation.setCursorVisible(false);

        mBinding.etJobTitle.setFocusableInTouchMode(false);
        mBinding.etJobTitle.setCursorVisible(false);
        setViewData();

    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void setViewData() {
        if (mProfileData != null) {
            mSelectedLat = mProfileData.getUser().getLatitude();
            mSelectedLng = mProfileData.getUser().getLongitude();
            mSelectedJobTitleID = mProfileData.getUser().getJobTitleId();
            if(mSelectedJobTitleID == 0){
                mBinding.etJobTitle.setText("Please select a job");

            }else{
                mBinding.etJobTitle.setText(mProfileData.getUser().getJobTitle());

            }
            mBinding.toolbarProfile.tvToolbarGeneralLeft.setText(R.string.header_edit_profile);
            mBinding.etFname.setText(mProfileData.getUser().getFirstName());
            mBinding.etLname.setText(mProfileData.getUser().getLastName());
            mBinding.etDesc.setText(mProfileData.getUser().getAboutMe());
            mBinding.etLocation.setText(mProfileData.getUser().getPreferredJobLocation());

            if (!TextUtils.isEmpty(mProfileData.getUser().getProfilePic())) {
                Picasso.with(this).load(mProfileData.getUser().getProfilePic())
                        .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                        .placeholder(R.drawable.profile_pic_placeholder)
                        .into(mBinding.createProfile1IvProfileIcon);
            }

            mBinding.etLocation.setOnClickListener(this);
            mBinding.createProfile1IvProfileIcon.setOnClickListener(this);
            mBinding.btnSave.setOnClickListener(this);
            mBinding.etJobTitle.setOnClickListener(this);
            mBinding.toolbarProfile.ivToolBarLeft.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.et_job_title:
                new BottomSheetJobTitle(UpdateProfileActivity.this, this, PreferenceUtil.getJobTitlePosition());
                break;

            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            case R.id.btn_save:
                validateAndUpdate();
                break;

            case R.id.et_location:
                startActivityForResult(new Intent(UpdateProfileActivity.this, PlacesMapActivity.class), REQUEST_CODE_LOCATION);
                break;

            case R.id.create_profile1_iv_profile_icon:
                callBottomSheet();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mAddress = data.getStringExtra(Constants.EXTRA_PLACE_NAME);
                    mAddress.concat(" - ").concat(data.getStringExtra(Constants.EXTRA_POSTAL_CODE));
                    mSelectedLat = data.getStringExtra(Constants.EXTRA_LATITUDE);
                    mSelectedLng = data.getStringExtra(Constants.EXTRA_LONGITUDE);

                    if (!TextUtils.isEmpty(mAddress)) {
                        mBinding.inputLayoutLocation.setHintEnabled(true);
                        mBinding.inputLayoutLocation.setHintAnimationEnabled(true);
                        mBinding.etLocation.setText(mAddress);
                    }
                }
            }

        } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
                if (mFilePath != null) {

                    Picasso.with(UpdateProfileActivity.this)
                            .load(new File(mFilePath)).centerCrop()
                            .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(mBinding.createProfile1IvProfileIcon);

                    uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_PIC);

                }
            }

        } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                mFilePath = CameraUtil.getInstance().getGallaryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);

                if (mFilePath != null) {

                    Picasso.with(UpdateProfileActivity.this)
                            .load(new File(mFilePath)).centerCrop()
                            .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                            .placeholder(R.drawable.profile_pic_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(mBinding.createProfile1IvProfileIcon);

                    uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_PIC);
                }
            }
        }
    }

    private void validateAndUpdate() {
        if (TextUtils.isEmpty(mBinding.etFname.getText())) {
            showToast(getString(R.string.error_no_first_name));

        } else if (TextUtils.isEmpty(mBinding.etLname.getText())) {
            showToast(getString(R.string.error_no_last_name));

        } else if (TextUtils.isEmpty(mBinding.etJobTitle.getText())) {
            showToast(getString(R.string.error_no_job_type));

        }else if(mBinding.etJobTitle.getText().toString().equalsIgnoreCase("Please select a job")){
            showToast(getString(R.string.error_no_job_type));

        }else if (TextUtils.isEmpty(mBinding.etLocation.getText())) {
            showToast(getString(R.string.error_no_location));

        } else if (TextUtils.isEmpty(mBinding.etDesc.getText())) {
            showToast(getString(R.string.error_no_description));

        } else {
            updateProfileData();
        }
    }

    private void updateProfileData() {

        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setFirstName(Utils.getStringFromEditText(mBinding.etFname));
        request.setLastName(Utils.getStringFromEditText(mBinding.etLname));
        request.setPreferredJobLocation(Utils.getStringFromEditText(mBinding.etLocation));
        request.setAboutMe(Utils.getStringFromEditText(mBinding.etDesc));
        request.setLatitude(mSelectedLat);
        request.setLongitude(mSelectedLng);
        request.setJobTitleID(mSelectedJobTitleID);
        showProgressBar(getString(R.string.please_wait));

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.updateUserProfile(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                hideProgressBar();
                if (!TextUtils.isEmpty(response.getMessage())) {
                    showToast(response.getMessage());
                }

                if (response.getStatus() == 1) {
                    EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                hideProgressBar();
                LogUtils.LOGE(TAG, " UPDATE PROFILE API failed!");
            }
        });
    }

    private void uploadImageApi(String filePath, String imageType) {
        showProgressBar(getString(R.string.please_wait));
        File file = new File(filePath);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody uploadType = RequestBody.create(MediaType.parse("multipart/form-data"), imageType);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        Call<FileUploadResponse> response = webServices.uploadImage(uploadType, fbody);
        response.enqueue(new BaseCallback<FileUploadResponse>(UpdateProfileActivity.this) {
            @Override
            public void onSuccess(FileUploadResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response != null && response.getStatus() == 1) {
                    // showSnackBarFromTop(response.getMessage(), false);
                    PreferenceUtil.setProfileImagePath(response.getFileUploadResponseData().getImageUrl());
                    showToast(response.getMessage());

                }
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGE(TAG, " ImageUpload failed!");

            }
        });
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    @Override
    public void cameraClicked() {
        imageSourceType = 0;
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.CAMERA, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this)) {
            takePhoto();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(mBinding.etFname, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(UpdateProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(UpdateProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void galleryClicked() {
        imageSourceType = 1;

        if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) && PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
            getImageFromGallery();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(mBinding.etFname, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Accept", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(UpdateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            LogUtils.LOGD(TAG, "request permisison called if granted --");
            if (imageSourceType == 0) {
                takePhoto();
            } else {
                getImageFromGallery();
            }

        }
    }

    private void getImageFromGallery() {
        Intent gIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gIntent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(gIntent, "Select File"),
                Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int position) {
        mBinding.etJobTitle.setText(title);
        mSelectedJobTitleID = titleId;
    }
}

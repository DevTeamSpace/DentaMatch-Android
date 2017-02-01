package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityCreateProfile1Binding;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.response.profile.JobTitleResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.searchjob.SearchJobActivity;
import com.appster.dentamatch.util.Alert;
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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by virender on 03/01/17.
 */
public class CreateProfileActivity1 extends BaseActivity implements View.OnClickListener, ImageSelectedListener, JobTitleSelectionListener {
    private String TAG = "CreateProfileActivity1";
    private ImageSelectedListener imageSelectedListener;
    private String mFilePath;
    private ActivityCreateProfile1Binding mBinder;
    private String selectedJobtitle = "";
    private byte imageSourceType;
    private boolean mImageUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_create_profile1);

        initViews();
        callJobListApi();
    }

    private void initViews() {
        mBinder.createProfile1BtnNotNow.setOnClickListener(this);
        mBinder.createProfile1BtnNext.setOnClickListener(this);
        mBinder.createProfile1IvProfileIcon.setOnClickListener(this);
        if(!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())){

            Picasso.with(CreateProfileActivity1.this).load(PreferenceUtil.getProfileImagePath())
                    .centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinder.createProfile1IvProfileIcon);
        }
        mBinder.etJobTitle.setOnClickListener(this);
        mBinder.createProfileTvName.setText(getString(R.string.hi_user, PreferenceUtil.getFirstName()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_profile1_iv_profile_icon:
                callBottomSheet();
                break;

            case R.id.et_job_title:
                hideKeyboard();
                if(PreferenceUtil.getJobTitleList()!=null && PreferenceUtil.getJobTitleList().size()>0)
                new BottomSheetJobTitle(CreateProfileActivity1.this, this, 0);
                break;

            case R.id.create_profile1_btn_next:
                if (mImageUploaded) {
                    launchNextActivity();
                } else {
                    if (checkValidation()) {
//                    Intent intent = new Intent(this, CreateProfileActivity2.class);
//                    startActivity(intent);
                        uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_PIC);
                    }
                }

                break;
            case R.id.create_profile1_btn_not_now:

                Alert.createYesNoAlert(CreateProfileActivity1.this, getString(R.string.ok), getString(R.string.cancel), "", getString(R.string.alert_profile), new Alert.OnAlertClickListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        startActivity(new Intent(CreateProfileActivity1.this, SearchJobActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(mFilePath)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_photo_alert));
            return false;
        }
        if (TextUtils.isEmpty(selectedJobtitle)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
            return false;
        }
        return true;
    }

    private void uploadImageApi(String filePath, String imageType) {
        showProgressBar(getString(R.string.please_wait));
        File file = new File(filePath);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody uploadType = RequestBody.create(MediaType.parse("multipart/form-data"), imageType);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        Call<FileUploadResponse> response = webServices.uploadImage(uploadType, fbody);
        response.enqueue(new BaseCallback<FileUploadResponse>(CreateProfileActivity1.this) {
            @Override
            public void onSuccess(FileUploadResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response != null && response.getStatus() == 1) {
                    mImageUploaded = true;
                    PreferenceUtil.setProfileImagePath(response.getFileUploadResponseData().getImageUrl());
                    launchNextActivity();
                }
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGE(TAG, " ImageUpload failed!");
            }
        });
    }

    private void callJobListApi() {
        LogUtils.LOGD(TAG, "job title list");
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.jobTitle().enqueue(new BaseCallback<JobTitleResponse>(CreateProfileActivity1.this) {
            @Override
            public void onSuccess(JobTitleResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    LogUtils.LOGD(TAG, "Size is--=" + response.getJobTitleResponseData().getJobTitleList().size());

                    PreferenceUtil.setJobTitleList(response.getJobTitleResponseData().getJobTitleList());
//                    String title[] = new String[response.getJobTitleResponseData().getJobTitleList().size()];
//                    for (int i = 0; i < response.getJobTitleResponseData().getJobTitleList().size(); i++) {
//                        title[i] = response.getJobTitleResponseData().getJobTitleList().get(i).getJobTitle();
//                    }
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<JobTitleResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
            }
        });
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
                Snackbar.make(mBinder.createProfileTvName, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity1.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(CreateProfileActivity1.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
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
                Snackbar.make(mBinder.createProfileTvName, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Accept", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity1.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }
    }

    private void launchNextActivity() {
        Intent intent = new Intent(CreateProfileActivity1.this, CreateProfileActivity2.class);
        startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);

            } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = data.getData();
                mFilePath = CameraUtil.getInstance().getGallaryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            }
            LogUtils.LOGD(TAG, "file path" + mFilePath);

            if (mFilePath != null) {
                Picasso.with(CreateProfileActivity1.this).load(new File(mFilePath)).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.createProfile1IvProfileIcon);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.LOGD(TAG, "request permisison called --" + grantResults.length);

        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            LogUtils.LOGD(TAG, "request permisison called if granted --");

            if (imageSourceType == 0) {
                takePhoto();
            } else {
                getImageFromGallery();
            }
        }
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int position) {
        PreferenceUtil.setJobTitle(title);
        PreferenceUtil.setJobTitleId(titleId);
        PreferenceUtil.setJobTitlePosition(position);
        mBinder.etJobTitle.setText(title);
        selectedJobtitle = title;
    }
}



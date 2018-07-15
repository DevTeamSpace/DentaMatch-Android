package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.response.profile.JobTitleResponse;
import com.appster.dentamatch.network.response.profile.LicenceUpdateResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.auth.UserVerifyPendingActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
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
 * To inject activity reference.
 */
public class CreateProfileActivity1 extends BaseActivity implements View.OnClickListener, ImageSelectedListener, JobTitleSelectionListener {
    private static final String TAG = LogUtils.makeLogTag(CreateProfileActivity1.class);
    private String mFilePath;
    private ActivityCreateProfile1Binding mBinder;
    private String selectedJobTitle = "";
    private byte imageSourceType;
    private boolean mImageUploaded;
    private int isLicenceRequired = 0;
    private int jobTitleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_create_profile1);
        initViews();
    }

    private void initViews() {
        //mBinder.createProfile1BtnNotNow.setOnClickListener(this);
        mBinder.createProfile1BtnNext.setOnClickListener(this);
        mBinder.createProfile1IvProfileIcon.setOnClickListener(this);

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            mFilePath = PreferenceUtil.getProfileImagePath();
            Picasso.with(CreateProfileActivity1.this).load(PreferenceUtil.getProfileImagePath())
                    .centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinder.createProfile1IvProfileIcon);
            mImageUploaded = true;
        }

        mBinder.etJobTitle.setOnClickListener(this);
        mBinder.createProfileTvName.setText(getString(R.string.hi_user, PreferenceUtil.getFirstName()+" "+PreferenceUtil.getLastName()));
        mBinder.tvPreferredJobLocationVal.setText(PreferenceUtil.getPreferredJobLocationName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_profile1_iv_profile_icon:
                callBottomSheet();
                break;

            case R.id.et_job_title:
                hideKeyboard();

                if (PreferenceUtil.getJobTitleList() != null && PreferenceUtil.getJobTitleList().size() > 0) {
                    new BottomSheetJobTitle(CreateProfileActivity1.this, this, 0);
                } else {
                    callJobListApi();
                }

                break;

            case R.id.create_profile1_btn_next:

               /* if (mImageUploaded) {
                    if (validateInputData())
                        launchNextActivity();

                    *//*if (TextUtils.isEmpty(selectedJobTitle)) {
                        Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
                    } else if (TextUtils.isEmpty(mBinder.etDescAboutMe.getText().toString().trim())) {
                        Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_summary_alert));
                    } else {
                        launchNextActivity();
                    }*//*

                } else {
                    if (checkValidation()) {
                        uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_PIC);
                    }
                }*/

                if (validateInputData())
                    callUploadLicenseApi();

                break;

            /*case R.id.create_profile1_btn_not_now:
                Alert.createYesNoAlert(CreateProfileActivity1.this,
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        "",
                        getString(R.string.alert_profile),
                        new Alert.OnAlertClickListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        if(PreferenceUtil.isJobFilterSet()){
                            startActivity(new Intent(CreateProfileActivity1.this, HomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else {
                            startActivity(new Intent(CreateProfileActivity1.this, SearchJobActivity.class)
                                    .putExtra(Constants.EXTRA_IS_FIRST_TIME, true)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                break;*/
            default:
                break;
        }
    }

    private void callUploadLicenseApi() {
        processToShowDialog();
        try {
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.updateLicence(getLicenseRequestWrapper()).enqueue(new BaseCallback<LicenceUpdateResponse>(CreateProfileActivity1.this) {
                @Override
                public void onSuccess(LicenceUpdateResponse response) {

                    if (response.getStatus() == 1) {

                        PreferenceUtil.setUserVerified(response.getResult().getUserDetails().getIsVerified());
                        PreferenceUtil.setUserModel(response.getResult().getUserDetails());
                        PreferenceUtil.setUserToken(response.getResult().getUserDetails().getAccessToken());
                        PreferenceUtil.setJobTitleId(jobTitleId);
                        if(response.getResult().getUserDetails().getIsVerified()==Constants.USER_VERIFIED_STATUS) {
                            Intent profileCompletedIntent = new Intent(CreateProfileActivity1.this, ProfileCompletedPendingActivity.class);
                            profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, isLicenceRequired);
                            startActivity(profileCompletedIntent);

                            finish();
                        }else{
                            Intent profileCompletedIntent = new Intent(CreateProfileActivity1.this, UserVerifyPendingActivity.class);
                            profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, isLicenceRequired);
                            startActivity(profileCompletedIntent);
                            finish();

                        }
                    } else {
                        Utils.showToast(getApplicationContext(), response.getMessage());

                    }
                }

                @Override
                public void onFail(Call<LicenceUpdateResponse> call, BaseResponse baseResponse) {
                    PreferenceUtil.setJobTitleId(0);

                }
            });

        } catch (Exception e) {
            hideProgressBar();
            LogUtils.LOGE(TAG,e.getMessage());
        }
    }

    private LicenceRequest getLicenseRequestWrapper() {
        LicenceRequest licenceRequest = new LicenceRequest();
        licenceRequest.setJobTitleId(jobTitleId);
        licenceRequest.setAboutMe(mBinder.etDescAboutMe.getText().toString());

        if (isLicenceRequired == 1) {
            licenceRequest.setLicense(mBinder.createProfileEtLicence.getText().toString());
            licenceRequest.setState(mBinder.createProfileEtState.getText().toString());
        }
        return licenceRequest;
    }

    private boolean validateInputData() {

        if (TextUtils.isEmpty(selectedJobTitle)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
            return false;
        }

        if (TextUtils.isEmpty(mBinder.etDescAboutMe.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_summary_alert));
            return false;
        }

        if (isLicenceRequired == 1) {
            if (TextUtils.isEmpty(mBinder.createProfileEtLicence.getText().toString().trim())) {
                Utils.showToast(CreateProfileActivity1.this, getString(R.string.blank_licence_number));
                return false;
            }

            if (mBinder.createProfileEtLicence.getText().toString().trim().length() > Constants.LICENCE_MAX_LENGTH) {
                Utils.showToast(CreateProfileActivity1.this, getString(R.string.licence_number_length));
                return false;
            }

            if (mBinder.createProfileEtLicence.getText().toString().trim().contains(" ")) {
                Utils.showToast(CreateProfileActivity1.this, getString(R.string.licence_number_blnk_space_alert));
                return false;
            }

            if (mBinder.createProfileEtLicence.getText().toString().trim().charAt(0) == '-' || mBinder.createProfileEtLicence.getText().toString().trim().charAt(mBinder.createProfileEtLicence.getText().toString().trim().length() - 1) == '-') {
                Utils.showToast(CreateProfileActivity1.this, getString(R.string.licence_number_hyfen_alert));
                return false;
            }

            if (TextUtils.isEmpty(mBinder.createProfileEtState.getText().toString().trim())) {
                Utils.showToast(CreateProfileActivity1.this, getString(R.string.blank_state_alert));
                return false;
            }

            if (mBinder.createProfileEtState.getText().toString().trim().length() >= Constants.DEFAULT_FIELD_LENGTH) {
                Utils.showToast(CreateProfileActivity1.this, getString(R.string.state_max_length));
                return false;
            }
        }

        return true;
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    private boolean checkValidation() {

        if (TextUtils.isEmpty(mFilePath)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_photo_alert));
            return false;
        }

        if (TextUtils.isEmpty(selectedJobTitle)) {
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

                if (response.getStatus() == 1) {
                    mImageUploaded = true;
                    PreferenceUtil.setProfileImagePath(response.getFileUploadResponseData().getImageUrl());
                    // launchNextActivity();
                }
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
            }
        });
    }

    private void callJobListApi() {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.jobTitle().enqueue(new BaseCallback<JobTitleResponse>(CreateProfileActivity1.this) {
            @Override
            public void onSuccess(JobTitleResponse response) {
                if (response.getStatus() == 1) {
                    PreferenceUtil.setJobTitleList(response.getJobTitleResponseData().getJobTitleList());
                    new BottomSheetJobTitle(CreateProfileActivity1.this, CreateProfileActivity1.this, 0);
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<JobTitleResponse> call, BaseResponse baseResponse) {
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
                        .setAction(getString(R.string.txt_ok), new View.OnClickListener() {
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
                        .setAction(getString(R.string.txt_accept), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity1.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }
    }

    private void launchNextActivity() {
        Intent intent = new Intent(CreateProfileActivity1.this, CreateProfileActivity2.class);
        startActivity(intent);
    }

//    private void getImageFromGallery() {
//        Intent gIntent = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        gIntent.setType("image/*");
//        startActivityForResult(
//                Intent.createChooser(gIntent, "Select File"),
//                Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
//    }

//    private void takePhoto() {
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(cameraIntent, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);

            } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = data.getData();
                mFilePath = CameraUtil.getInstance().getGalleryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            }

            if (mFilePath != null) {
                uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_PIC);
                mImageUploaded = false;
                Picasso.with(CreateProfileActivity1.this).load(new File(mFilePath)).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.createProfile1IvProfileIcon);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

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
    public void onJobTitleSelection(String title, int titleId, int position, int isLicenseRequired) {
        PreferenceUtil.setJobTitle(title);
       // PreferenceUtil.setJobTitleId(titleId);
        jobTitleId=titleId;
        PreferenceUtil.setJobTitlePosition(position);
        this.isLicenceRequired = isLicenseRequired;
        mBinder.etJobTitle.setText(title);
        selectedJobTitle = title;
        if (isLicenseRequired == 0) {
            mBinder.createProfileInputLayoutLicence.setVisibility(View.GONE);
            mBinder.createProfileInputLayoutState.setVisibility(View.GONE);
            mBinder.createProfileEtLicence.setText("");
            mBinder.createProfileEtState.setText("");
        } else {
            mBinder.createProfileInputLayoutLicence.setVisibility(View.VISIBLE);
            mBinder.createProfileInputLayoutState.setVisibility(View.VISIBLE);
            mBinder.createProfileEtLicence.setText("");
            mBinder.createProfileEtState.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferenceUtil.setJobTitleId(0);
    }
}



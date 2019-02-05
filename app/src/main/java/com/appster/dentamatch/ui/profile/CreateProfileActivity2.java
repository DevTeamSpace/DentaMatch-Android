/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.response.profile.LicenceUpdateResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.ui.profile.workexperience.WorkExperienceActivity;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by virender on 02/01/17.
 * To inject activity reference.
 */
public class CreateProfileActivity2 extends BaseActivity implements View.OnClickListener, ImageSelectedListener, YearSelectionListener {
    private static final String TAG = LogUtils.makeLogTag(CreateProfileActivity2.class);
    private ImageView ivProfile, ivUpload, ivToolbarLeft;
    private TextView tvName, tvJobTitle;
    private ProgressBar mProgressBar;
    private TextView tvToolbarLeft;
    private EditText etLicenceNumber, etLicenceNumberExpiry, etState;
    private Button btnNext;
    private String mFilePath;
    private byte imageSourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile2);

        initViews();
    }

    private void initViews() {
        ivProfile = findViewById(R.id.create_profile_iv_profile_icon);
        ivToolbarLeft = findViewById(R.id.iv_tool_bar_left);
        btnNext = findViewById(R.id.create_profile2_btn_next);
        tvToolbarLeft = findViewById(R.id.tv_toolbar_general_left);
        tvName = findViewById(R.id.create_profile_tv_name);
        etLicenceNumber = findViewById(R.id.create_profile_et_licence);
        etState = findViewById(R.id.create_profile_et_state);
        tvJobTitle = findViewById(R.id.create_profile_tv_job_title);
        mProgressBar = findViewById(R.id.create_profile_progress_bar);
        etLicenceNumberExpiry = findViewById(R.id.create_profile_et_licence_expiry);

        btnNext.setOnClickListener(this);
        ivToolbarLeft.setOnClickListener(this);

        mProgressBar.setProgress(Constants.PROFILE_PERCENTAGE.PROFILE_2);
        tvToolbarLeft.setText(getString(R.string.header_create_profile));

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.get()
                    .load(PreferenceUtil.getProfileImagePath())
                    .centerCrop()
                    .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(ivProfile);
        }

        if (!TextUtils.isEmpty(PreferenceUtil.getJobTitle())) {
            tvJobTitle.setText(PreferenceUtil.getJobTitle());
        }

        if (!TextUtils.isEmpty(PreferenceUtil.getFirstName())) {
            tvName.setText(PreferenceUtil.getFirstName());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;

            case R.id.create_profile_iv_profile_icon:
//                mSelectedImage = 0;
//                callBottomSheet();
                break;

            case R.id.create_profile2_btn_next:
                hideKeyboard();

                if (checkInputValidator()) {
                    callLicenceApi(prepareLicenceRequest());
                }
                break;

            case R.id.create_profile_et_licence_expiry:
                hideKeyboard();
                new BottomSheetPicker(this, this, 0, 0);
                break;


            default:
                break;
        }
    }

    private boolean checkInputValidator() {

        if (TextUtils.isEmpty(etLicenceNumber.getText().toString().trim())) {
            Utils.showToast(CreateProfileActivity2.this, getString(R.string.blank_licence_number));
            return false;
        }

        if (etLicenceNumber.getText().toString().trim().length() > Constants.LICENCE_MAX_LENGTH) {
            Utils.showToast(CreateProfileActivity2.this, getString(R.string.licence_number_length));
            return false;
        }

        if (etLicenceNumber.getText().toString().trim().contains(" ")) {
            Utils.showToast(CreateProfileActivity2.this, getString(R.string.licence_number_blnk_space_alert));
            return false;
        }

        if (etLicenceNumber.getText().toString().trim().charAt(0) == '-' || etLicenceNumber.getText().toString().trim().charAt(etLicenceNumber.getText().toString().trim().length() - 1) == '-') {
            Utils.showToast(CreateProfileActivity2.this, getString(R.string.licence_number_hyfen_alert));
            return false;
        }

        if (TextUtils.isEmpty(etState.getText().toString().trim())) {
            Utils.showToast(CreateProfileActivity2.this, getString(R.string.blank_state_alert));
            return false;
        }

        if (etState.getText().toString().trim().length() >= Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(CreateProfileActivity2.this, getString(R.string.state_max_length));
            return false;
        }

        return true;
    }

    private LicenceRequest prepareLicenceRequest() {
        processToShowDialog();
        LicenceRequest licenceRequest = new LicenceRequest();
        licenceRequest.setJobTitleId(PreferenceUtil.getJobTitleId());
        licenceRequest.setLicense(etLicenceNumber.getText().toString());
        licenceRequest.setState(etState.getText().toString());
        return licenceRequest;
    }

    private void callLicenceApi(LicenceRequest licenceRequest) {
        try {
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.updateLicence(licenceRequest).enqueue(new BaseCallback<LicenceUpdateResponse>(CreateProfileActivity2.this) {
                @Override
                public void onSuccess(LicenceUpdateResponse response) {

                    if (response.getStatus() == 1) {
                        startActivity(new Intent(CreateProfileActivity2.this, WorkExperienceActivity.class));

                    } else {
                        Utils.showToast(getApplicationContext(), response.getMessage());

                    }
                }

                @Override
                public void onFail(Call<LicenceUpdateResponse> call, BaseResponse baseResponse) {

                }
            });

        } catch (Exception e) {
            hideProgressBar();
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    private void uploadImageApi(String filePath, String imageType) {
        showProgressBar(getString(R.string.please_wait));
        File file = new File(filePath);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody uploadType = RequestBody.create(MediaType.parse("multipart/form-data"), imageType);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        Call<FileUploadResponse> response = webServices.uploadImage(uploadType, fbody);
        response.enqueue(new BaseCallback<FileUploadResponse>(CreateProfileActivity2.this) {
            @Override
            public void onSuccess(FileUploadResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
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
                Snackbar.make(ivProfile, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(CreateProfileActivity2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
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
                Snackbar.make(ivProfile, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_accept), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity2.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }

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
//
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
                Picasso.get().load(new File(mFilePath)).centerCrop().resize(Constants.IMAGE_DIME_CERTIFICATE, Constants.IMAGE_DIME_CERTIFICATE).placeholder(R.drawable.ic_upload).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivUpload);
                uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_STATE_BOARD);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
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
    public void onExperienceSection(int year, int month) {

        if (month == 1) {
            etLicenceNumberExpiry.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
        } else {
            etLicenceNumberExpiry.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.txt_months));
        }

    }
}

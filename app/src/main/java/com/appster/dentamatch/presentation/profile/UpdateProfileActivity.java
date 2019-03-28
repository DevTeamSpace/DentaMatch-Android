/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityUpdateProfileBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponseData;
import com.appster.dentamatch.presentation.auth.PreferredJobLocationAdapter;
import com.appster.dentamatch.presentation.common.SearchStateActivity;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.StringUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by virender on 21/01/17.
 * To inject activity reference.
 */
public class UpdateProfileActivity extends BaseLoadingActivity<UpdateProfileViewModel>
        implements View.OnClickListener, ImageSelectedListener, JobTitleSelectionListener {

    private static final int REQUEST_CODE_LOCATION = 102;

    private byte imageSourceType;
    private ActivityUpdateProfileBinding mBinding;
    private ProfileResponseData mProfileData;
    private String mSelectedCity;
    private String mSelectedState;
    private String mSelectedCountry;

    private int mSelectedJobTitleID;

    private ArrayAdapter<PreferredJobLocationData> mPreferredJobLocationDataArrayAdapter;
    private int preferredJobLocationId;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

        if (getIntent().hasExtra(Constants.EXTRA_PROFILE_DATA)) {
            mProfileData = getIntent().getParcelableExtra(Constants.EXTRA_PROFILE_DATA);
        }

        mBinding.etJobTitle.setFocusableInTouchMode(false);
        mBinding.etJobTitle.setCursorVisible(false);

        mBinding.createProfileEtState.setFocusableInTouchMode(false);
        mBinding.createProfileEtState.setCursorVisible(false);

        setViewData();

        mBinding.etDesc.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        mBinding.etPreferredJobLocation.setFocusableInTouchMode(false);
        mBinding.etPreferredJobLocation.setCursorVisible(false);
        if (mProfileData != null && mProfileData.getUser() != null && mProfileData.getUser().getPreferredLocationName() != null) {
            mBinding.etPreferredJobLocation.setText(mProfileData.getUser().getPreferredLocationName());
        }
        mBinding.etPreferredJobLocation.setOnClickListener(this);

        viewModel.getProfileResponse().observe(this, this::onSuccessProfileRequest);
        viewModel.getPreferredJobLocation().observe(this, this::onSuccessPreferredJobLocationRequest);
        viewModel.getUpdateUserProfile().observe(this, this::onSuccessUpdateUserProfile);
        viewModel.getUploadImage().observe(this, this::onSuccessImageUpload);
    }

    private void onSuccessImageUpload(@Nullable FileUploadResponse response) {
        if (response != null) {
            showSnackBar(response.getMessage());
            Picasso.get()
                    .load(response.getFileUploadResponseData().getImageUrl())
                    .centerCrop()
                    .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinding.createProfile1IvProfileIcon);
        }
    }

    private void onSuccessUpdateUserProfile(@Nullable BaseResponse response) {
        if (response != null) {
            if (!TextUtils.isEmpty(response.getMessage())) {
                showToast(response.getMessage());
            }

            if (response.getStatus() == 1) {
                EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                finish();
            }
        }
    }

    private void onSuccessProfileRequest(@Nullable ProfileResponse response) {
        if (response != null) {
            boolean isFound = false;
            for (int i = 0; i < response.getProfileResponseData().getJobTitleLists().size(); i++) {
                if (mSelectedJobTitleID == response.getProfileResponseData().getJobTitleLists().get(i).getId()) {
                    JobTitleListModel jtm = response.getProfileResponseData().getJobTitleLists().get(i);
                    mBinding.etJobTitle.setText(StringUtils.isNullOrEmpty(jtm.getShortName()) ? jtm.getJobTitle() : jtm.getShortName());
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                mSelectedJobTitleID = 0;
            }
        }
    }

    private void onSuccessPreferredJobLocationRequest(@Nullable PreferredJobLocationModel response) {
        if (response != null) {
            hideKeyboard();
            mPreferredJobLocationDataArrayAdapter =
                    new PreferredJobLocationAdapter(UpdateProfileActivity.this,
                            response.getResult().getPreferredJobLocations());
            showLocationList();
        }
    }

    private void getProfileData() {
        viewModel.requestProfile();
    }

    private void setViewData() {
        if (mProfileData != null) {
            mSelectedJobTitleID = mProfileData.getUser().getJobTitleId();
            mSelectedCity = mProfileData.getUser().getPreferredCity();
            mSelectedState = mProfileData.getUser().getPreferredState();
            mSelectedCountry = mProfileData.getUser().getPreferredCountry();
            preferredJobLocationId = mProfileData.getUser().getPreferredJobLocationId();
            if (mSelectedJobTitleID == 0) {
                mBinding.etJobTitle.setText(getString(R.string.msg_empty_job_title));
            } else {
                String shortName = "";
                for (JobTitleListModel jtm : mProfileData.getJobTitleLists()) {
                    if (StringUtils.isNullOrEmpty(jtm.getShortName()) || jtm.getId() != mProfileData.getUser().getJobTitleId()) {
                        continue;
                    }
                    shortName = jtm.getShortName();
                }
                mBinding.etJobTitle.setText(StringUtils.isNullOrEmpty(shortName) ? mProfileData.getUser().getJobtitleName() : shortName);
            }
            if ((mProfileData.getUser().getLicenseNumber() != null && !TextUtils.isEmpty(mProfileData.getUser().getLicenseNumber()))) {
                mBinding.createProfileInputLayoutLicence.setVisibility(View.VISIBLE);
                mBinding.createProfileInputLayoutState.setVisibility(View.VISIBLE);
                mBinding.createProfileEtLicence.setText(mProfileData.getUser().getLicenseNumber());
                mBinding.createProfileEtState.setText(mProfileData.getUser().getState());
            }
            mBinding.toolbarProfile.tvToolbarGeneralLeft.setText(R.string.header_edit_profile);
            mBinding.etFname.setText(mProfileData.getUser().getFirstName());
            mBinding.etLname.setText(mProfileData.getUser().getLastName());
            mBinding.etDesc.setText(mProfileData.getUser().getAboutMe());
            if (!TextUtils.isEmpty(mProfileData.getUser().getProfilePic())) {
                Picasso.get()
                        .load(mProfileData.getUser().getProfilePic())
                        .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                        .placeholder(R.drawable.profile_pic_placeholder)
                        .into(mBinding.createProfile1IvProfileIcon);
            }
            mBinding.createProfile1IvProfileIcon.setOnClickListener(this);
            mBinding.btnSave.setOnClickListener(this);
            mBinding.etJobTitle.setOnClickListener(this);
            mBinding.createProfileEtState.setOnClickListener(this);
            mBinding.toolbarProfile.ivToolBarLeft.setOnClickListener(this);
        }

        getProfileData();
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
            case R.id.ivToolBarLeft:
                onBackPressed();
                break;
            case R.id.btn_save:
                hideKeyboard();
                validateAndUpdate();
                break;
            case R.id.create_profile1_iv_profile_icon:
                callBottomSheet();
                break;
            case R.id.et_preferred_job_location:
                hideKeyboard(mBinding.etPreferredJobLocation);
                if (mPreferredJobLocationDataArrayAdapter == null) {
                    viewModel.requestPreferredJobLocation();
                } else {
                    showLocationList();
                }
                break;
            case R.id.create_profile_et_state:
                startActivity(new Intent(this, SearchStateActivity.class));
                break;
            default:
                break;
        }
    }

    private void showLocationList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(UpdateProfileActivity.this);
        builderSingle.setTitle(getResources().getString(R.string.preferred_location_label));
        builderSingle.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(mPreferredJobLocationDataArrayAdapter, (dialog, which) -> {
            PreferredJobLocationData locationData = mPreferredJobLocationDataArrayAdapter.getItem(which);
            if (locationData != null) {
                mBinding.etPreferredJobLocation.setText(locationData.getPreferredLocationName());
                preferredJobLocationId = locationData.getId();
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String mFilePath;
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mSelectedCity = data.getStringExtra(Constants.EXTRA_CITY_NAME);
                    mSelectedState = data.getStringExtra(Constants.EXTRA_STATE_NAME);
                    mSelectedCountry = data.getStringExtra(Constants.EXTRA_COUNTRY_NAME);
                    if (TextUtils.isEmpty(mSelectedCity)) {
                        mSelectedCity = "";
                    }
                    if (TextUtils.isEmpty(mSelectedState)) {
                        mSelectedState = "";
                    }
                    if (TextUtils.isEmpty(mSelectedCountry)) {
                        mSelectedCountry = "";
                    }
                }
            }

        } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
                if (mFilePath != null) {
                    uploadImageApi(mFilePath);
                }
            }

        } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                mFilePath = CameraUtil.getInstance().getGalleryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
                if (mFilePath != null) {
                    uploadImageApi(mFilePath);
                }
            }
        }
    }

    private void validateAndUpdate() {
        if (TextUtils.isEmpty(mBinding.etFname.getText().toString().trim())) {
            showToast(getString(R.string.error_no_first_name));

        } else if (TextUtils.isEmpty(mBinding.etLname.getText().toString().trim())) {
            showToast(getString(R.string.error_no_last_name));

        } else if (mSelectedJobTitleID == 0) {
            showToast(getString(R.string.error_no_job_type));

        } else if (mBinding.etJobTitle.getText().toString().equalsIgnoreCase(getString(R.string.txt_job_title_hint))) {
            showToast(getString(R.string.error_no_job_type));

        } else if (TextUtils.isEmpty(mBinding.etDesc.getText().toString().trim())) {
            showToast(getString(R.string.error_no_description));

        } else if (mBinding.createProfileEtLicence.isShown() && mBinding.createProfileEtLicence.getText().toString().trim().length() == 0) {
            showToast(getString(R.string.pls_enter_license_no));

        } else if (mBinding.createProfileEtLicence.isShown() && mBinding.createProfileEtState.getText().toString().trim().length() == 0) {
            showToast(getString(R.string.pls_enter_license_state));
        } else {
            updateProfileData();
        }
    }

    private void updateProfileData() {
        boolean license = mBinding.createProfileEtLicence.getVisibility() == View.VISIBLE;
        viewModel.updateUserProfile(Utils.getStringFromEditText(mBinding.etFname),
                Utils.getStringFromEditText(mBinding.etLname),
                Utils.getStringFromEditText(mBinding.etDesc),
                mSelectedJobTitleID,
                String.valueOf(preferredJobLocationId),
                license ? Utils.getStringFromEditText(mBinding.createProfileEtState) : null,
                license ? Utils.getStringFromEditText(mBinding.createProfileEtLicence) : null);
    }

    private void uploadImageApi(@NonNull String filePath) {
        viewModel.uploadImage(filePath);
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
                        .setAction(getString(R.string.txt_ok), view -> PermissionUtils.requestPermission(UpdateProfileActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.REQUEST_CODE.REQUEST_CODE_CAMERA)).show();
            } else {
                PermissionUtils.requestPermission(UpdateProfileActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void galleryClicked() {
        imageSourceType = 1;

        if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        this)) {
            getImageFromGallery();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(mBinding.etFname, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_accept), view -> PermissionUtils.requestPermission(UpdateProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.REQUEST_CODE.REQUEST_CODE_GALLERY)).show();
            } else {
                PermissionUtils.requestPermission(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            if (imageSourceType == 0) {
                takePhoto();
            } else {
                getImageFromGallery();
            }
        }
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int position, int isLicenseRequired) {
        mBinding.etJobTitle.setText(title);
        mSelectedJobTitleID = titleId;
        mBinding.etJobTitle.setText(title);
        if (isLicenseRequired == 0) {
            mBinding.createProfileInputLayoutLicence.setVisibility(View.GONE);
            mBinding.createProfileInputLayoutState.setVisibility(View.GONE);
            mBinding.createProfileEtLicence.setText("");
            mBinding.createProfileEtState.setText("");
        } else {
            mBinding.createProfileInputLayoutLicence.setVisibility(View.VISIBLE);
            mBinding.createProfileInputLayoutState.setVisibility(View.VISIBLE);
            mBinding.createProfileEtLicence.setText("");
            mBinding.createProfileEtState.setText("");

            if ((mProfileData.getUser().getLicenseNumber() != null && !TextUtils.isEmpty(mProfileData.getUser().getLicenseNumber())) && mProfileData.getUser().getJobtitleName().equals(title)) {
                mBinding.createProfileEtLicence.setText(mProfileData.getUser().getLicenseNumber());
                mBinding.createProfileEtState.setText(mProfileData.getUser().getState());
            }

        }
    }
}

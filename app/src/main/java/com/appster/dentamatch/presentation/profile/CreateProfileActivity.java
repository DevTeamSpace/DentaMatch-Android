/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityCreateProfileBinding;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse;
import com.appster.dentamatch.network.response.profile.StateList;
import com.appster.dentamatch.presentation.auth.UserVerifyPendingActivity;
import com.appster.dentamatch.presentation.common.SearchStateActivity;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by virender on 03/01/17.
 * To inject activity reference.
 */
public class CreateProfileActivity extends BaseLoadingActivity<CreateProfileViewModel>
        implements View.OnClickListener, ImageSelectedListener, JobTitleSelectionListener {

    private String mFilePath;
    private ActivityCreateProfileBinding mBinder;
    private String selectedJobTitle = "";
    private byte imageSourceType;
    private int isLicenceRequired = 0;
    private int jobTitleId;
    private int mPrevSelStatePos;
    private ArrayList<StateList> mStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_create_profile);
        initViews();
    }

    private void initViews() {
        mBinder.createProfile1BtnNext.setOnClickListener(this);
        mBinder.createProfile1IvProfileIcon.setOnClickListener(this);

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            mFilePath = PreferenceUtil.getProfileImagePath();
            Picasso.get().load(PreferenceUtil.getProfileImagePath())
                    .centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinder.createProfile1IvProfileIcon);
        }

        mBinder.etJobTitle.setOnClickListener(this);
        mBinder.createProfileEtState.setFocusableInTouchMode(false);
        mBinder.createProfileEtState.setCursorVisible(false);
        mBinder.createProfileEtState.setOnClickListener(this);
        mBinder.createProfileTvName.setText(getString(R.string.hi_user, PreferenceUtil.getFirstName()));
        mBinder.tvPreferredJobLocationVal.setText(PreferenceUtil.getPreferredJobLocationName());

        viewModel.getLicenseUpdate().observe(this, this::onSuccessLicenseUpdate);
        viewModel.getJobTitle().observe(this,
                title -> new BottomSheetJobTitle(CreateProfileActivity.this,
                        CreateProfileActivity.this,
                        0));
        viewModel.getUploadImage().observe(this,
                response ->
                        Picasso.get()
                                .load(response != null ? response.getFileUploadResponseData().getImageUrl() : "")
                                .centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                                .placeholder(R.drawable.profile_pic_placeholder)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(mBinder.createProfile1IvProfileIcon));
    }

    private void onSuccessLicenseUpdate(@Nullable LicenseUpdateResponse response) {
        if (response != null
                && response.getResult().getUserDetails().getIsVerified() == Constants.USER_VERIFIED_STATUS) {
            Intent profileCompletedIntent = new Intent(CreateProfileActivity.this, ProfileCompletedPendingActivity.class);
            profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, isLicenceRequired);
            startActivity(profileCompletedIntent);
            finish();
        } else {
            Intent profileCompletedIntent = new Intent(CreateProfileActivity.this, UserVerifyPendingActivity.class);
            profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, isLicenceRequired);
            startActivity(profileCompletedIntent);
            finish();
        }
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
                    new BottomSheetJobTitle(CreateProfileActivity.this, this, 0);
                } else {
                    callJobListApi();
                }
                break;
            case R.id.create_profile1_btn_next:
                if (validateInputData())
                    callUploadLicenseApi();
                break;
            case R.id.create_profile_et_state:
                Intent intent = new Intent(this, SearchStateActivity.class);
                if (mStateList != null && !mStateList.isEmpty()) {
                    intent.putExtra(Constants.BundleKey.PREV_SEL_STATE, mPrevSelStatePos);
                    intent.putParcelableArrayListExtra(Constants.BundleKey.STATE, mStateList);
                }
                startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_STATE);
                break;
            default:
                break;
        }
    }

    private void callUploadLicenseApi() {
        viewModel.updateLicence(jobTitleId,
                getTextFromEditText(mBinder.etDescAboutMe),
                isLicenceRequired == 1 ? getTextFromEditText(mBinder.createProfileEtLicence) : null,
                isLicenceRequired == 1 ? getTextFromEditText(mBinder.createProfileEtState) : null);
    }

    private boolean validateInputData() {
        if (TextUtils.isEmpty(selectedJobTitle)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
            return false;
        }
        if (TextUtils.isEmpty(getTextFromEditText(mBinder.etDescAboutMe))) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_summary_alert));
            return false;
        }
        if (isLicenceRequired == 1) {
            if (TextUtils.isEmpty(mBinder.createProfileEtLicence.getText().toString().trim())) {
                Utils.showToast(CreateProfileActivity.this, getString(R.string.blank_licence_number));
                return false;
            }
            if (mBinder.createProfileEtLicence.getText().toString().trim().length() > Constants.LICENCE_MAX_LENGTH) {
                Utils.showToast(CreateProfileActivity.this, getString(R.string.licence_number_length));
                return false;
            }
            if (mBinder.createProfileEtLicence.getText().toString().trim().contains(" ")) {
                Utils.showToast(CreateProfileActivity.this, getString(R.string.licence_number_blnk_space_alert));
                return false;
            }
            if (mBinder.createProfileEtLicence.getText().toString().trim().charAt(0) == '-' || mBinder.createProfileEtLicence.getText().toString().trim().charAt(mBinder.createProfileEtLicence.getText().toString().trim().length() - 1) == '-') {
                Utils.showToast(CreateProfileActivity.this, getString(R.string.licence_number_hyfen_alert));
                return false;
            }
            if (TextUtils.isEmpty(mBinder.createProfileEtState.getText().toString().trim())) {
                Utils.showToast(CreateProfileActivity.this, getString(R.string.blank_state_alert));
                return false;
            }
            if (mBinder.createProfileEtState.getText().toString().trim().length() >= Constants.DEFAULT_FIELD_LENGTH) {
                Utils.showToast(CreateProfileActivity.this, getString(R.string.state_max_length));
                return false;
            }
        }
        return true;
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    private void uploadImageApi(@NonNull String filePath) {
        viewModel.uploadImage(filePath);
    }

    private void callJobListApi() {
        viewModel.requestJobTitle();
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
                        .setAction(getString(R.string.txt_ok), view -> PermissionUtils.requestPermission(CreateProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA)).show();
            } else {
                PermissionUtils.requestPermission(CreateProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
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
                        .setAction(getString(R.string.txt_accept), view -> PermissionUtils.requestPermission(CreateProfileActivity.this,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_STATE) {
                mStateList = data.getParcelableArrayListExtra(Constants.BundleKey.STATE);
                mPrevSelStatePos = data.getIntExtra(Constants.BundleKey.PREV_SEL_STATE, -1);
                mBinder.createProfileEtState.setText(data.getStringExtra(Constants.BundleKey.SEL_STATE));
                mBinder.etDescAboutMe.setFocusable(true);
                mBinder.etDescAboutMe.requestFocus();
                return;
            }
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);

            } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = data.getData();
                mFilePath = CameraUtil.getInstance().getGalleryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            }

            if (mFilePath != null) {
                uploadImageApi(mFilePath);
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
    public void onJobTitleSelection(String title, int titleId, int position, int isLicenseRequired) {
        PreferenceUtil.setJobTitle(title);
        // PreferenceUtil.setJobTitleId(titleId);
        jobTitleId = titleId;
        PreferenceUtil.setJobTitlePosition(position);
        this.isLicenceRequired = isLicenseRequired;
        mBinder.etJobTitle.setText(title);
        selectedJobTitle = title;
        if (isLicenseRequired == 0) {
            mBinder.createProfileInputLayoutLicence.setVisibility(View.GONE);
            mBinder.licToolTip.setVisibility(View.GONE);
            mBinder.createProfileInputLayoutState.setVisibility(View.GONE);
            mBinder.createProfileEtLicence.setText("");
            mBinder.createProfileEtState.setText("");
        } else {
            mBinder.createProfileInputLayoutLicence.setVisibility(View.VISIBLE);
            mBinder.licToolTip.setVisibility(View.VISIBLE);
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
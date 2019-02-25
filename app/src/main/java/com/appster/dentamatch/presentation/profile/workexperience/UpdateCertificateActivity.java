/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile.workexperience;

import android.Manifest;
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
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityUpdateCertificateBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.DateSelectedListener;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.StringUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetDatePicker;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by virender on 20/01/17.
 * To inject activity reference.
 */
public class UpdateCertificateActivity extends BaseLoadingActivity<UpdateCertificateViewModel>
        implements View.OnClickListener, ImageSelectedListener, DateSelectedListener {

    private ActivityUpdateCertificateBinding mBinder;
    private String mFilePath;
    private byte imageSourceType;
    private CertificatesList data;
    private boolean isFromDentalStateBoard;
    private boolean isImageUploaded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_update_certificate);
        initViews();
        viewModel.getCertificateUpload().observe(this, this::onSuccessCertificateUpload);
        viewModel.getCertificateError().observe(this, e -> isImageUploaded = false);
        viewModel.getDentalUpload().observe(this, this::onSuccessDentalUpload);
        viewModel.getSaveCertificate().observe(this, this::onSuccessSaveCertificate);
    }

    private void onSuccessSaveCertificate(@Nullable BaseResponse response) {
        if (response != null) {
            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
            finish();
        }
    }

    private void onSuccessDentalUpload(@Nullable FileUploadResponse response) {
        if (response != null) {
            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
            finish();
        }
    }

    private void onSuccessCertificateUpload(@Nullable FileUploadResponse response) {
        if (response != null) {
            data.setImage(response.getFileUploadResponseData().getImageUrl());
            isImageUploaded = true;
            Picasso.get()
                    .load(new File(mFilePath))
                    .centerCrop()
                    .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinder.ivCertificateUploadIcon);
        }
    }

    private void initViews() {
        mBinder.toolbarUpdateCertificate.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
        mBinder.ivCertificateUploadIcon.setOnClickListener(this);
        mBinder.tvValidityDatePicker.setOnClickListener(this);
        mBinder.toolbarUpdateCertificate.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnSave.setOnClickListener(this);
        mBinder.tvValidityDatePicker.setOnClickListener(this);

        if (getIntent() != null) {
            isFromDentalStateBoard = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
            data = getIntent().getParcelableExtra(Constants.INTENT_KEY.DATA);
            setViewData();
        }
    }

    private void setViewData() {
        if (data != null) {
            mBinder.tvCertificatesName.setText(data.getCertificateName());
            if (!TextUtils.isEmpty(data.getImage())) {
                Picasso.get().load(data.getImage()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.ic_upload).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.ivCertificateUploadIcon);
                isImageUploaded = true;
            }
            if (isFromDentalStateBoard || (data != null && !StringUtils.isNullOrEmpty(data.getCertificateName()) && data.getCertificateName().equalsIgnoreCase(getString(R.string.resume)))) {
                mBinder.tvValidityDatePicker.setVisibility(View.GONE);
            } else {
                mBinder.tvValidityDatePicker.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(data.getValidityDate())) {
                mBinder.tvValidityDatePicker.setText(Utils.dateFormatYYYYMMMMDD(data.getValidityDate()));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_certificate_upload_icon:
                callBottomSheet();
                break;
            case R.id.tv_validity_date_picker:
                callBottomSheetDate();
                break;
            case R.id.btn_save:
                if (isFromDentalStateBoard) {
                    //TODO : dental State board has been removed for now , keeping the code for now for future reference.
                    if (TextUtils.isEmpty(mFilePath)) {
                        Utils.showToast(getApplicationContext(), getString(R.string.blank_satate_board_photo_alert));
                        return;
                    }
                    uploadDentaImageApi(mFilePath);
                } else {
                    if (!TextUtils.isEmpty(data.getImage()) || isImageUploaded) {
                        if ((data != null && !data.getCertificateName().equalsIgnoreCase(getString(R.string.resume))) && TextUtils.isEmpty(mBinder.tvValidityDatePicker.getText().toString().trim())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.blank_certificate_validity_date, data.getCertificateName()));
                            return;
                        }
                        postCertificateData();
                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.alert_upload_photo_first));
                    }
                }
                break;
            case R.id.iv_tool_bar_left:
                finish();
                break;
            default:
                break;
        }
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    private void callBottomSheetDate() {
        new BottomSheetDatePicker(UpdateCertificateActivity.this, this, 0);
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
                Snackbar.make(mBinder.ivCertificateUploadIcon, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_ok), view ->
                                PermissionUtils.requestPermission(UpdateCertificateActivity.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constants.REQUEST_CODE.REQUEST_CODE_CAMERA))
                        .show();
            } else {
                PermissionUtils.requestPermission(UpdateCertificateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
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
                Snackbar.make(mBinder.ivCertificateUploadIcon, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_accept), view ->
                                PermissionUtils.requestPermission(UpdateCertificateActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constants.REQUEST_CODE.REQUEST_CODE_GALLERY))
                        .show();
            } else {
                PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = intent.getData();
                mFilePath = CameraUtil.getInstance().getGalleryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            }
            if (mFilePath != null) {
                if (!isFromDentalStateBoard) {
                    uploadCertificateImageApi(mFilePath, "" + data.getId());
                }
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
    public void onDateSelection(String date, int position) {
        mBinder.tvValidityDatePicker.setText(Utils.dateFormatYYYYMMMMDD(date));
    }

    private void uploadCertificateImageApi(final String filePath, String certificateId) {
        viewModel.uploadCertificateImage(filePath, certificateId);
    }

    private void uploadDentaImageApi(String filePath) {
        viewModel.uploadDentalImage(filePath);
    }

    private void postCertificateData() {
        viewModel.saveCertificate(data.getId(),
                Utils.getRequriedServerDateFormet(mBinder.tvValidityDatePicker.getText().toString()));
    }
}
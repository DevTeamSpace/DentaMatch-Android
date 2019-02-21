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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityCertificateBinding;
import com.appster.dentamatch.interfaces.DateSelectedListener;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.response.certificates.CertificateResponse;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetDatePicker;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by virender on 10/01/17.
 * To inject activity reference.
 */
public class CertificateActivity extends BaseLoadingActivity<CertificateViewModel>
        implements View.OnClickListener, ImageSelectedListener, DateSelectedListener {

    private ActivityCertificateBinding mBinder;
    private String mFilePath;
    private ImageView ivTemp;
    private TextView tvTemp;
    private byte imageSourceType;
    private String certificateId = "";
    private int position;
    private final ArrayList<CertificatesList> certificateList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_certificate);
        initViews();
        callCertificateListApi();
        viewModel.getCertificationList().observe(this, this::onSuccessCertificationListRequest);
        viewModel.getCertificationListFailed().observe(this, e -> mBinder.btnNext.setVisibility(View.VISIBLE));
        viewModel.getCertificateUpload().observe(this, this::onSuccessCertificateUpload);
        viewModel.getCertificateListSave().observe(this, this::onSuccessCertificateListSave);
    }

    private void onSuccessCertificateListSave(@Nullable BaseResponse response) {
        if (response != null) {
            startActivity(new Intent(CertificateActivity.this, AboutMeActivity.class));
        }
    }

    private void onSuccessCertificateUpload(@Nullable FileUploadResponse response) {
        if (response != null) {
            certificateList.get(position).setImage(response.getFileUploadResponseData().getImageUrl());
            certificateList.get(position).setImageUploaded(true);
            ivTemp.setImageBitmap(CameraUtil.getInstance().decodeBitmapFromPath(response.getFileUploadResponseData().getImageUrl(),
                    CertificateActivity.this,
                    Constants.IMAGE_DIME_CERTIFICATE,
                    Constants.IMAGE_DIME_CERTIFICATE));
        }
    }

    private void onSuccessCertificationListRequest(@Nullable CertificateResponse response) {
        if (response != null) {
            certificateList.clear();
            if (response.getCertificateResponseData().getCertificatesLists() != null) {
                if (certificateList.size() > 0) {
                    certificateList.addAll(response.getCertificateResponseData().getCertificatesLists());
                }
                inflateViews();
                mBinder.btnNext.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initViews() {
        mBinder.toolbarCertificates.tvToolbarGeneralLeft.setText(getString(R.string.header_certification));
        mBinder.layoutCertificatesHeader.tvTitle.setText(getString(R.string.cetification_title));
        mBinder.layoutCertificatesHeader.tvDescription.setText(getString(R.string.lorem_ipsum));
        mBinder.btnNext.setOnClickListener(this);
        mBinder.toolbarCertificates.ivToolBarLeft.setOnClickListener(this);

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.get()
                    .load(PreferenceUtil.getProfileImagePath())
                    .centerCrop()
                    .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .into(mBinder.layoutCertificatesHeader.ivProfileIcon);
        }
        mBinder.layoutCertificatesHeader.progressBar.setProgress(Constants.PROFILE_PERCENTAGE.CERTIFICATE);
        mBinder.btnNext.setOnClickListener(this);
    }


    private void callBottomSheetDate(int pos) {
        new BottomSheetDatePicker(CertificateActivity.this, this, pos);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (checkValidation()) {
                    postCertificateData();
                }
                break;
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private boolean checkValidation() {
        boolean isImageUploaded = false;

        for (int i = 0; i < certificateList.size(); i++) {
            if (certificateList.get(i).isImageUploaded()) {
                isImageUploaded = true;

                if (TextUtils.isEmpty(certificateList.get(i).getValidityDate())) {
                    Utils.showToast(CertificateActivity.this, getString(R.string.blank_certificate_validity_date, certificateList.get(i).getCertificateName()));
                    return false;
                }
            }
        }

        if (!isImageUploaded) {
            Utils.showToast(CertificateActivity.this, getString(R.string.blank_certificate_photo_alert));
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
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
                uploadCertificateImageApi(mFilePath, certificateId);
            }
        }
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
                Snackbar.make(mBinder.layoutCertificatesHeader.ivProfileIcon, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_ok), view -> PermissionUtils.requestPermission(CertificateActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.REQUEST_CODE.REQUEST_CODE_CAMERA)).show();
            } else {
                PermissionUtils.requestPermission(CertificateActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void galleryClicked() {
        imageSourceType = 1;

        if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
            getImageFromGallery();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(mBinder.layoutCertificatesHeader.ivProfileIcon,
                        this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.txt_accept), view -> PermissionUtils.requestPermission(CertificateActivity.this,
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
    public void onDateSelection(String date, int pos) {
        tvTemp.setText(Utils.dateFormatYYYYMMMMDD(date));
        certificateList.get(position).setValidityDate(date);
    }

    private void callCertificateListApi() {
        viewModel.requestCertificationList();
    }

    private void inflateViews() {
        mBinder.layoutCertificatesInflater.removeAllViews();

        for (int i = 0; i < certificateList.size(); i++) {
            final View certificatesView = getLayoutInflater().inflate(R.layout.layout_certificates_cell,
                    mBinder.layoutCertificatesInflater,
                    false);

            final ImageView ivCertificate = certificatesView.findViewById(R.id.iv_certificate_upload_icon);
            TextView tvUploadPhoto = certificatesView.findViewById(R.id.tv_upload_photo);
            TextView tvDatePicker = certificatesView.findViewById(R.id.tv_validity_date_picker);
            final TextView tvCertificateName = certificatesView.findViewById(R.id.tv_certificates_name);
            tvCertificateName.setText(certificateList.get(i).getCertificateName());

            if (!TextUtils.isEmpty(certificateList.get(i).getValidityDate())) {
                tvDatePicker.setText(certificateList.get(i).getValidityDate());
            }

            if (!TextUtils.isEmpty(certificateList.get(i).getImage())) {
                certificateList.get(i).setImageUploaded(true);
                Picasso.get()
                        .load(certificateList.get(i).getImage())
                        .centerCrop()
                        .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                        .placeholder(R.drawable.ic_upload)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(ivCertificate);
            }

            tvUploadPhoto.setTag(i);
            ivCertificate.setTag(i);
            tvDatePicker.setTag(i);

            ivCertificate.setOnClickListener(view -> {
                ivTemp = (ImageView) view;
                position = (Integer) ivCertificate.getTag();
                certificateId = String.valueOf(certificateList.get((Integer) ivCertificate.getTag()).getId());
                callBottomSheet();
            });

            tvDatePicker.setOnClickListener(view -> {
                tvTemp = (TextView) view;
                position = (Integer) ivCertificate.getTag();
                hideKeyboard();

                if (certificateList.get(position).isImageUploaded()) {
                    callBottomSheetDate((Integer) ivCertificate.getTag());
                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.alert_upload_photo_first));
                }
            });

            mBinder.layoutCertificatesInflater.addView(certificatesView);
        }
    }

    private void uploadCertificateImageApi(final String filePath, String certificateId) {
        viewModel.uploadCertificateImage(filePath, certificateId);
    }

    private void postCertificateData() {
        viewModel.saveCertificateList(certificateList);
    }
}

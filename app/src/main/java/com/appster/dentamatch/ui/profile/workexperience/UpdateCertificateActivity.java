package com.appster.dentamatch.ui.profile.workexperience;

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
import android.util.Log;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityUpdateCertificateBinding;
import com.appster.dentamatch.interfaces.DateSelectedListener;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.certificates.CertificateRequest;
import com.appster.dentamatch.network.request.certificates.UpdateCertificates;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetDatePicker;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.params.CoreConnectionPNames;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by virender on 20/01/17.
 */
public class UpdateCertificateActivity extends BaseActivity implements View.OnClickListener, ImageSelectedListener, DateSelectedListener {
    private static final String TAG = "UpdateCertificate";
    private ActivityUpdateCertificateBinding mBinder;
    private String mFilePath;
    private byte imageSourceType;
    private CertificatesList data;
    private boolean isFromDentalStateBoard;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_update_certificate);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarUpdateCertificate.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
        mBinder.ivCertificateUpoloadIcon.setOnClickListener(this);
        mBinder.tvValidityDatePicker.setOnClickListener(this);
        mBinder.toolbarUpdateCertificate.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnSave.setOnClickListener(this);
        mBinder.tvValidityDatePicker.setOnClickListener(this);

        if (getIntent() != null) {
            data = getIntent().getParcelableExtra(Constants.INTENT_KEY.DATA);
            isFromDentalStateBoard = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
            data = (CertificatesList) getIntent().getParcelableExtra(Constants.INTENT_KEY.DATA);
            setViewData();
        }
    }

    private void setViewData() {
        if (data != null) {
            mBinder.tvCertificatesName.setText(data.getCertificateName());
            if (!TextUtils.isEmpty(data.getImageUrl())) {
                Picasso.with(UpdateCertificateActivity.this).load(data.getImageUrl()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.ivCertificateUpoloadIcon);

            }
            if (isFromDentalStateBoard) {
                mBinder.tvValidityDatePicker.setVisibility(View.GONE);
            } else {
                mBinder.tvValidityDatePicker.setVisibility(View.VISIBLE);

            }
            mBinder.tvValidityDatePicker.setText(data.getValidityDate());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_certificate_upoload_icon:
                callBottomSheet();
                break;

            case R.id.tv_validity_date_picker:
                callBottomSheetDate();

                break;
            case R.id.btn_save:
                if (isFromDentalStateBoard) {
                    if (TextUtils.isEmpty(mFilePath)) {
                        Utils.showToast(getApplicationContext(), getString(R.string.blank_satate_board_photo_alert));
                        return;
                    }
                    uploadImageApi(mFilePath, Constants.APIS.IMAGE_TYPE_STATE_BOARD);
                } else {


                    if (TextUtils.isEmpty(mBinder.tvValidityDatePicker.getText().toString().trim())) {
                        Utils.showToast(getApplicationContext(), getString(R.string.blank_certificate_validity_date));
                        return;
                    }
                    postCertificateData(preparePostValidation());
                }
                break;

            case R.id.iv_tool_bar_left:
                finish();
                break;
        }
    }

    private CertificateRequest preparePostValidation() {
        CertificateRequest certificateRequest = new CertificateRequest();
        ArrayList<UpdateCertificates> updateCertificatesArrayList = new ArrayList<>();
        UpdateCertificates updateCertificates = new UpdateCertificates();
        updateCertificates.setId(data.getId());
        updateCertificates.setValue(mBinder.tvValidityDatePicker.getText().toString());
        updateCertificatesArrayList.add(updateCertificates);
        certificateRequest.setUpdateCertificatesList(updateCertificatesArrayList);
        return certificateRequest;
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
                Snackbar.make(mBinder.ivCertificateUpoloadIcon, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(UpdateCertificateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);

                            }
                        }).show();
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
                Snackbar.make(mBinder.ivCertificateUpoloadIcon, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Accept", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(UpdateCertificateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_CAMERA) {
                mFilePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);

            } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = intent.getData();
                mFilePath = CameraUtil.getInstance().getGallaryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            }
            Log.d("Tag", "file path" + mFilePath);

            if (mFilePath != null) {
//                mBinder.createProfile1IvProfileIcon.setImageBitmap(CameraUtil.getInstance().decodeBitmapFromPath(mFilePath, this, Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN));
                Picasso.with(UpdateCertificateActivity.this).load(new File(mFilePath)).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.ivCertificateUpoloadIcon);
                uploadCertificateImageApi(mFilePath, "" + data.getId());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "request permission called --" + grantResults.length);

        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            Log.d(TAG, "request permission called if granted --");
            if (imageSourceType == 0) {
                takePhoto();
            } else {
                getImageFromGallery();
            }

        }

    }

    @Override
    public void onDateSelection(String date, int position) {
        mBinder.tvValidityDatePicker.setText(date);
    }

    private void uploadCertificateImageApi(final String filePath, String certificateId) {
        showProgressBar(getString(R.string.please_wait));
        File file = new File(filePath);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody uploadType = RequestBody.create(MediaType.parse("multipart/form-data"), certificateId);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        Call<FileUploadResponse> response = webServices.uploadCertificateImage(uploadType, fbody);
        response.enqueue(new BaseCallback<FileUploadResponse>(UpdateCertificateActivity.this) {
            @Override
            public void onSuccess(FileUploadResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response != null && response.getStatus() == 1) {
                    // showSnackBarFromTop(response.getMessage(), false);

                }
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGE(TAG, " ImageUpload failed!");

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
        response.enqueue(new BaseCallback<FileUploadResponse>(UpdateCertificateActivity.this) {
            @Override
            public void onSuccess(FileUploadResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response != null && response.getStatus() == 1) {
                    // showSnackBarFromTop(response.getMessage(), false);
//                    Utils.showToast(getApplicationContext(), "url is---" + response.getFileUploadResponseData().getImageUrl());

                }
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGE(TAG, " ImageUpload failed!");

            }
        });
    }

    private void postCertificateData(CertificateRequest certificateRequest) {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveCertificate(certificateRequest).enqueue(new BaseCallback<BaseResponse>(UpdateCertificateActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
            }
        });

    }
}

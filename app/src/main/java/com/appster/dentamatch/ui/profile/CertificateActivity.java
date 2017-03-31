package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityCertificateBinding;
import com.appster.dentamatch.interfaces.DateSelectedListener;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.certificates.CertificateRequest;
import com.appster.dentamatch.network.request.certificates.UpdateCertificates;
import com.appster.dentamatch.network.response.certificates.CertificateResponse;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by virender on 10/01/17.
 */
public class CertificateActivity extends BaseActivity implements View.OnClickListener, ImageSelectedListener, DateSelectedListener {
    private static final String TAG = "CertificateActivity";
    private ActivityCertificateBinding mBinder;
    private String mFilePath;
    private ImageView ivTemp;
    private TextView tvTemp;
    private byte imageSourceType;
    private String certificateId = "";
    private int position;
    private ArrayList<CertificatesList> certificateList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_certificate);
        initViews();
        callCertificateListApi();
    }

    private void initViews() {
        mBinder.toolbarCertificates.tvToolbarGeneralLeft.setText(getString(R.string.header_certification));
        mBinder.layoutCertificatesHeader.tvTitle.setText(getString(R.string.cetification_title));
        mBinder.btnNext.setOnClickListener(this);
        mBinder.toolbarCertificates.ivToolBarLeft.setOnClickListener(this);

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.with(getApplicationContext())
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
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_next:
                if (checkValidation()) {
                    postCertificateData(prepareCertificateSaveRequest());
                }
                break;

            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    private CertificateRequest prepareCertificateSaveRequest() {
        CertificateRequest certificateRequest = new CertificateRequest();
        ArrayList<UpdateCertificates> updateCertificatesArrayList = new ArrayList<>();

        for (int i = 0; i < certificateList.size(); i++) {
            if (certificateList.get(i).isImageUploaded() && !TextUtils.isEmpty(certificateList.get(i).getImage())) {
                UpdateCertificates updateCertificates = new UpdateCertificates();
                updateCertificates.setId(certificateList.get(i).getId());
                updateCertificates.setValue(certificateList.get(i).getValidityDate());
                updateCertificatesArrayList.add(updateCertificates);
            }
        }

        certificateRequest.setUpdateCertificatesList(updateCertificatesArrayList);
        return certificateRequest;
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
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
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
                ivTemp.setImageBitmap(CameraUtil.getInstance().decodeBitmapFromPath(mFilePath,
                        this,
                        Constants.IMAGE_DIME_CERTIFICATE,
                        Constants.IMAGE_DIME_CERTIFICATE));

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
                        .setAction(getString(R.string.txt_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CertificateActivity.this,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
                            }
                        }).show();
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
                        .setAction(getString(R.string.txt_accept), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CertificateActivity.this,
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


    @Override
    public void onDateSelection(String date, int pos) {
        tvTemp.setText(Utils.dateFormatYYYYMMMMDD(date));
        certificateList.get(position).setValidityDate(date);
    }

    private void callCertificateListApi() {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getCertificationList().enqueue(new BaseCallback<CertificateResponse>(CertificateActivity.this) {
            @Override
            public void onSuccess(CertificateResponse response) {
                if (response.getStatus() == 1) {
                    if (certificateList != null) {
                        certificateList.clear();
                    }

                    if (response.getCertificateResponseData().getCertificatesLists() != null) {
                        certificateList.addAll(response.getCertificateResponseData().getCertificatesLists());
                        inflateViews();
                    }

                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<CertificateResponse> call, BaseResponse baseResponse) {
            }
        });

    }

    private void inflateViews() {
        mBinder.layoutCertificatesInflater.removeAllViews();

        for (int i = 0; i < certificateList.size(); i++) {
            final View certificatesView = getLayoutInflater().inflate(R.layout.layout_certificates_cell,
                    mBinder.layoutCertificatesInflater,
                    false);

            final ImageView ivCertificate = (ImageView) certificatesView.findViewById(R.id.iv_certificate_upoload_icon);
            TextView tvUploadPhoto = (TextView) certificatesView.findViewById(R.id.tv_upload_photo);
            TextView tvDatePicker = (TextView) certificatesView.findViewById(R.id.tv_validity_date_picker);
            final TextView tvCertificateName = (TextView) certificatesView.findViewById(R.id.tv_certificates_name);
            tvCertificateName.setText(certificateList.get(i).getCertificateName());

            if (!TextUtils.isEmpty(certificateList.get(i).getValidityDate())) {
                tvDatePicker.setText(certificateList.get(i).getValidityDate());
            }

            if (!TextUtils.isEmpty(certificateList.get(i).getImage())) {
                certificateList.get(i).setImageUploaded(true);
                Picasso.with(getApplicationContext())
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

            ivCertificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivTemp = (ImageView) view;
                    position = (Integer) ivCertificate.getTag();
                    certificateId = String.valueOf(certificateList.get((Integer) ivCertificate.getTag()).getId());
                    callBottomSheet();
                }
            });

            tvDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvTemp = (TextView) view;
                    position = (Integer) ivCertificate.getTag();
                    hideKeyboard();

                    if (certificateList.get(position).isImageUploaded()) {
                        callBottomSheetDate((Integer) ivCertificate.getTag());
                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.alert_upload_photo_first));
                    }
                }
            });

            mBinder.layoutCertificatesInflater.addView(certificatesView);
        }
    }

    private void uploadCertificateImageApi(final String filePath, String certificateId) {
        showProgressBar(getString(R.string.please_wait));
        File file = new File(filePath);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody uploadType = RequestBody.create(MediaType.parse("multipart/form-data"), certificateId);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        Call<FileUploadResponse> response = webServices.uploadCertificateImage(uploadType, fbody);
        response.enqueue(new BaseCallback<FileUploadResponse>(CertificateActivity.this) {
            @Override
            public void onSuccess(FileUploadResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    certificateList.get(position).setImage(filePath);
                    certificateList.get(position).setImageUploaded(true);
                }
            }

            @Override
            public void onFail(Call<FileUploadResponse> call, BaseResponse baseResponse) {
            }
        });
    }

    private void postCertificateData(CertificateRequest certificateRequest) {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveCertificate(certificateRequest).enqueue(new BaseCallback<BaseResponse>(CertificateActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    startActivity(new Intent(CertificateActivity.this, AboutMeActivity.class));
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });
    }
}

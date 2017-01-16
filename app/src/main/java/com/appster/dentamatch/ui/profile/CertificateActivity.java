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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.annotations.NonNull;
import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityCertificateBinding;
import com.appster.dentamatch.interfaces.DateSelectedListener;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.appster.dentamatch.network.response.auth.CertificateResponse;
import com.appster.dentamatch.network.response.auth.CertificatesList;
import com.appster.dentamatch.network.response.auth.JobTitleResponse;
import com.appster.dentamatch.network.response.auth.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.workexperience.WorkExpListActivity;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.NetworkMonitor;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetDatePicker;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 10/01/17.
 */
public class CertificateActivity extends BaseActivity implements View.OnClickListener, ImageSelectedListener, DateSelectedListener {
    private ActivityCertificateBinding mBinder;
    private String mFilePath;
    private ImageView ivTemp;
    private TextView tvTemp;
    private byte imageSourceType;



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
            Picasso.with(getApplicationContext()).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(mBinder.layoutCertificatesHeader.ivProfileIcon);

        }
        mBinder.layoutCertificatesHeader.progressBar.setProgress(90);
        mBinder.btnNext.setOnClickListener(this);
    }


    private void callBottomSheetDate() {
        new BottomSheetDatePicker(CertificateActivity.this, this);

    }


    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                startActivity(new Intent(this, AboutMeActivity.class));
                break;
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
        }

    }

    public void getImageFromGallery() {
        Intent gIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gIntent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(gIntent, "Select File"),
                Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
    }

    public void takePhoto() {


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Tag", "request permisison called --");
        Log.d("Tag", "request permisison called --" + grantResults.length);


        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            Log.d("Tag", "request permisison called if granted --");
            if(imageSourceType==0){
                takePhoto();
            }else{
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
                mFilePath = CameraUtil.getInstance().getGallaryPAth(selectedImageUri, this);
                mFilePath = CameraUtil.getInstance().compressImage(mFilePath, this);
            }
            Log.d("Tag", "file path" + mFilePath);

            if (mFilePath != null) {
//                ivProfile.setImageBitmap(CameraUtil.getInstance().decodeBitmapFromPath(mFilePath, this, 100, 100));
                Picasso.with(CertificateActivity.this).load(new File(mFilePath)).centerCrop().resize(Constants.IMAGE_DIME_CERTIFICATE, Constants.IMAGE_DIME_CERTIFICATE).placeholder(R.drawable.ic_upload).into(ivTemp);

            }
        }
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    @Override
    public void cameraClicked() {
        imageSourceType=0;
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.CAMERA, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this)) {
            takePhoto();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(mBinder.layoutCertificatesHeader.ivProfileIcon, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CertificateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(CertificateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void gallaryClicked() {
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) && PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
            getImageFromGallery();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(mBinder.layoutCertificatesHeader.ivProfileIcon, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Accept", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CertificateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
            }
        }

    }


    @Override
    public void onDateSelection(String date) {
        tvTemp.setText(date);
    }

    private void callCertificateListApi() {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getCertificationList().enqueue(new BaseCallback<CertificateResponse>(CertificateActivity.this) {
            @Override
            public void onSuccess(CertificateResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    inflateViews(response.getCertificateResponseData().getCertificatesLists());
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<CertificateResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }
    private void inflateViews(ArrayList<CertificatesList> lists) {
        mBinder.layoutCertificatesInflater.removeAllViews();
        for (int i = 0; i < lists.size(); i++) {
            final View certificatesView = getLayoutInflater().inflate(R.layout.layout_certificates_cell, mBinder.layoutCertificatesInflater, false);
            ImageView ivCertificate = (ImageView) certificatesView.findViewById(R.id.iv_certificate_upoload_icon);
            TextView tvUplodPhoto = (TextView) certificatesView.findViewById(R.id.tv_upload_photo);
            TextView tvDatePicker = (TextView) certificatesView.findViewById(R.id.tv_validity_date_picker);
            final TextView tvCertificateName = (TextView) certificatesView.findViewById(R.id.tv_certificates_name);
            tvCertificateName.setText(lists.get(i).getCertificateName());
            tvDatePicker.setText(lists.get(i).getValidityDate());
            if (!TextUtils.isEmpty(lists.get(i).getImage())) {
                Picasso.with(getApplicationContext()).load(lists.get(i).getImage()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(ivCertificate);

            }
            tvUplodPhoto.setTag(i);
            ivCertificate.setTag(i);
            tvUplodPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            ivCertificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivTemp = (ImageView) view;
                    if (PermissionUtils.checkPermissionGranted(Manifest.permission.CAMERA, CertificateActivity.this) &&
                            PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, CertificateActivity.this) &&
                            PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, CertificateActivity.this)) {
                        callBottomSheet();

                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(CertificateActivity.this, Manifest.permission.CAMERA)) {
                            Snackbar.make(tvCertificateName, getResources().getString(R.string.text_camera_permision),
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            PermissionUtils.requestPermission(CertificateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA
                                            );
                                        }
                                    }).show();
                        } else {
                            PermissionUtils.requestPermission(CertificateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
                        }
                    }
                }
            });
            tvDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvTemp = (TextView) view;
                    hideKeyboard();
                    callBottomSheetDate();
                }
            });
            mBinder.layoutCertificatesInflater.addView(certificatesView);
        }
    }

}

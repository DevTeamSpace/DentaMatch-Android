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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityCreateProfile1Binding;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.auth.JobTitleResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.NetworkMonitor;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetView;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;

/**
 * Created by virender on 03/01/17.
 */
public class CreateProfileActivity1 extends BaseActivity implements View.OnClickListener, ImageSelectedListener {
    private String TAG = "CreateProfileActivity1";
    private ImageSelectedListener imageSelectedListener;
    private String mFilePath;
    private ActivityCreateProfile1Binding mBinder;
    private String selectedJobtitle = "";
    private byte imageSourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_profile1);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_create_profile1);

        initViews();
        callJobListApi();

    }

    private void initViews() {
        mBinder.createProfile1BtnNotNow.setOnClickListener(this);
        mBinder.createProfile1BtnNext.setOnClickListener(this);
        mBinder.createProfile1IvProfileIcon.setOnClickListener(this);


        mBinder.createProfileTvName.setText("Hi " + PreferenceUtil.getFirstName());


        mBinder.spinnerJobTitleCreateProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceUtil.setJobTitle(PreferenceUtil.getJobTitleList().get(i).getJobTitle());
                selectedJobtitle = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_profile1_iv_profile_icon:
//                if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) && PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                callBottomSheet();
//
//                } else {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                        Snackbar.make(mBinder.createProfile1IvProfileIcon, this.getResources().getString(R.string.text_camera_permision),
//                                Snackbar.LENGTH_INDEFINITE)
//                                .setAction(getString(R.string.accept), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        PermissionUtils.requestPermission(CreateProfileActivity1.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
//
//                                    }
//                                }).show();
//                    } else {
//                        PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
//                    }
//                }

                break;
            case R.id.create_profile1_btn_next:
                if (TextUtils.isEmpty(mFilePath)) {
                    Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_photo_alert));
                    return;
                }
                if (TextUtils.isEmpty(selectedJobtitle)) {
                    Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
                    return;
                }
                Intent intent = new Intent(this, CreateProfileActivity2.class);
                startActivity(intent);
                break;
            case R.id.create_profile1_btn_not_now:

                Alert.createYesNoAlert(CreateProfileActivity1.this, getString(R.string.ok), getString(R.string.cancel), "", getString(R.string.alert_profile), new Alert.OnAlertClickListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        Utils.showToast(CreateProfileActivity1.this, "Comming soon....");
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
                    String title[] = new String[response.getJobTitleResponseData().getJobTitleList().size()];
                    for (int i = 0; i < response.getJobTitleResponseData().getJobTitleList().size(); i++) {
                        title[i] = response.getJobTitleResponseData().getJobTitleList().get(i).getJobTitle();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateProfileActivity1.this,
                            android.R.layout.simple_dropdown_item_1line, title);
                    mBinder.spinnerJobTitleCreateProfile.setPrompt(getString(R.string.lable_job_title));
                    mBinder.spinnerJobTitleCreateProfile.setAdapter(arrayAdapter);
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
//        takePhoto();

        imageSourceType=0;
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
    public void gallaryClicked() {
//        getImageFromGallery();
        imageSourceType=1;

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
                PreferenceUtil.setProfileImagePath(mFilePath);
//                ivProfile.setImageBitmap(CameraUtil.getInstance().decodeBitmapFromPath(mFilePath, this, 100, 100));
                Picasso.with(CreateProfileActivity1.this).load(new File(mFilePath)).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(mBinder.createProfile1IvProfileIcon);

            }
        }
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
    public String getActivityName() {
        return null;
    }
}



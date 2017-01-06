package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityCreateProfile1Binding;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.BottomSheetView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by virender on 03/01/17.
 */
public class CreateProfileActivity1 extends Activity implements View.OnClickListener, ImageSelectedListener {
    private ImageSelectedListener imageSelectedListener;
    private String mFilePath;
    private ActivityCreateProfile1Binding mBinder;
    private String[] spinnerList = {"job title,Dev", "Qa", "Po", "Sm", "tl", "security"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_profile1);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_create_profile1);

        initViews();
    }

    private void initViews() {
        mBinder.createProfile1BtnNotNow.setOnClickListener(this);
        mBinder.createProfile1BtnNext.setOnClickListener(this);
        mBinder.createProfile1IvProfileIcon.setOnClickListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerList);
//        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
//                findViewById(R.id.android_material_design_spinner);
        mBinder.spinnerJobTitleCreateProfile.setPrompt(getString(R.string.lable_job_title));
        mBinder.spinnerJobTitleCreateProfile.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_profile1_iv_profile_icon:
                callBottomSheet();
                break;
            case R.id.create_profile1_btn_next:
                Intent intent = new Intent(this, CreateProfileActivity2.class);
                intent.putExtra(Constants.INTENT_KEY.IMAGE_PATH, mFilePath);
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
                }).show();
                break;
        }
    }

    private void callBottomSheet() {
        new BottomSheetView(this, this);
    }

    @Override
    public void cameraClicked() {
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.CAMERA, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this)) {
            takePhoto();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(mBinder.createProfile1IvProfileIcon, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity1.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA
                                );
                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(CreateProfileActivity1.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void gallaryClicked() {
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) && PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
            getImageFromGallery();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(mBinder.createProfile1IvProfileIcon, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.accept), new View.OnClickListener() {
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

        }

    }
}



package com.appster.dentamatch.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.CameraUtil;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.widget.BottomSheetView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by virender on 02/01/17.
 */
public class CreateProfileActivity2 extends BaseActivity implements View.OnClickListener, ImageSelectedListener {
    private ImageView ivProfile, ivUpload;
    private ImageSelectedListener imageSelectedListener;
    private ProgressBar mProgressBar;
    private Button btnNext;
    private String mFilePath;
    private byte mSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile2);
        initViews();
    }

    private void initViews() {
        ivProfile = (ImageView) findViewById(R.id.create_profile_iv_profile_icon);
        ivUpload = (ImageView) findViewById(R.id.create_profile_iv_upoload_icon);
        btnNext = (Button) findViewById(R.id.create_profile2_btn_next);
        mProgressBar = (ProgressBar) findViewById(R.id.create_profile_progress_bar);
        ivUpload.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        mProgressBar.setProgress(65);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_profile_iv_upoload_icon:
                mSelectedImage = 1;
                callBottomSheet();
                break;
            case R.id.create_profile_iv_profile_icon:
//                mSelectedImage = 0;
//                callBottomSheet();
                break;
            case R.id.create_profile2_btn_next:
                startActivity(new Intent(this, WorkExperienceActivity.class));
                break;
        }
    }

    private void callBottomSheet() {
        BottomSheetView bottomSheetView = new BottomSheetView(this, this);
    }

    @Override
    public void cameraClicked() {
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.CAMERA, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this)) {
            takePhoto();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(ivProfile, getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity2.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);

                            }
                        }).show();
            } else {
                PermissionUtils.requestPermission(CreateProfileActivity2.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void gallaryClicked() {
        if (PermissionUtils.checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this) && PermissionUtils.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
            getImageFromGallery();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(ivProfile, this.getResources().getString(R.string.text_camera_permision),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Accept", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.requestPermission(CreateProfileActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);

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
                if (mSelectedImage == 0) {
                    Picasso.with(CreateProfileActivity2.this).load(new File(mFilePath)).centerCrop().resize(102, 102).placeholder(R.drawable.profile_pic_placeholder).into(ivProfile);
                } else {
                    Picasso.with(CreateProfileActivity2.this).load(new File(mFilePath)).centerCrop().resize(142, 142).placeholder(R.drawable.ic_upload).into(ivUpload);
                }
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

    @Override
    public String getActivityName() {
        return null;
    }
}

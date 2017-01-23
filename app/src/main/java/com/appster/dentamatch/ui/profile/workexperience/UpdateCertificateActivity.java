package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityUpdateCertificateBinding;
import com.appster.dentamatch.databinding.LayoutCertificatesCellBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.certificates.CertificateRequest;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.AboutMeActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by virender on 20/01/17.
 */
public class UpdateCertificateActivity extends BaseActivity implements View.OnClickListener {
    private ActivityUpdateCertificateBinding mBinder;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.layout_certificates_cell);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarUpdateCertificate.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
        mBinder.tvUploadPhoto.setOnClickListener(this);
        mBinder.tvValidityDatePicker.setOnClickListener(this);
        mBinder.toolbarUpdateCertificate.ivToolBarLeft.setOnClickListener(this);
        mBinder.tvCertificatesName.setText("");
//        if (!TextUtils.isEmpty(certificateList.get(i).getImage())) {
//            Picasso.with(getApplicationContext()).load(certificateList.get(i).getImage()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.ic_upload).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.ivCertificateUpoloadIcon);
//
//        }
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

    private void postCertificateData(CertificateRequest certificateRequest) {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveCertificate(certificateRequest).enqueue(new BaseCallback<BaseResponse>(UpdateCertificateActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    startActivity(new Intent(UpdateCertificateActivity.this, AboutMeActivity.class));

                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}

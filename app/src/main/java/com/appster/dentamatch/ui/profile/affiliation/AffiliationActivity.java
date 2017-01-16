package com.appster.dentamatch.ui.profile.affiliation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityAffiliationBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.auth.AffiliationResponse;
import com.appster.dentamatch.network.response.auth.CertificateResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.CertificateActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

/**
 * Created by virender on 13/01/17.
 */
public class AffiliationActivity extends BaseActivity implements OnClickListener {
    private ActivityAffiliationBinding mBinder;
    private LinearLayoutManager mLayoutManager;
    private AffiliationAdapter affiliationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_affiliation);
        initViews();
        getAffiliation();
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void initViews() {
        mBinder.toolbarAffiliation.tvToolbarGeneralLeft.setText(getString(R.string.header_affiliation));
        mBinder.includeProfileHeader.tvTitle.setText(getString(R.string.title_affiliation));
        mLayoutManager = new LinearLayoutManager(this);

        mBinder.recyclerAffiliation.setLayoutManager(mLayoutManager);
        mBinder.recyclerAffiliation.addItemDecoration(new SimpleDividerItemDecoration(this));
        affiliationAdapter = new AffiliationAdapter(this);
        mBinder.recyclerAffiliation.setAdapter(affiliationAdapter);
//        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
//            Picasso.with(getApplicationContext()).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(mBinder.includeProfileHeader.ivProfileIcon);
//
//        }
//        mBinder.includeProfileHeader.progressBar.setProgress(80);
        mBinder.toolbarAffiliation.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                finish();
                break;
            case R.id.btn_next:
                startActivity(new Intent(this, CertificateActivity.class));
                break;
        }
    }

    private void getAffiliation() {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getAffiliationList().enqueue(new BaseCallback<AffiliationResponse>(AffiliationActivity.this) {
            @Override
            public void onSuccess(AffiliationResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {

                    affiliationAdapter.addList(response.getAffiliationResponseData().getAffiliationList());
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<AffiliationResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }
}

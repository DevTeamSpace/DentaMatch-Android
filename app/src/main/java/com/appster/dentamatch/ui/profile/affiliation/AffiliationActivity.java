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
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.affiliation.AffiliationPostRequest;
import com.appster.dentamatch.network.request.affiliation.OtherAffiliationRequest;
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.CertificateActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 13/01/17.
 */
public class AffiliationActivity extends BaseActivity implements OnClickListener {
    private ActivityAffiliationBinding mBinder;
    private AffiliationAdapter affiliationAdapter;
    private boolean isFromEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_affiliation);

        if (getIntent() != null) {
            isFromEditProfile = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }

        initViews();
        getAffiliation();
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void initViews() {
        mBinder.toolbarAffiliation.tvToolbarGeneralLeft.setText(getString(R.string.header_affiliation));

        if (isFromEditProfile) {
            mBinder.btnNext.setText(getString(R.string.save_label));
            mBinder.toolbarAffiliation.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile).toUpperCase());
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mBinder.recyclerAffiliation.setLayoutManager(mLayoutManager);
        affiliationAdapter = new AffiliationAdapter(this, isFromEditProfile);
        mBinder.recyclerAffiliation.setAdapter(affiliationAdapter);
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
                hideKeyboard();

                if (checkValidation()) {
                    postAffiliationData(preParePostAffiliationRequest());
                }
                break;

            default:
                break;
        }
    }

    private boolean checkValidation() {
        boolean isSelected = false;
        for (int i = 0; i < affiliationAdapter.getList().size(); i++) {
            if (affiliationAdapter.getList().get(i).getJobSeekerAffiliationStatus() == 1) {
                isSelected = true;
            }

            if (affiliationAdapter.getList().get(i).getAffiliationName().equalsIgnoreCase(Constants.OTHERS) && affiliationAdapter.getList().get(i).getJobSeekerAffiliationStatus() == 1) {
                if (TextUtils.isEmpty(affiliationAdapter.getList().get(i).getOtherAffiliation())) {
                    Utils.showToast(AffiliationActivity.this, getString(R.string.blank_other_alert));
                    return false;

                } else {

                    if (TextUtils.isEmpty(affiliationAdapter.getList().get(i).getOtherAffiliation())) {
                        Utils.showToast(AffiliationActivity.this, getString(R.string.blank_other_alert));
                        return false;
                    } else {
                        affiliationAdapter.getList().get(i).setOtherAffiliation(affiliationAdapter.getList().get(i).getOtherAffiliation());
                    }
                }
            }
        }
        if (!isSelected) {
            Utils.showToast(AffiliationActivity.this, getString(R.string.blank_affiliation_selection));
            return false;
        }
        return true;
    }

    private void getAffiliation() {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getAffiliationList().enqueue(new BaseCallback<AffiliationResponse>(AffiliationActivity.this) {
            @Override
            public void onSuccess(AffiliationResponse response) {
                if (response.getStatus() == 1) {
                    affiliationAdapter.addList(response.getAffiliationResponseData().getAffiliationList());
                    mBinder.btnNext.setVisibility(View.VISIBLE);
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    mBinder.btnNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(Call<AffiliationResponse> call, BaseResponse baseResponse) {
                mBinder.btnNext.setVisibility(View.GONE);
            }
        });

    }

    private AffiliationPostRequest preParePostAffiliationRequest() {
        AffiliationPostRequest postRequest = new AffiliationPostRequest();
        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<OtherAffiliationRequest> otherList = new ArrayList<>();

        for (int i = 0; i < affiliationAdapter.getList().size(); i++) {

            if (!affiliationAdapter.getList().get(i).getAffiliationName().equalsIgnoreCase(Constants.OTHERS) &&
                    affiliationAdapter.getList().get(i).getJobSeekerAffiliationStatus() == 1) {
                idList.add(affiliationAdapter.getList().get(i).getAffiliationId());

            } else {
                if (affiliationAdapter.getList().get(i).getJobSeekerAffiliationStatus() == 1) {
                    OtherAffiliationRequest otherAffiliationRequest = new OtherAffiliationRequest();
                    otherAffiliationRequest.setAffiliationId(affiliationAdapter.getList().get(i).getAffiliationId());
                    otherAffiliationRequest.setOtherAffiliation(affiliationAdapter.getList().get(i).getOtherAffiliation());
                    otherList.add(otherAffiliationRequest);
                }
            }

        }

        postRequest.setIdList(idList);
        postRequest.setOtherAffiliationList(otherList);

        return postRequest;
    }

    private void postAffiliationData(AffiliationPostRequest affiliationPostRequest) {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveAffiliation(affiliationPostRequest).enqueue(new BaseCallback<BaseResponse>(AffiliationActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    if (getIntent() != null && getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false)) {
                        EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                        finish();
                    } else {
                        startActivity(new Intent(AffiliationActivity.this, CertificateActivity.class));
                    }
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });
    }
}

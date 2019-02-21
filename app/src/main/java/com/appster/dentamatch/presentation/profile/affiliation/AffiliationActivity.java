/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile.affiliation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.databinding.ActivityAffiliationBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse;
import com.appster.dentamatch.presentation.profile.CertificateActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
public class AffiliationActivity extends BaseLoadingActivity<AffiliationViewModel>
        implements OnClickListener {

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

        viewModel.getAffiliation().observe(this, this::onSuccessAffiliation);
        viewModel.getFailedRequestAffiliation().observe(this, e -> mBinder.btnNext.setVisibility(View.GONE));
        viewModel.getSaveAffiliations().observe(this, this::onSuccessSaveAffiliations);
    }

    private void onSuccessSaveAffiliations(@Nullable BaseResponse response) {
        if (response != null) {
            if (getIntent() != null && getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false)) {
                EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                finish();
            } else {
                startActivity(new Intent(AffiliationActivity.this, CertificateActivity.class));
            }
        }
    }

    private void onSuccessAffiliation(@Nullable AffiliationResponse response) {
        if (response != null) {
            affiliationAdapter.addList(response.getAffiliationResponseData().getAffiliationList());
            mBinder.btnNext.setVisibility(View.VISIBLE);
        }
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
                    postAffiliationData();
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
        viewModel.requestAffiliationList();
    }

    private void postAffiliationData() {
        viewModel.saveAffiliations(affiliationAdapter.getList());
    }
}

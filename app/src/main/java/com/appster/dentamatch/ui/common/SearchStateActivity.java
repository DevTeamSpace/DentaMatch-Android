/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.common;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.StateAdapter;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.databinding.ActivityStateBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.profile.StateList;
import com.appster.dentamatch.network.response.profile.StateResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by atul on 13/11/18.
 * To show and search list of states.
 */
public class SearchStateActivity extends BaseActivity implements View.OnClickListener, StateAdapter.ISettingCallback {

    private int mPrevSelPos = -1;
    private StateAdapter mStateAdapter;
    private ArrayList<StateList> mStateLists;
    private ActivityStateBinding mStateBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStateBinding = DataBindingUtil.setContentView(this, R.layout.activity_state);
        mStateBinding.toolbarState.txvToolbarGeneralCenter.setText(R.string.ttl_state);
        mStateBinding.toolbarState.ivToolBarLeft.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStateList();
    }

    private void initView() {
        mStateBinding.textSearchState.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        mStateBinding.textSearchState.setTextColor(ContextCompat.getColor(this, R.color.white));
        mStateBinding.rvStateList.setHasFixedSize(true);
        mStateBinding.rvStateList.setLayoutManager(new LinearLayoutManager(this));
        mStateBinding.rvStateList.addItemDecoration(new SimpleDividerItemDecoration(this));

        mStateAdapter = new StateAdapter(mStateLists, this);
        mStateBinding.rvStateList.setAdapter(mStateAdapter);
    }

    private void getStateList() {
        mStateLists = getIntent().getParcelableArrayListExtra(Constants.BundleKey.STATE);
        if (mStateLists != null && !mStateLists.isEmpty()) {
            mPrevSelPos = getIntent().getIntExtra(Constants.BundleKey.PREV_SEL_STATE, -1);
            initView();
            return;
        }
        showProgressBar(getString(R.string.please_wait));
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getStateList().enqueue(new BaseCallback<StateResponse>(this) {
            @Override
            public void onSuccess(StateResponse response) {
                if (response.getStatus() == 1) {
                    mStateLists = response.getResult().getStateList();
                    initView();
                } else {
                    showToast(response.getMessage());
                }
            }

            @Override
            public void onFail(Call<StateResponse> call, BaseResponse baseResponse) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public void onOptionSelected(String stateName) {
        if (mPrevSelPos != -1)
            mStateLists.get(mPrevSelPos).setSelected(false);
        for (int index = 0; index < mStateLists.size(); index++) {
            if (mStateLists.get(index).getStateName().equals(stateName)) {
                mStateLists.get(index).setSelected(true);
                mPrevSelPos = index;
            }
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.BundleKey.STATE, mStateLists);
        intent.putExtra(Constants.BundleKey.SEL_STATE, mStateLists.get(mPrevSelPos).getStateName());
        intent.putExtra(Constants.BundleKey.PREV_SEL_STATE, mPrevSelPos);
        setResult(RESULT_OK, intent);
        hideKeyboard();
        finish();
    }

    void filter(String text) {
        ArrayList<StateList> temp = new ArrayList<>();
        for (StateList d : mStateLists) {
            if (d.getStateName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        mStateAdapter.updateList(temp);
    }

    @Override
    public String getActivityName() {
        return getClass().getSimpleName();
    }

}

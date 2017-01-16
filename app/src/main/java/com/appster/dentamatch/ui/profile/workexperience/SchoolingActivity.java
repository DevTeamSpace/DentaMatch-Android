package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.SkillsAdapter;
import com.appster.dentamatch.databinding.ActivitySchoolingBinding;
import com.appster.dentamatch.databinding.ActivitySkillsBinding;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.skills.SkillsResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import java.util.List;

import retrofit2.Call;

/**
 * Created by ram on 15/01/17.
 */
public class SchoolingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Schooling";
    private ActivitySchoolingBinding mBinder;
    private Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_schooling);
        initViews();

        getSkillsListApi();
    }

    private void initViews() {
        btnNext = mBinder.btnNext;
        mBinder.toolbarSkills.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSkills.txvToolbarGeneralCenter.setText(getString(R.string.header_schooling_exp));
        btnNext.setOnClickListener(this);

    }

    @Override
    public String getActivityName() {
        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();

                break;
            case R.id.btn_next:
//                getSkillsListApi();
        }
    }

    private void getSkillsListApi() {
        LogUtils.LOGD(TAG, "getSkillsListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getSkillsList().enqueue(new BaseCallback<SkillsResponse>(SchoolingActivity.this) {
            @Override
            public void onSuccess(SkillsResponse response) {
                if (response.getStatus() == 1) {
//                    setAdapter(response.getSkillsResponseData().getSkillsList());
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<SkillsResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "getSkillsListApi onFail...");

            }
        });
    }
}

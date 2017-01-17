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
import com.appster.dentamatch.adapters.SchoolsAdapter;
import com.appster.dentamatch.adapters.SkillsAdapter;
import com.appster.dentamatch.databinding.ActivitySchoolingBinding;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.schools.SchoolingResponse;
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
    private SchoolsAdapter mSchoolsAdapter;
    private Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_schooling);
        initViews();

        getSchoolListApi();
    }

    private void initViews() {
        btnNext = mBinder.btnNext;
        mBinder.toolbarSchooling.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSchooling.tvToolbarGeneralLeft.setText(getString(R.string.header_schooling_exp).toUpperCase());
//        mBinder.layoutProfileHeader.tvTitle.setText(getString(R.string.header_schooling_exp));
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
//                getSchoolListApi();
        }
    }

    private void setAdapter(List<SchoolType> schoolTypeList) {
        mSchoolsAdapter = new SchoolsAdapter(schoolTypeList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSchools.setLayoutManager(layoutManager);
        mBinder.recyclerSchools.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSchools.setAdapter(mSchoolsAdapter);
        mSchoolsAdapter.notifyDataSetChanged();
    }

    private void getSchoolListApi() {
        LogUtils.LOGD(TAG, "getSchoolListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getSchoolList().enqueue(new BaseCallback<SchoolingResponse>(this) {
            @Override
            public void onSuccess(SchoolingResponse response) {
                if (response.getStatus() == 1) {
                    setAdapter(response.getSchoolingResponseData().getSchoolTypeList());
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<SchoolingResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void addSchoolListApi() {
        LogUtils.LOGD(TAG, "addSchoolListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getSchoolList().enqueue(new BaseCallback<SchoolingResponse>(this) {
            @Override
            public void onSuccess(SchoolingResponse response) {
                if (response.getStatus() == 1) {
                    setAdapter(response.getSchoolingResponseData().getSchoolTypeList());
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<SchoolingResponse> call, BaseResponse baseResponse) {

            }
        });
    }
}

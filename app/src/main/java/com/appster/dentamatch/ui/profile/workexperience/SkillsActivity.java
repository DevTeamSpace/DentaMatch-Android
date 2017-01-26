package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
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
import com.appster.dentamatch.databinding.ActivitySkillsBinding;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.certificates.UpdateCertificates;
import com.appster.dentamatch.network.request.skills.SkillsUpdateRequest;
import com.appster.dentamatch.network.response.skills.SkillsResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by ram on 12/01/17.
 */
public class SkillsActivity extends BaseActivity implements View.OnClickListener, SkillsAdapter.OnSkillClick, EditTextSelected {
    private static final String TAG = "Skills";
    private ActivitySkillsBinding mBinder;

    private SkillsAdapter mSkillsAdapter;
    private Button btnNext;

    private int mSkillPosition;
    private List<ParentSkill> mParentSkillList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_skills);
        initViews();
        getSkillsListApi();
    }

    private void initViews() {
        btnNext = mBinder.btnNext;
        mBinder.toolbarSkills.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSkills.tvToolbarGeneralLeft.setText(getString(R.string.header_skills_exp).toUpperCase());
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
                updateSkillsListApi(prepareSkillsUpdateRequest());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.LOGD(TAG, "onActivityResult data " + data);

        if (data != null) {
            ArrayList<SubSkill> subSkills = data.getParcelableArrayListExtra(Constants.BundleKey.SUB_SKILLS);
            LogUtils.LOGD(TAG, subSkills.size() + " items");

            mParentSkillList.get(mSkillPosition).setSubSkills(subSkills);

            mSkillsAdapter.notifyDataSetChanged();
        }
    }

    private void setAdapter(List<ParentSkill> skillArrayList) {
        mSkillsAdapter = new SkillsAdapter(skillArrayList, this, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSkills.setLayoutManager(layoutManager);
        mBinder.recyclerSkills.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSkills.setAdapter(mSkillsAdapter);
        mSkillsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(ArrayList<SubSkill> subSkillList, int position) {

        Intent intent = new Intent(SkillsActivity.this, SubSkillsActivity.class);
        intent.putExtra(Constants.BundleKey.SUB_SKILLS, subSkillList);

        startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_SKILLS);
        mSkillPosition = position;
    }

    private void getSkillsListApi() {
        LogUtils.LOGD(TAG, "getSkillsListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getSkillsList().enqueue(new BaseCallback<SkillsResponse>(this) {
            @Override
            public void onSuccess(SkillsResponse response) {
                if (response.getStatus() == 1) {
                    mParentSkillList = response.getSkillsResponseData().getSkillsList();
                    setAdapter(mParentSkillList);
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

    private SkillsUpdateRequest prepareSkillsUpdateRequest() {
        SkillsUpdateRequest skillsUpdateRequest = new SkillsUpdateRequest();

        ArrayList<Integer> skills = new ArrayList<Integer>();
        ArrayList<UpdateCertificates> othersList = new ArrayList<UpdateCertificates>();

        for (ParentSkill parentSkill : mParentSkillList) {
            if(parentSkill.getSkillName().equalsIgnoreCase(Constants.OTHERS)){
                UpdateCertificates obj = new UpdateCertificates();
                obj.setId(parentSkill.getId());
                obj.setValue(parentSkill.getOtherSkill());
                othersList.add(obj);
            }
            for (SubSkill subSkill : parentSkill.getSubSkills()) {
                if (subSkill.getIsSelected() == 1) {
                    if(!subSkill.getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                        skills.add(subSkill.getId());
                    }else {
                        UpdateCertificates obj = new UpdateCertificates();
                        obj.setId(subSkill.getId());
                        obj.setValue(subSkill.getOtherText());
                        othersList.add(obj);
                    }
                }
            }

            skillsUpdateRequest.setSkills(skills);
            skillsUpdateRequest.setOther(othersList);
        }

        return skillsUpdateRequest;
    }

    private void updateSkillsListApi(final SkillsUpdateRequest skillsUpdateRequest) {
        LogUtils.LOGD(TAG, "updateSkillsListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.updateSkills(skillsUpdateRequest).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    LogUtils.LOGD(TAG, "updateSkillsListApi success");
                    Utils.showToast(SkillsActivity.this, response.getMessage());
                    startActivity(new Intent(SkillsActivity.this, AffiliationActivity.class));
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "updateSkillsListApi onFail...");
            }
        });
    }

    @Override
    public void onEditTextSelected(int position) {
        mBinder.recyclerSkills.smoothScrollToPosition(position);
    }
}

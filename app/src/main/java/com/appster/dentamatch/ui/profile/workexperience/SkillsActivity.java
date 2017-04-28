package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.SkillsAdapter;
import com.appster.dentamatch.databinding.ActivitySkillsBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.interfaces.OnSkillClick;
import com.appster.dentamatch.model.ParentSkillModel;
import com.appster.dentamatch.model.SubSkillModel;
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
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by ram on 12/01/17.
 */
public class SkillsActivity extends BaseActivity implements View.OnClickListener, OnSkillClick, EditTextSelected {
    private static final String TAG = "Skills";
    private ActivitySkillsBinding mBinder;

    private SkillsAdapter mSkillsAdapter;

    private int mSkillPosition;
    private boolean isFromProfile;
    private List<ParentSkillModel> mParentSkillList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_skills);
        initViews();
        getSkillsListApi();
    }

    private void initViews() {
        Button btnNext = mBinder.btnNext;
        mBinder.toolbarSkills.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSkills.tvToolbarGeneralLeft.setText(getString(R.string.header_skills_exp).toUpperCase());

        if (getIntent() != null) {
            isFromProfile = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }

        if (isFromProfile) {
            mBinder.toolbarSkills.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile).toUpperCase());
            mBinder.btnNext.setText(getString(R.string.save_label));

        }

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

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            ArrayList<SubSkillModel> subSkills = data.getParcelableArrayListExtra(Constants.BundleKey.SUB_SKILLS);
            mParentSkillList.get(mSkillPosition).setSubSkills(subSkills);
            mSkillsAdapter.notifyDataSetChanged();
        }
    }

    private void setAdapter(List<ParentSkillModel> skillArrayList) {
        mSkillsAdapter = new SkillsAdapter(skillArrayList, this, this, this, isFromProfile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSkills.setLayoutManager(layoutManager);
        mBinder.recyclerSkills.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSkills.setAdapter(mSkillsAdapter);
        mSkillsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(ArrayList<SubSkillModel> subSkillList, int position) {

        Intent intent = new Intent(SkillsActivity.this, SubSkillsActivity.class);
        intent.putExtra(Constants.BundleKey.SUB_SKILLS, subSkillList);

        startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_SKILLS);
        mSkillPosition = position;
    }

    private void getSkillsListApi() {
        processToShowDialog();

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getSkillsList().enqueue(new BaseCallback<SkillsResponse>(this) {
            @Override
            public void onSuccess(SkillsResponse response) {

                if (response.getStatus() == 1) {
                    mParentSkillList = response.getSkillsResponseData().getSkillsList();
                    setAdapter(mParentSkillList);
                    mBinder.btnNext.setVisibility(View.VISIBLE);

                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    mBinder.btnNext.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFail(Call<SkillsResponse> call, BaseResponse baseResponse) {
                mBinder.btnNext.setVisibility(View.GONE);

            }
        });
    }

    private SkillsUpdateRequest prepareSkillsUpdateRequest() {
        SkillsUpdateRequest skillsUpdateRequest = new SkillsUpdateRequest();

        ArrayList<Integer> skills = new ArrayList<>();
        ArrayList<UpdateCertificates> othersList = new ArrayList<>();

        for (ParentSkillModel parentSkillModel : mParentSkillList) {

            if (parentSkillModel.getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                    if (!TextUtils.isEmpty(parentSkillModel.getOtherSkill())) {
                        UpdateCertificates obj = new UpdateCertificates();
                        obj.setId(parentSkillModel.getId());
                        obj.setValue(parentSkillModel.getOtherSkill().trim());
                        othersList.add(obj);
                    }
            }

            for (SubSkillModel subSkillModel : parentSkillModel.getSubSkills()) {

                if (subSkillModel.getIsSelected() == 1) {

                    if (!subSkillModel.getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                        skills.add(subSkillModel.getId());
                    } else {
                        UpdateCertificates obj = new UpdateCertificates();
                        obj.setId(subSkillModel.getId());

                        if (!TextUtils.isEmpty(subSkillModel.getOtherText())) {
                            obj.setValue(subSkillModel.getOtherText().trim());
                        }

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
        processToShowDialog();

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.updateSkills(skillsUpdateRequest).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    Utils.showToast(SkillsActivity.this, response.getMessage());

                    if (isFromProfile) {
                        EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                        finish();
                    } else {
                        startActivity(new Intent(SkillsActivity.this, AffiliationActivity.class));
                    }

                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });
    }

    @Override
    public void onEditTextSelected(int position) {
        mBinder.recyclerSkills.smoothScrollToPosition(position);
    }
}

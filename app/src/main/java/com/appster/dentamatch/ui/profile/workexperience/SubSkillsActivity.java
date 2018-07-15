/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

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

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.SubSkillsAdapter;
import com.appster.dentamatch.databinding.ActivitySubSkillsBinding;
import com.appster.dentamatch.model.SubSkillModel;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 * To inject activity reference.
 */
public class SubSkillsActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySubSkillsBinding mBinder;

    private ArrayList<SubSkillModel> subSkills;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_sub_skills);
        initViews();
        overridePendingTransition(R.anim.pull_in, R.anim.hold_still);

       // SubSkillModel sSkil= new SubSkillModel();
       // sSkil.setSkillName("Other");
       // subSkills.add(sSkil);

        setAdapter(subSkills);
    }

    @Override
    public void onBackPressed() {
        if (validate()) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_SUB_SKILLS, subSkills);

            setResult(Constants.REQUEST_CODE.REQUEST_CODE_SKILLS, intent);
            finish();
        }
    }

    private boolean validate() {
        hideKeyboard();
        int position = 0;

        for(int i = 0 ; i < subSkills.size(); i++){
            if(subSkills.get(i).getSkillName().equalsIgnoreCase("Other")){
                position = i;
                break;
            }
        }

        if(subSkills!=null && subSkills.size()>0) {
            boolean checked = subSkills.get(position).getIsSelected() == 1;
            String skillName = subSkills.get(position).getSkillName();
            boolean otherTextEmpty = TextUtils.isEmpty(subSkills.get(position).getOtherText()) || TextUtils.isEmpty(subSkills.get(position).getOtherText().trim());

            if (checked && skillName.equalsIgnoreCase(Constants.OTHERS) && otherTextEmpty) {
                Utils.showToastLong(this, getString(R.string.blank_other_alert));
                return false;
            }
        }
        return true;
    }

    private void initViews() {

        if (getIntent() != null) {
            subSkills = getIntent().getParcelableArrayListExtra(Constants.BundleKey.SUB_SKILLS);
        }

        mBinder.ivLeft.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_left:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold_still, R.anim.pull_out);
    }

    private void setAdapter(List<SubSkillModel> skillArrayList) {
        if (skillArrayList == null) return;

        SubSkillsAdapter mSkillsAdapter = new SubSkillsAdapter(skillArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSkills.setLayoutManager(layoutManager);
        mBinder.recyclerSkills.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSkills.setAdapter(mSkillsAdapter);
        mSkillsAdapter.notifyDataSetChanged();

        mBinder.recyclerSkills.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    // Recycle view scrolling up...
                    hideKeyboard();

                }
            }
        });
    }
}

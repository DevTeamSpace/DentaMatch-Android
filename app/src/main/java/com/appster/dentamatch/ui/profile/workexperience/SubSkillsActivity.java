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
import com.appster.dentamatch.adapters.SubSkillsAdapter;
import com.appster.dentamatch.databinding.ActivitySubSkillsBinding;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 */
public class SubSkillsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SubSkills";
    private ActivitySubSkillsBinding mBinder;

    private SubSkillsAdapter mSkillsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_sub_skills);
        initViews();

        Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_SUB_SKILLS);
        ArrayList<SubSkill> subSkills = null;


        if (bundle != null) {
            subSkills = bundle.getParcelableArrayList(Constants.BundleKey.SUB_SKILLS);
        }

        setAdapter(subSkills);
    }

    private void initViews() {
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.btn_next:
////                getSkillsListApi();
        }
    }

    private void setAdapter(List<SubSkill> skillArrayList) {
        if (skillArrayList == null) return;

        mSkillsAdapter = new SubSkillsAdapter(skillArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSkills.setLayoutManager(layoutManager);
        mBinder.recyclerSkills.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSkills.setAdapter(mSkillsAdapter);
        mSkillsAdapter.notifyDataSetChanged();
    }
}

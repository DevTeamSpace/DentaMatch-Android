package com.appster.dentamatch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExpListBinding;
import com.appster.dentamatch.ui.common.BaseActivity;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExpListActivity extends BaseActivity {
    private ActivityWorkExpListBinding mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_exp_list);
        initViews();
        inflateExpList();
    }

    private void initViews() {
        mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void inflateExpList() {
        mBinder.layoutExpListInflater.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 3; i++) {
            final View refrenceView = getLayoutInflater().inflate(R.layout.layout_work_exp_header_item, mBinder.layoutExpListInflater, false);
            refrenceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ViewAndEditWorkExperienceActivity.class));
                }
            });
            mBinder.layoutExpListInflater.addView(refrenceView);
        }
    }
}

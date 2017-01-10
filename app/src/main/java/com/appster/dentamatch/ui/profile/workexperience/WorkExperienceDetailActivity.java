package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceDetailBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.BottomSheetPicker;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExperienceDetailActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private com.appster.dentamatch.databinding.ActivityWorkExperinceDetailBinding mBinder;
    //AtzTextBinding mBinder;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experince_detail);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpDetail.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.btnNextDetailWorkExp.setOnClickListener(this);
        mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.toolbarWorkExpDetail.ivToolBarLeft.setOnClickListener(this);
        try {
            mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        setSpinnerData();
    }

    private void inflateRefrence() {
        mBinder.layoutRefrenceInfalter.removeAllViews();
        for (int i = 0; i < count; i++) {
            final View refrenceView = getLayoutInflater().inflate(R.layout.layout_reference, mBinder.layoutRefrenceInfalter, false);
            TextView tvRefrenceCount = (TextView) refrenceView.findViewById(R.id.tv_refrence_count);
            TextView tvRefrenceDlt = (TextView) refrenceView.findViewById(R.id.tv_refrence_delete);
            tvRefrenceCount.setText(getString(R.string.reference) + " " + (i+1));
            tvRefrenceCount.setVisibility(View.VISIBLE);
            tvRefrenceDlt.setVisibility(View.VISIBLE);
            tvRefrenceDlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count--;
                    inflateRefrence();

                }
            });
// number.setTag(i);
//            number.setText(Integer.toString(i));
            mBinder.layoutRefrenceInfalter.addView(refrenceView);
        }
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_add_more_reference:
            case R.id.tv_add_more_reference:
                count++;
                inflateRefrence();
                break;
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            case R.id.tv_add_more_experience:
                startActivity(new Intent(getApplicationContext(), WorkExpListActivity.class));

                break;
            case R.id.tv_experince_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.includeLayoutWorkExp.tvExperinceWorkExp.getText().toString())) {
                    String split[] = mBinder.includeLayoutWorkExp.tvExperinceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }
                new BottomSheetPicker(this, this, year, month);

                break;
            case R.id.btn_next_detail_work_exp:
                Utils.showToast(WorkExperienceDetailActivity.this, "New feature will introduce soon");
                break;
        }

    }

    private void setSpinnerData() {
        String title[] = new String[PreferenceUtil.getJobTitleList().size()];

        for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
            title[i] = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkExperienceDetailActivity.this,
                android.R.layout.simple_dropdown_item_1line, title);
//        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
//                findViewById(R.id.android_material_design_spinner);
        mBinder.includeLayoutWorkExp.spinnerJobTitleWorkExp.setPrompt(getString(R.string.lable_job_title));
        mBinder.includeLayoutWorkExp.spinnerJobTitleWorkExp.setAdapter(arrayAdapter);
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
        PreferenceUtil.setMonth(month);
        PreferenceUtil.setYear(year);
    }
}
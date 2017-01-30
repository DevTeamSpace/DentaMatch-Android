package com.appster.dentamatch.ui.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySearchJobBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.widget.CustomTextView;

import org.apmem.tools.layouts.FlowLayout;

/**
 * Created by virender on 26/01/17.
 */
public class SearchJobActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySearchJobBinding mBinder;
    private boolean isPartTime, isFullTime, isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_search_job);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarSearchJob.tvToolbarGeneralLeft.setText(getString(R.string.header_search_job));
        mBinder.toolbarSearchJob.ivToolBarLeft.setOnClickListener(this);
        mBinder.layoutFullTime.setOnClickListener(this);
        mBinder.layoutPartTime.setOnClickListener(this);
        mBinder.tvCurrentLocation.setOnClickListener(this);
        mBinder.tvJobTitle.setOnClickListener(this);
        mBinder.tvSaturday.setOnClickListener(this);
        mBinder.tvSunday.setOnClickListener(this);
        mBinder.tvMonday.setOnClickListener(this);
        mBinder.tvTuesday.setOnClickListener(this);
        mBinder.tvWednesday.setOnClickListener(this);
        mBinder.tvThursday.setOnClickListener(this);
        mBinder.tvFriday.setOnClickListener(this);
        setSelectedTitle();
    }

    private void setSelectedTitle() {
        if (PreferenceUtil.getSearchJobTitleList() != null && PreferenceUtil.getSearchJobTitleList().size() > 0) {

            for (int i = 0; i < PreferenceUtil.getSearchJobTitleList().size(); i++) {
                CustomTextView textView = new CustomTextView(this);
                FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 10, 10, 10);
                textView.setLayoutParams(lp);
                textView.setSingleLine(true);

                textView.setBackgroundResource(R.drawable.bg_edit_text);
                textView.setPadding(30, 10, 30, 10);
                textView.setText(PreferenceUtil.getSearchJobTitleList().get(i).getJobTitle());
                mBinder.flowLayoutJobTitle.addView(textView, lp);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_current_location:
                startActivity(new Intent(SearchJobActivity.this, PlacesMapActivity.class));
                break;
            case R.id.layout_part_time:
                if (isPartTime) {
                    isPartTime = false;
                    mBinder.ivPartTimeCheckBox.setBackgroundResource(R.drawable.ic_check_empty);
                } else {
                    isPartTime = true;
                    mBinder.ivPartTimeCheckBox.setBackgroundResource(R.drawable.ic_check_selected);
                }
                break;

            case R.id.layout_full_time:
                if (isFullTime) {
                    isFullTime = false;
                    mBinder.ivFullTimeCheckBox.setBackgroundResource(R.drawable.ic_check_empty);
                } else {
                    isFullTime = true;
                    mBinder.ivFullTimeCheckBox.setBackgroundResource(R.drawable.ic_check_selected);
                }
                break;

            case R.id.tv_job_title:
                startActivity(new Intent(getApplicationContext(), SelectJobTitleActivity.class));
                break;
            case R.id.tv_sunday:
                if (isSunday) {
                    isSunday = false;
                    mBinder.tvSunday.setBackground(null);
                } else {
                    isSunday = true;

                    mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
            case R.id.tv_monday:
                if (isMonday) {
                    isMonday = false;
                    mBinder.tvMonday.setBackground(null);
                } else {
                    isMonday = true;
                    mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
            case R.id.tv_tuesday:
                if (isTuesday) {
                    isTuesday = false;
                    mBinder.tvTuesday.setBackground(null);
                } else {
                    isTuesday = true;
                    mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
            case R.id.tv_wednesday:
                if (isWednesday) {
                    isWednesday = false;
                    mBinder.tvWednesday.setBackground(null);
                } else {
                    isWednesday = true;
                    mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
            case R.id.tv_thursday:
                if (isThursday) {
                    isThursday = false;
                    mBinder.tvThursday.setBackground(null);
                } else {
                    isThursday = true;
                    mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
            case R.id.tv_friday:
                if (isFriday) {
                    isFriday = false;
                    mBinder.tvFriday.setBackground(null);
                } else {
                    isFriday = true;
                    mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
            case R.id.tv_saturday:
                if (isSaturday) {
                    isSaturday = false;
                    mBinder.tvSaturday.setBackground(null);
                } else {
                    isSaturday = true;
                    mBinder.tvSaturday.setBackgroundResource(R.drawable.shape_circular_text_view);

                }
                break;
        }

    }
}

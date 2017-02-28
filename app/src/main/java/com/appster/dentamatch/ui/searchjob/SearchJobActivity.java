package com.appster.dentamatch.ui.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySearchJobBinding;
import com.appster.dentamatch.model.JobTitleList;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by virender on 26/01/17.
 */
public class SearchJobActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivitySearchJobBinding mBinder;
    private String mSelectedLat, mSelectedLng;
    private ArrayList<Integer> mSelectedJobID;
    private ArrayList<String> mPartTimeDays;
    private ArrayList<JobTitleList> mChosenTitles;
    private String mSelectedZipCode;
    private boolean isFirstTime;
    private String mSelectedAddress;
    private boolean isPartTime, isFullTime, isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday;


    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_search_job);

        if(getIntent().hasExtra(Constants.EXTRA_IS_FIRST_TIME)){
            isFirstTime = getIntent().getBooleanExtra(Constants.EXTRA_IS_FIRST_TIME,false);
        }

        initViews();
        setUserSelectedData();
    }

    private void setUserSelectedData() {

        if( PreferenceUtil.getJobFilter() !=  null) {
            SearchJobRequest request = (SearchJobRequest) PreferenceUtil.getJobFilter();

            mSelectedJobID = request.getJobTitle();
            isFullTime = (request.getIsFulltime() == 1);
            isPartTime = (request.getIsParttime() == 1);

//            if(isPartTime){
//                mPartTimeDays = request.getParttimeDays();
//            }

            mSelectedZipCode = request.getZipCode();
            mSelectedLat = request.getLat();
            mSelectedLng = request.getLng();

            /**
             * Set views based on the user data obtained above.
             */
            if(isFullTime){
                mBinder.cbFullTimeCheckBox.setChecked(true);
            }

            if(isPartTime){
                mBinder.dayLayout.setVisibility(View.VISIBLE);
                mBinder.cbPartTimeCheckBox.setChecked(true);

                for (String days : request.getParttimeDays()){
                    switch(days){
                        case "Sunday":
//                            mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
                           onClick(mBinder.tvSunday);
                            break;

                        case "Monday":
//                            mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
                            onClick(mBinder.tvMonday);

                            break;

                        case "Tuesday":
//                            mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                            onClick(mBinder.tvTuesday);

                            break;

                        case "Wednesday":
//                            mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                            onClick(mBinder.tvWednesday);

                            break;

                        case "Thursday":
//                            mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
                            onClick(mBinder.tvThursday);

                            break;

                        case "Friday":
//                            mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
                            onClick(mBinder.tvFriday);

                            break;

                        case "Saturday":
//                            mBinder.tvSaturday.setBackgroundResource(R.drawable.shape_circular_text_view);
                            onClick(mBinder.tvSaturday);

                            break;

                        default: break;
                    }
                }
            }else{
                mBinder.dayLayout.setVisibility(View.GONE);
            }

            mSelectedAddress = request.getAddress();

            if(!TextUtils.isEmpty(mSelectedAddress )) {
                mBinder.tvFetchedLoation.setVisibility(View.VISIBLE);
                mBinder.tvFetchedLoation.setText(mSelectedAddress);
            }

            mChosenTitles = request.getSelectedJobTitles();

            if(mChosenTitles != null && mChosenTitles.size() > 0) {
                mBinder.flowLayoutJobTitle.setVisibility(View.VISIBLE);

                for (JobTitleList items : mChosenTitles) {
                    addTitleToLayout(items, false);
                }
            }


        }

    }


    private void initViews() {
        mSelectedJobID = new ArrayList<>();
        mPartTimeDays = new ArrayList<>();

        mBinder.toolbarSearchJob.tvToolbarGeneralLeft.setText(getString(R.string.header_search_job));
        mBinder.toolbarSearchJob.ivToolBarLeft.setOnClickListener(this);
        mBinder.cbFullTimeCheckBox.setOnCheckedChangeListener(this);
        mBinder.cbPartTimeCheckBox.setOnCheckedChangeListener(this);
        mBinder.tvCurrentLocation.setOnClickListener(this);
        mBinder.tvJobTitle.setOnClickListener(this);
        mBinder.tvSaturday.setOnClickListener(this);
        mBinder.tvSunday.setOnClickListener(this);
        mBinder.tvMonday.setOnClickListener(this);
        mBinder.tvTuesday.setOnClickListener(this);
        mBinder.tvWednesday.setOnClickListener(this);
        mBinder.tvThursday.setOnClickListener(this);
        mBinder.tvFriday.setOnClickListener(this);
        mBinder.btnJobSearch.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_current_location:
                Intent intent = new Intent(SearchJobActivity.this, PlacesMapActivity.class);
                intent.putExtra(Constants.EXTRA_LATITUDE, mSelectedLat);
                intent.putExtra(Constants.EXTRA_LONGITUDE, mSelectedLng);
                intent.putExtra(Constants.EXTRA_POSTAL_CODE,mSelectedZipCode);
                intent.putExtra(Constants.EXTRA_PLACE_NAME, mSelectedAddress);

                startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS);
                break;

            case R.id.tv_job_title:
                Intent jobTitleSelectionIntent = new Intent(getApplicationContext(), SelectJobTitleActivity.class);

                if (mChosenTitles != null) {
                    jobTitleSelectionIntent.putExtra(Constants.EXTRA_CHOSEN_JOB_TITLES, mChosenTitles);
                }

                startActivityForResult(jobTitleSelectionIntent, Constants.REQUEST_CODE.REQUEST_CODE_JOB_TITLE);
                break;

            case R.id.tv_sunday:
                if (isSunday) {
                    isSunday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_sunday));
                    mBinder.tvSunday.setBackground(null);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));
                } else {
                    mPartTimeDays.add(getString(R.string.txt_full_sunday));
                    isSunday = true;
                    mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));
                }
                break;

            case R.id.tv_monday:
                if (isMonday) {
                    isMonday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_monday));
                    mBinder.tvMonday.setBackground(null);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));

                } else {
                    isMonday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_monday));
                    mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));
                }
                break;

            case R.id.tv_tuesday:
                if (isTuesday) {
                    isTuesday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_tuesday));
                    mBinder.tvTuesday.setBackground(null);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));

                } else {
                    isTuesday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_tuesday));
                    mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));

                }
                break;

            case R.id.tv_wednesday:
                if (isWednesday) {
                    isWednesday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_wednesday));
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));
                    mBinder.tvWednesday.setBackground(null);
                } else {
                    isWednesday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_wednesday));
                    mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));

                }
                break;

            case R.id.tv_thursday:
                if (isThursday) {
                    isThursday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_thursday));
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));
                    mBinder.tvThursday.setBackground(null);
                } else {
                    isThursday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_thursday));
                    mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));

                }
                break;

            case R.id.tv_friday:
                if (isFriday) {
                    isFriday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_friday));
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));
                    mBinder.tvFriday.setBackground(null);
                } else {
                    isFriday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_friday));
                    mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));
                }
                break;

            case R.id.tv_saturday:
                if (isSaturday) {
                    isSaturday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_saturday));
                    mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.brownish_grey));
                    mBinder.tvSaturday.setBackground(null);
                } else {
                    isSaturday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_saturday));
                    mBinder.tvSaturday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.white_color));
                }
                break;

            case R.id.btn_job_search:
                if (isValidData()) {
                    saveAndProceed();
                }
                break;

            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.cb_part_time_check_box:
                if (isChecked) {
                    isPartTime = true;
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.black_color));
                    mBinder.dayLayout.setVisibility(View.VISIBLE);
                } else {
                    isPartTime = false;
                    mBinder.dayLayout.setVisibility(View.GONE);
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.grayish_two));
                }

                break;


            case R.id.cb_full_time_check_box:
                if (isChecked) {
                    isFullTime = true;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.black_color));
                } else {
                    isFullTime = false;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SearchJobActivity.this, R.color.grayish_two));
                }
                break;

            default:
                break;
        }

    }

    private void saveAndProceed() {
        SearchJobRequest request = new SearchJobRequest();

        if (isPartTime) {
            request.setIsParttime(1);
        } else {
            request.setIsParttime(0);
        }

        if (isFullTime) {
            request.setIsFulltime(1);
        } else {
            request.setIsFulltime(0);
        }

        request.setLat(mSelectedLat);
        request.setLng(mSelectedLng);
        request.setJobTitle(mSelectedJobID);
        request.setPage(1);
        request.setParttimeDays(mPartTimeDays);
        request.setZipCode(mSelectedZipCode);
        request.setAddress(mSelectedAddress);

        request.setSelectedJobTitles(mChosenTitles);
        /**
         * This value is set in order to redirect user from login or splash screen.
         */
        PreferenceUtil.setJobFilter(true);
        PreferenceUtil.saveJobFilter(request);
        /**
         * This value is used by job search result helper to see if the filter value has been changed
         * and it needs to request data from server again or not.
         */
        PreferenceUtil.setFilterChanged(true);
        startActivity(new Intent(this, HomeActivity.class)
//                .putExtra(Constants.EXTRA_SEARCH_JOB, true)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    private boolean isValidData() {
        if (mSelectedJobID.size() == 0) {
            showToast(getString(R.string.msg_empty_job_title));
            return false;

        } else if (!isFullTime && !isPartTime) {
            showToast(getString(R.string.msg_empty_job_type));
            return false;

        } else if (isPartTime && mPartTimeDays.size() == 0) {
            showToast(getString(R.string.msg_empty_part_days));
            return false;

        } else if (TextUtils.isEmpty(mBinder.tvFetchedLoation.getText())) {
            showToast(getString(R.string.msg_empty_location));
            return false;

        } else if (TextUtils.isEmpty(mSelectedZipCode)) {
            showToast(getString(R.string.msg_empty_zip_code));
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_JOB_TITLE) {

            if (data != null && data.hasExtra(Constants.EXTRA_CHOSEN_JOB_TITLES)) {
                mSelectedJobID.clear();
                mBinder.flowLayoutJobTitle.removeAllViews();
                ArrayList<JobTitleList> jobTitleList = data.getParcelableArrayListExtra(Constants.EXTRA_CHOSEN_JOB_TITLES);
                mChosenTitles = jobTitleList;
                mBinder.flowLayoutJobTitle.setVisibility(View.VISIBLE);

                for (JobTitleList item : jobTitleList) {
                    addTitleToLayout(item, true);
                }


            }

        } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS) {

            if (data != null && data.hasExtra(Constants.EXTRA_PLACE_NAME)) {
                mBinder.tvFetchedLoation.setVisibility(View.VISIBLE);
                mSelectedAddress = data.getStringExtra(Constants.EXTRA_PLACE_NAME)
                        .concat(" ")
                        .concat(data.getStringExtra(Constants.EXTRA_POSTAL_CODE));
                mBinder.tvFetchedLoation.setText(mSelectedAddress);
                mSelectedLat = data.getStringExtra(Constants.EXTRA_LATITUDE);
                mSelectedLng = data.getStringExtra(Constants.EXTRA_LONGITUDE);
                mSelectedZipCode = data.getStringExtra(Constants.EXTRA_POSTAL_CODE);
            }
        }
    }

    private void addTitleToLayout(JobTitleList jobTitleListItem, boolean shouldAdd) {
        String text = jobTitleListItem.getJobTitle();

        if(shouldAdd) {
            mSelectedJobID.add(jobTitleListItem.getId());
        }

        com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding =  DataBindingUtil.bind(LayoutInflater.from(mBinder.flowLayoutJobTitle.getContext())
                .inflate(R.layout.item_flow_child, mBinder.flowLayoutJobTitle, false));

        flowBinding.flowChild.setText(text);
        mBinder.flowLayoutJobTitle.addView(flowBinding.getRoot());

    }


}

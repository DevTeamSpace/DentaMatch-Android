package com.appster.dentamatch.ui.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySearchJobBinding;
import com.appster.dentamatch.eventbus.LocationEvent;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.model.SelectedJobTitleModel;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.SelectedPreferredJobLocationData;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by virender on 26/01/17.
 */
public class SearchJobActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivitySearchJobBinding mBinder;
    private String mSelectedLat, mSelectedLng;
    private ArrayList<Integer> mSelectedJobID;
    private ArrayList<String> mPartTimeDays;
    private ArrayList<JobTitleListModel> mChosenTitles;
    private ArrayList<SelectedJobTitleModel> mSelectedJobTitles;
    private String mSelectedZipCode;
    private boolean isFirstTime;
    private String mSelectedAddress;
    private String mSelectedCity;
    private String mSelectedCountry;
    private String mSelectedState;

    private ArrayList<Integer> mSelectedPrefJobLocIds;
    private ArrayList<PreferredJobLocationData> mChosenPrefJobLocations;
    private ArrayList<SelectedPreferredJobLocationData> mSelectedPrefJobLocations;




    private boolean isPartTime, isFullTime, isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday;


    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_search_job);
        initViews();
        setUserSelectedData();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * This method will be called when Event is posted.
     */
    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        hideProgressBar();
        Location location = locationEvent.getMessage();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Address address = Utils.getReverseGeoCode(this, latLng);
        setData(address);
    }


    private void setData(Address address) {
        if (address != null) {
            mSelectedAddress = convertAddressToString(address);
            mSelectedZipCode = address.getPostalCode() == null ? "" : address.getPostalCode();
            mSelectedLat = String.valueOf(address.getLatitude());
            mSelectedLng = String.valueOf(address.getLongitude());
            mSelectedCountry = address.getCountryName();

            if (address.getLocality() != null) {
                mSelectedCity = address.getLocality();
            } else {
                mSelectedCity = address.getSubLocality();
            }

            mSelectedState = address.getAdminArea();

            //mBinder.tvFetchedLocation.setVisibility(View.VISIBLE);
           // mBinder.tvFetchedLocation.setText(mSelectedAddress.concat(" ").concat(mSelectedZipCode));
        } else {
            mSelectedAddress = "";
            mSelectedZipCode = "";
            mSelectedLat = "";
            mSelectedLng = "";
            mSelectedCountry = "";
            mSelectedState = "";
            mSelectedCity = "";
        }

    }


    private String convertAddressToString(Address address) {
        if (address == null) return "";

        StringBuilder sb = new StringBuilder();

        if (address.getAddressLine(0) != null) {
            sb.append(address.getAddressLine(0));
        }

        if (address.getAddressLine(1) != null) {
            sb.append(", ");
            sb.append(address.getAddressLine(1));
        }

        if (address.getSubAdminArea() != null) {
            sb.append(", ");
            sb.append(address.getSubAdminArea());
        }

        if (address.getAdminArea() != null) {
            sb.append(", ");
            sb.append(address.getAdminArea());
        }

        return sb.toString();
    }

    private void setUserSelectedData() {
        try {

            if (PreferenceUtil.getJobFilter() != null) {
                SearchJobRequest request = (SearchJobRequest) PreferenceUtil.getJobFilter();

                if (request.getJobTitle() != null) {
                    mSelectedJobID.addAll(request.getJobTitle());
                }

                if (request.getPreferredJobLocationId() != null) {
                    mSelectedPrefJobLocIds.addAll(request.getPreferredJobLocationId());
                }



                isFullTime = (request.getIsFulltime() == 1);
                isPartTime = (request.getIsParttime() == 1);

                mSelectedZipCode = request.getZipCode();
                mSelectedLat = request.getLat();
                mSelectedLng = request.getLng();
                mSelectedCountry = request.getCountry();
                mSelectedCity = request.getCity();
                mSelectedState = request.getState();

                /*
                  Set views based on the user data obtained above.
                 */
                if (isFullTime) {
                    mBinder.cbFullTimeCheckBox.setChecked(true);
                }

                if (isPartTime) {
                    mBinder.dayLayout.setVisibility(View.VISIBLE);
                    mBinder.cbPartTimeCheckBox.setChecked(true);

                    for (String days : request.getParttimeDays()) {
                        switch (days) {
                            case "Sunday":
                                onClick(mBinder.tvSunday);
                                break;

                            case "Monday":
                                onClick(mBinder.tvMonday);

                                break;

                            case "Tuesday":
                                onClick(mBinder.tvTuesday);

                                break;

                            case "Wednesday":
                                onClick(mBinder.tvWednesday);

                                break;

                            case "Thursday":
                                onClick(mBinder.tvThursday);

                                break;

                            case "Friday":
                                onClick(mBinder.tvFriday);

                                break;

                            case "Saturday":
                                onClick(mBinder.tvSaturday);

                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    mBinder.dayLayout.setVisibility(View.GONE);
                }

                mSelectedAddress = request.getAddress();

               /* if (!TextUtils.isEmpty(mSelectedAddress)) {
                    mBinder.tvFetchedLocation.setVisibility(View.VISIBLE);
                    mBinder.tvFetchedLocation.setText(mSelectedAddress.concat(" ").concat(mSelectedZipCode));
                }
*/
                if (request.getSelectedJobTitles() != null) {
                    mSelectedJobTitles = request.getSelectedJobTitles();
                    for (SelectedJobTitleModel model : mSelectedJobTitles) {
                        JobTitleListModel jobModel = new JobTitleListModel();
                        jobModel.setSelected(true);
                        jobModel.setId(model.getId());
                        jobModel.setJobTitle(model.getJobTitle());

                        mChosenTitles.add(jobModel);
                    }
                }

                if (mChosenTitles != null && mChosenTitles.size() > 0) {
                    mBinder.flowLayoutJobTitle.setVisibility(View.VISIBLE);

                    for (JobTitleListModel items : mChosenTitles) {
                        addTitleToLayout(items, false);
                    }
                }


                LogUtils.LOGD("PreferredJobListCount>>", ""+PreferenceUtil.getPreferredJobList().size());

                if (request.getPreferredJobLocationId() != null) {
                    mSelectedPrefJobLocIds = request.getPreferredJobLocationId();

                    if(mSelectedPrefJobLocations.size()==0) {
                        for (PreferredJobLocationData sData : PreferenceUtil.getPreferredJobList()) {
                            if (mSelectedPrefJobLocIds != null && mSelectedPrefJobLocIds.size() > 0 && mSelectedPrefJobLocIds.contains(sData.getId())) {
                                SelectedPreferredJobLocationData selectedPreferredJobLocationData = new SelectedPreferredJobLocationData();
                                selectedPreferredJobLocationData.setPreferredLocationName(sData.getPreferredLocationName());
                                selectedPreferredJobLocationData.setId(sData.getId());
                                selectedPreferredJobLocationData.setIsActive(sData.getIsActive());

                                mSelectedPrefJobLocations.add(selectedPreferredJobLocationData);
                            }
                        }
                    }


                    for (SelectedPreferredJobLocationData model : mSelectedPrefJobLocations) {
                        PreferredJobLocationData jobModel = new PreferredJobLocationData();
                        jobModel.setSelected(true);
                        jobModel.setId(model.getId());
                        jobModel.setPreferredLocationName(model.getPreferredLocationName());

                        mChosenPrefJobLocations.add(jobModel);
                    }

                }

               if(mSelectedPrefJobLocIds!=null && mSelectedPrefJobLocIds.size()>0){

                   mBinder.flowTvPreferredJobLocation.setVisibility(View.VISIBLE);

                   for (PreferredJobLocationData items : PreferenceUtil.getPreferredJobList()) {

                       if(mSelectedPrefJobLocIds.contains(items.getId()))
                       addSelectedPreferredJobLocation(items, false);
                   }
               }



            }else{
                /*
                In case we have net connection show loader and fetch user current location address.
                 */
                if(Utils.isConnected(this)) {
                    processToShowDialog();
                    LocationUtils.addFragment(this);
                }
            }





        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void initViews() {
        mSelectedJobID = new ArrayList<>();
        mPartTimeDays = new ArrayList<>();
        mChosenTitles = new ArrayList<>();
        mSelectedJobTitles = new ArrayList<>();
        mSelectedPrefJobLocIds = new ArrayList<>();
        mChosenPrefJobLocations= new ArrayList<>();
        mSelectedPrefJobLocations= new ArrayList<>();

        mBinder.toolbarSearchJob.tvToolbarGeneralLeft.setText(getString(R.string.header_search_job));
        mBinder.toolbarSearchJob.ivToolBarLeft.setOnClickListener(this);
        mBinder.cbFullTimeCheckBox.setOnCheckedChangeListener(this);
        mBinder.cbPartTimeCheckBox.setOnCheckedChangeListener(this);
       // mBinder.tvCurrentLocation.setOnClickListener(this);
       // mBinder.tvFetchedLocation.setOnClickListener(this);
        mBinder.tvJobTitle.setOnClickListener(this);
        mBinder.tvSaturday.setOnClickListener(this);
        mBinder.tvSunday.setOnClickListener(this);
        mBinder.tvMonday.setOnClickListener(this);
        mBinder.tvTuesday.setOnClickListener(this);
        mBinder.tvWednesday.setOnClickListener(this);
        mBinder.tvThursday.setOnClickListener(this);
        mBinder.tvFriday.setOnClickListener(this);
        mBinder.btnJobSearch.setOnClickListener(this);
        mBinder.tvPreferredJobLocation.setOnClickListener(this);


        if(getIntent().hasExtra(Constants.EXTRA_IS_FIRST_TIME)){
            isFirstTime = getIntent().getBooleanExtra(Constants.EXTRA_IS_FIRST_TIME,false);
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {



           /* case R.id.tv_fetched_location:
            case R.id.tv_current_location:
                if(Utils.isConnected(SearchJobActivity.this)) {
                    Intent intent = new Intent(SearchJobActivity.this, PlacesMapActivity.class);

                    if (mSelectedLat != null && mSelectedLng != null) {
                        intent.putExtra(Constants.EXTRA_LATITUDE, mSelectedLat);
                        intent.putExtra(Constants.EXTRA_LONGITUDE, mSelectedLng);
                        intent.putExtra(Constants.EXTRA_POSTAL_CODE, mSelectedZipCode);
                        intent.putExtra(Constants.EXTRA_PLACE_NAME, mSelectedAddress);
                        intent.putExtra(Constants.EXTRA_COUNTRY_NAME, mSelectedCountry);
                        intent.putExtra(Constants.EXTRA_CITY_NAME, mSelectedCity);
                        intent.putExtra(Constants.EXTRA_STATE_NAME, mSelectedState);
                    }

                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS);
                }else{
                    showToast(getString(R.string.no_internet));
                }
                break;
*/
            case R.id.tv_job_title:
                if(Utils.isConnected(SearchJobActivity.this)) {
                    Intent jobTitleSelectionIntent = new Intent(getApplicationContext(), SelectJobTitleActivity.class);

                    if (mChosenTitles != null && mChosenTitles.size() > 0) {
                        jobTitleSelectionIntent.putExtra(Constants.EXTRA_CHOSEN_JOB_TITLES, mChosenTitles);
                    }

                    startActivityForResult(jobTitleSelectionIntent, Constants.REQUEST_CODE.REQUEST_CODE_JOB_TITLE);
                }else {
                    showToast(getString(R.string.no_internet));
                }
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


            case R.id.tv_preferred_job_location:
                if(Utils.isConnected(SearchJobActivity.this)) {
                    Intent preferedJobListIntent = new Intent(getApplicationContext(), SelectPreferredLocationActivity.class);

                    if (mChosenPrefJobLocations != null && mChosenPrefJobLocations.size() > 0) {
                        preferedJobListIntent.putExtra(Constants.EXTRA_CHOSEN_PREFERRED_JOB_LOCATION, mChosenPrefJobLocations);
                    }

                    startActivityForResult(preferedJobListIntent, Constants.REQUEST_CODE.REQUEST_CODE_PREFJOB_LOC);
                }else {
                    showToast(getString(R.string.no_internet));
                }
                break;



            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if(isFirstTime){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            finish();
        }
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
        request.setCity(mSelectedCity);
        request.setCountry(mSelectedCountry);
        request.setState(mSelectedState);


        request.setSelectedJobTitles(mSelectedJobTitles);

        request.setPreferredJobLocationId(mSelectedPrefJobLocIds);


        /*
          This value is set in order to redirect user from login or splash screen.
         */
        PreferenceUtil.setJobFilter(true);
        PreferenceUtil.saveJobFilter(request);

        /*
          This value is used by job search result helper to see if the filter value has been changed
          and it needs to request data from server again or not.
         */
        PreferenceUtil.setFilterChanged(true);
        startActivity(new Intent(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    private boolean isValidData() {
        if (mSelectedJobID.size() == 0 || mChosenTitles.size() == 0 || mSelectedJobTitles.size() == 0) {
            showToast(getString(R.string.msg_empty_job_title));
            return false;

        } else if (!isFullTime && !isPartTime) {
            showToast(getString(R.string.msg_empty_job_type));
            return false;

        } else if (isPartTime && mPartTimeDays.size() == 0) {
            showToast(getString(R.string.msg_empty_part_days));
            return false;

        } /*else if (TextUtils.isEmpty(mBinder.tvFetchedLocation.getText())) {
            showToast(getString(R.string.msg_empty_location));
            return false;

        }*//* else if (TextUtils.isEmpty(mSelectedZipCode)) {
            showToast(getString(R.string.msg_empty_zip_code));
            return false;

        }else if(TextUtils.isEmpty(mSelectedCountry)) {
            showToast(getString(R.string.msg_empty_country));
            return false;

        }else if (TextUtils.isEmpty(mSelectedCity)){
            showToast(getString(R.string.msg_empty_city));
            return false;

        }else if (TextUtils.isEmpty(mSelectedState)){
            showToast(getString(R.string.msg_empty_state));
            return false;

        }*/else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_JOB_TITLE) {

                if (data != null && data.hasExtra(Constants.EXTRA_CHOSEN_JOB_TITLES)) {
                    mSelectedJobID.clear();
                    mChosenTitles.clear();
                    mBinder.flowLayoutJobTitle.removeAllViews();
                    ArrayList<JobTitleListModel> jobTitleList = data.getParcelableArrayListExtra(Constants.EXTRA_CHOSEN_JOB_TITLES);
                    mChosenTitles = jobTitleList;

                    mBinder.flowLayoutJobTitle.setVisibility(View.VISIBLE);
                    mSelectedJobTitles.clear();

                    if (mChosenTitles.size() > 0) {
                        for (JobTitleListModel model : mChosenTitles) {
                            SelectedJobTitleModel jobModel = new SelectedJobTitleModel();
                            jobModel.setJobTitle(model.getJobTitle());
                            jobModel.setId(model.getId());
                            mSelectedJobTitles.add(jobModel);
                        }
                    }

                    for (JobTitleListModel item : jobTitleList) {
                        addTitleToLayout(item, true);
                    }


                }

            } else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS) {

               /* if (data != null && data.hasExtra(Constants.EXTRA_PLACE_NAME)) {
                    mBinder.tvFetchedLocation.setVisibility(View.VISIBLE);
                    mSelectedAddress = data.getStringExtra(Constants.EXTRA_PLACE_NAME);
                    mSelectedLat = data.getStringExtra(Constants.EXTRA_LATITUDE);
                    mSelectedLng = data.getStringExtra(Constants.EXTRA_LONGITUDE);
                    mSelectedZipCode = data.getStringExtra(Constants.EXTRA_POSTAL_CODE);
                    mSelectedCity = data.getStringExtra(Constants.EXTRA_CITY_NAME);
                    mSelectedState = data.getStringExtra(Constants.EXTRA_STATE_NAME);
                    mSelectedCountry = data.getStringExtra(Constants.EXTRA_COUNTRY_NAME);
                    mBinder.tvFetchedLocation.setText(mSelectedAddress.concat(" ").concat(mSelectedZipCode));

                }*/
            }
            else if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_PREFJOB_LOC) {


                if (data != null && data.hasExtra(Constants.EXTRA_CHOSEN_PREFERRED_JOB_LOCATION)) {
                    mSelectedPrefJobLocIds.clear();
                    mChosenPrefJobLocations.clear();
                    mBinder.flowTvPreferredJobLocation.removeAllViews();
                    ArrayList<PreferredJobLocationData> jobLocationData = data.getParcelableArrayListExtra(Constants.EXTRA_CHOSEN_PREFERRED_JOB_LOCATION);
                    mChosenPrefJobLocations = jobLocationData;

                    mBinder.flowTvPreferredJobLocation.setVisibility(View.VISIBLE);
                    mSelectedPrefJobLocations.clear();

                    if (mChosenPrefJobLocations.size() > 0) {
                        for (PreferredJobLocationData model : mChosenPrefJobLocations) {
                            SelectedPreferredJobLocationData jobModel = new SelectedPreferredJobLocationData();
                            jobModel.setPreferredLocationName(model.getPreferredLocationName());
                            jobModel.setId(model.getId());
                            mSelectedPrefJobLocations.add(jobModel);
                        }
                    }

                    for (PreferredJobLocationData item : jobLocationData) {
                        addSelectedPreferredJobLocation(item, true);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addTitleToLayout(JobTitleListModel jobTitleListItem, boolean shouldAdd) {
        String text = jobTitleListItem.getJobTitle();

        if(shouldAdd) {
            mSelectedJobID.add(jobTitleListItem.getId());
        }

        com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding =  DataBindingUtil.bind(LayoutInflater.from(mBinder.flowLayoutJobTitle.getContext())
                .inflate(R.layout.item_flow_child, mBinder.flowLayoutJobTitle, false));

        flowBinding.flowChild.setText(text);
        mBinder.flowLayoutJobTitle.addView(flowBinding.getRoot());

    }




    private void addSelectedPreferredJobLocation(PreferredJobLocationData preferredJobLocationData, boolean shouldAdd) {
        String text = preferredJobLocationData.getPreferredLocationName();

        if(shouldAdd) {
            mSelectedPrefJobLocIds.add(preferredJobLocationData.getId());
        }

        com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding =  DataBindingUtil.bind(LayoutInflater.from(mBinder.flowTvPreferredJobLocation.getContext())
                .inflate(R.layout.item_flow_child, mBinder.flowTvPreferredJobLocation, false));

        flowBinding.flowChild.setText(text);
        mBinder.flowTvPreferredJobLocation.addView(flowBinding.getRoot());

    }


}

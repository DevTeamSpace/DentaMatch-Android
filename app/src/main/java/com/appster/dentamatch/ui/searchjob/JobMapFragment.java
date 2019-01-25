/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.searchjob;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentJobsMapBinding;
import com.appster.dentamatch.eventbus.JobDataReceivedEvent;
import com.appster.dentamatch.eventbus.LocationEvent;
import com.appster.dentamatch.eventbus.SaveUnSaveEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.base.BaseFragment;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PermissionUtils;
import com.appster.dentamatch.util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

/**
 * To view job along-with the location on map
 */

public class JobMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnClickListener {
    private static final String TAG = LogUtils.makeLogTag(JobMapFragment.class);
    private final int MARKER_PADDING = 100;
    private int mSelectedJobID;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FragmentJobsMapBinding mMapBinding;
    private GoogleMap mGoogleMap;
    private double mCurrentLocLat;
    private double mCurrentLocLng;
    private ArrayList<LatLng> mMarkerPositionList;
    private Marker previousSelectedMarker;
    private ArrayList<SearchJobModel> mJobData;


    @Override
    public String getFragmentName() {
        return null;
    }

    public static JobMapFragment newInstance() {
        return new JobMapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDataUpdated(JobDataReceivedEvent event) {
        if (event != null) {

            if (mMarkerPositionList != null) {
                mMarkerPositionList.clear();
            }

            mJobData = event.getJobList();

            for (SearchJobModel model : mJobData) {
                if (mMarkerPositionList != null)
                    mMarkerPositionList.add(new LatLng(model.getLatitude(), model.getLongitude()));
            }

        }

        if (mMarkerPositionList != null)
            addMarker(mMarkerPositionList);
    }

    @Subscribe
    public void onJobSavedUnsaved(SaveUnSaveEvent event) {
        if (event != null) {

            for (SearchJobModel model1 : mJobData) {

                if (model1.getId() == event.getJobID()) {
                    model1.setIsSaved(event.getStatus());
                    SearchJobDataHelper.getInstance().notifyItemsChanged(model1);
                    if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        mMapBinding.infoWindowContent.cbJobSelection.setChecked(event.getStatus() == 1);
                        break;
                    }
                }
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mMapBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs_map, container, false);
        mMapBinding.mapView.onCreate(savedInstanceState);
        mMarkerPositionList = new ArrayList<>();
        mBottomSheetBehavior = BottomSheetBehavior.from(mMapBinding.jobMapInfoWindow);
        /*
          Disable user dragging of bottom sheet.
         */
        mBottomSheetBehavior.setHideable(false);
        mMapBinding.infoWindowContent.getRoot().setOnClickListener(this);
        mMapBinding.mapView.getMapAsync(this);
        return mMapBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(this);

        if (getActivity() != null && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            PermissionUtils.requestPermission(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS);

        } else {
            mGoogleMap.setMyLocationEnabled(true);
            LocationUtils.addFragment((AppCompatActivity) getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS) {

            if (grantResults.length > 0 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (getActivity() != null && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                    LocationUtils.addFragment((AppCompatActivity) getActivity());

                }
            }
        }
    }

    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        Location location = locationEvent.getMessage();
        mCurrentLocLat = location.getLatitude();
        mCurrentLocLng = location.getLongitude();

        /*
          the map is already loaded as we request current location onMapReady method.
          So we add markers in this method after fetching current location of user.
         */
        mMarkerPositionList.clear();

        /*
          Request Data helper class to provide job search data
         */
        SearchJobDataHelper.getInstance().requestData(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapBinding.mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapBinding.mapView.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapBinding.mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapBinding.mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapBinding.mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapBinding.mapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /*
          Animate the camera to marker and bring it to center.
         */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker.getPosition());
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MARKER_PADDING));

        if (previousSelectedMarker != null) {
            /*
              In case current and previous markers are same close the info window and deselect marker.
             */
            if (previousSelectedMarker.getId().equalsIgnoreCase(marker.getId())) {
                collapseInfoDialog();
                previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
                previousSelectedMarker = null;
            } else {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));
                previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
                previousSelectedMarker = marker;
                openInfoDialog(marker);
            }

        } else {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));
            previousSelectedMarker = marker;
            openInfoDialog(marker);
        }

        return false;
    }


    @Override
    public void onMapClick(LatLng latLng) {
        collapseInfoDialog();
        if (previousSelectedMarker != null) {
            previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
            previousSelectedMarker = null;
        }
    }

    private void openInfoDialog(Marker marker) {
        final SearchJobModel jobModel = (SearchJobModel) marker.getTag();
        /*
          In case a previous bottom sheet is remaining open then 1st the previous one gets hidden
          and then a new one appears.
         */
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            collapseInfoDialog();

            /*
              Allow the previous info window which is open to close , for that purpose a delay handler
              of 300 ms is added .
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateInfoWindowData(jobModel);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 300);
        } else {
            updateInfoWindowData(jobModel);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void updateInfoWindowData(SearchJobModel jobModel) {
        try {
            if (jobModel != null) {
                mSelectedJobID = jobModel.getId();
                mMapBinding.infoWindowContent.tvJobName.setText(jobModel.getJobTitleName());
                mMapBinding.infoWindowContent.cbJobSelection.setChecked(jobModel.getIsSaved() == 1);
                mMapBinding.infoWindowContent.cbJobSelection.setTag(jobModel);
                mMapBinding.infoWindowContent.cbJobSelection.setOnClickListener(this);

                if (jobModel.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                    mMapBinding.infoWindowContent.tvJobType.setText(getString(R.string.txt_part_time));
                    mMapBinding.infoWindowContent.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

                    ArrayList<String> partTimeDaysArray = new ArrayList<>();
                    if (jobModel.getIsMonday() == 1) {
                        partTimeDaysArray.add(getString(R.string.mon));
                    }

                    if (jobModel.getIsTuesday() == 1) {
                        partTimeDaysArray.add(getString(R.string.tue));
                    }

                    if (jobModel.getIsWednesday() == 1) {
                        partTimeDaysArray.add(getString(R.string.wed));
                    }

                    if (jobModel.getIsThursday() == 1) {
                        partTimeDaysArray.add(getString(R.string.thu));
                    }

                    if (jobModel.getIsFriday() == 1) {
                        partTimeDaysArray.add(getString(R.string.fri));
                    }

                    if (jobModel.getIsSaturday() == 1) {
                        partTimeDaysArray.add(getString(R.string.sat));
                    }

                    if (jobModel.getIsSunday() == 1) {
                        partTimeDaysArray.add(getString(R.string.sun));
                    }

                    String partTimeDays = TextUtils.join(", ", partTimeDaysArray);
                    mMapBinding.infoWindowContent.tvJobDate.setVisibility(View.VISIBLE);
                    mMapBinding.infoWindowContent.tvJobDate.setText(partTimeDays);

                    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, mMapBinding.infoWindowContent.tvJobName.getId());
                    params.addRule(RelativeLayout.END_OF, mMapBinding.infoWindowContent.tvJobType.getId());
                    params.addRule(RelativeLayout.START_OF, mMapBinding.infoWindowContent.tvJobDocDistance.getId());
                    params.setMargins(Utils.dpToPx(getActivity(), 12), 0, Utils.dpToPx(getActivity(), 10), 0);

                    mMapBinding.infoWindowContent.tvJobDate.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mMapBinding.infoWindowContent.tvJobDate.getLineCount() == 1) {
                                params.addRule(RelativeLayout.ALIGN_BASELINE, mMapBinding.infoWindowContent.tvJobType.getId());
                                mMapBinding.infoWindowContent.tvJobDate.setLayoutParams(params);
                            } else {
                                mMapBinding.infoWindowContent.tvJobDate.setLayoutParams(params);
                            }
                        }
                    }, 200);


                } else if (jobModel.getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                    mMapBinding.infoWindowContent.tvJobType.setBackgroundResource(R.drawable.job_type_background_full_time);
                    mMapBinding.infoWindowContent.tvJobDate.setVisibility(View.GONE);
                    mMapBinding.infoWindowContent.tvJobType.setText(getString(R.string.txt_full_time));

                } else if (jobModel.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                    mMapBinding.infoWindowContent.tvJobType.setBackgroundResource(R.drawable.job_type_background_temporary);
                    mMapBinding.infoWindowContent.tvJobDate.setVisibility(View.GONE);
                    mMapBinding.infoWindowContent.tvJobType.setText(getString(R.string.txt_temporary));
                }

                mMapBinding.infoWindowContent.tvJobDocAddress.setText(jobModel.getAddress());
                String endMessage = jobModel.getDays() > 1 ? getString(R.string.txt_days_ago) : getString(R.string.txt_day_ago);
                mMapBinding.infoWindowContent.tvJobDocTime.setText(String.valueOf(jobModel.getDays()).concat(" ").concat(endMessage));

                //mMapBinding.infoWindowContent.tvJobDocDistance.setText(String.format(Locale.getDefault(), "%.1f", jobModel.getDistance()).concat(getString(R.string.txt_miles)));
                mMapBinding.infoWindowContent.tvJobDocDistance.setText(String.format(Locale.getDefault(), "%.2f", jobModel.getPercentaSkillsMatch()).concat("%"));

                // mMapBinding.infoWindowContent.tvJobDocDistance.setText(String.format(Locale.getDefault(), "%.1f", jobModel.getDistance()).concat(getString(R.string.txt_miles)));
                mMapBinding.infoWindowContent.tvJobDocName.setText(jobModel.getOfficeName());
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    private void collapseInfoDialog() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private void addMarker(ArrayList<LatLng> markerList) {
        /*
          Lat lng Bounds of the entire icons on the map.
         */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        for (int i = 0; i < markerList.size(); i++) {
            MarkerOptions options = new MarkerOptions();
            options.position(markerList.get(i));
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
            Marker addedMarker = mGoogleMap.addMarker(options);

            /*
              Set the data model as a tag on the marker so it can be used when info window is
              displayed.
             */
            addedMarker.setTag(mJobData.get(i));
            builder.include(options.getPosition());
        }

        /*
          Include the current location also into the bounds so that the screen can be adjusted to a zoom
          level inclusive of both current location and other location markers.
         */
      /*  builder.include(new LatLng(mCurrentLocLat, mCurrentLocLng));*/
        LatLngBounds bounds = builder.build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MARKER_PADDING));

        //new code
        if (markerList.size() > 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (getActivity() != null && getActivity().getWindowManager() != null) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
            }
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(markerList.get(0).latitude, markerList.get(0).longitude), 10));
        }
    }

    private void saveUnSaveJob(int JobID, final int status, final SearchJobModel model) {
        SaveUnSaveRequest request = new SaveUnSaveRequest();
        request.setJobId(JobID);
        request.setStatus(status);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        if (getActivity() != null)
            ((BaseActivity) getActivity()).processToShowDialog();
        webServices.saveUnSaveJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(BaseResponse response) {
                ((BaseActivity) getActivity()).showToast(response.getMessage());

                if (response.getStatus() == 1) {
                    model.setIsSaved(status);
                    mMapBinding.infoWindowContent.cbJobSelection.setChecked(status == 1);
                    SearchJobDataHelper.getInstance().notifyItemsChanged(model);
                } else {

                    if (TextUtils.isEmpty(response.getMessage())) {
                        mMapBinding.infoWindowContent.cbJobSelection.setChecked(!(status == 1));
                        ((BaseActivity) getActivity()).showToast(response.getMessage());

                    }
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                mMapBinding.infoWindowContent.cbJobSelection.setChecked(!(status == 1));
            }
        });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.cb_job_selection:
                final SearchJobModel model = (SearchJobModel) v.getTag();
                final int status = (model.getIsSaved() == 1) ? 0 : 1;

                if (status == 0) {
                    Alert.createYesNoAlert(getActivity(), getString(R.string.txt_ok),
                            getString(R.string.txt_cancel),
                            getString(R.string.txt_alert_title),
                            getString(R.string.msg_unsave_warning),
                            new Alert.OnAlertClickListener() {
                                @Override
                                public void onPositive(DialogInterface dialog) {
                                    saveUnSaveJob(model.getId(), status, model);
                                }

                                @Override
                                public void onNegative(DialogInterface dialog) {
                                    ((CheckBox) v).setChecked(true);
                                    dialog.dismiss();
                                }
                            });
                } else {
                    saveUnSaveJob(model.getId(), status, model);
                }
                break;

            default:
                if (getActivity() != null)
                    getActivity().startActivity(new Intent(getActivity(), JobDetailActivity.class)
                            .putExtra(Constants.EXTRA_JOB_DETAIL_ID, mSelectedJobID));
                break;
        }
    }

    private int calculateZoomLevel(int screenWidth) {
        double equatorLength = 40075004; // in meters
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * screenWidth) > 400000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        LogUtils.LOGD("ADNAN", "zoom level = " + zoomLevel);
        return zoomLevel;
    }
}

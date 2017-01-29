package com.appster.dentamatch.ui.searchjob;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityJobDetailBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.JobDetailRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;

/**
 * Created by Appster on 25/01/17.
 */

public class JobDetailActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    private final int MAP_ZOOM_LEVEL = 7;
    private int jobID;
    private ActivityJobDetailBinding mBinding;
    private GoogleMap mGoogleMap;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_job_detail);
        setData();

        if(getIntent().hasExtra(Constants.EXTRA_JOB_DETAIL_ID)){
            jobID = getIntent().getIntExtra(Constants.EXTRA_JOB_DETAIL_ID,0);
        }

        mBinding.mapJobDetail.onCreate(savedInstanceState);
        mBinding.mapJobDetail.getMapAsync(this);
        getJobDetail();
    }


    private void setData() {
        mBinding.jobDetailToolbar.tvToolbarGeneralLeft.setText(getString(R.string.header_job_detail));
        mBinding.jobDetailToolbar.ivToolBarLeft.setOnClickListener(this);
        mBinding.tvJobDetailDocReadMore.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);

        addMarker(45.02695045318545, -98.0859375, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapJobDetail.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapJobDetail.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapJobDetail.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.mapJobDetail.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapJobDetail.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapJobDetail.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            case R.id.tv_job_detail_doc_read_more:
                break;

            default: break;
        }
    }

    private void getJobDetail(){
        JobDetailRequest request = new JobDetailRequest();
        request.setJobId(jobID);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.getJobDetail(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {

            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }



    private void addMarker(double lat, double lng, boolean isSelected) {
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(lat, lng));

        if(isSelected) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));
        }else{
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
        }

        mGoogleMap.addMarker(options);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), MAP_ZOOM_LEVEL));

    }
}

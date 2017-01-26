package com.appster.dentamatch.ui.jobs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentJobsMapBinding;
import com.appster.dentamatch.model.LocationEvent;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.PermissionUtils;
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

/**
 * Created by Appster on 24/01/17.
 */

public class JobMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private final int MARKER_PADDING = 100;

    private BottomSheetBehavior mBottomSheetBehavior;
    private FragmentJobsMapBinding mMapBinding;
    private GoogleMap mGoogleMap;
    private  double mCurrentLocLat;
    private  double mCurrentLocLng;
    private ArrayList<LatLng> mMarkerPositionList;
    private Marker previousSelectedMarker;


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
        if( !EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMapBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.fragment_jobs_map, container, false);
        mMapBinding.mapView.onCreate(savedInstanceState);
        mMarkerPositionList = new ArrayList<>();
        mBottomSheetBehavior = BottomSheetBehavior.from(mMapBinding.jobMapInfoWindow);
        mBottomSheetBehavior.setHideable(false);
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

        if (PermissionUtils.checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, getActivity())) {
            mGoogleMap.setMyLocationEnabled(true);
            LocationUtils.addFragment((AppCompatActivity) getActivity());
        } else {
            PermissionUtils.requestPermission(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_LOCATION_ACCESS) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mGoogleMap.setMyLocationEnabled(true);
                LocationUtils.addFragment((AppCompatActivity) getActivity());
            }
        }
    }

    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        Location location = locationEvent.getMessage();
        mCurrentLocLat = location.getLatitude();
        mCurrentLocLng = location.getLongitude();

        /**
         * the map is already loaded as we request current location onMapReady method.
         * So we add markers in this method after fetching current location of user.
         */
        mMarkerPositionList.clear();
        mMarkerPositionList.add(new LatLng(28.632746799225853, 77.2119140625));
        mMarkerPositionList.add(new LatLng(28.633500140406092, 77.23285675048828));

        addMarker(mMarkerPositionList);

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
        /**
         * Animate the camera to marker and bring it to center.
         */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker.getPosition());
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MARKER_PADDING));

        if(previousSelectedMarker != null ){
            /**
             * In case current and previous markers are same close the info window and deselect marker.
             */
            if(previousSelectedMarker.getId().equalsIgnoreCase(marker.getId())){
                collapseInfoDialog();
                previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
                previousSelectedMarker = null;
            }else{
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));
                previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
                previousSelectedMarker = marker;
                openInfoDialog();
            }

        }else{
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));
            previousSelectedMarker = marker;
            openInfoDialog();
        }



        return false;
    }

    private void openInfoDialog() {
        /**
         * In case a previous bottom sheet is remaining open then 1st the previous one gets hidden
         * and then a new one appears.
         */
        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            collapseInfoDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 200);
        }else{
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }


    }

    private void collapseInfoDialog(){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private void addMarker(ArrayList<LatLng> markerList ) {
        /**
         * Lat lng Bounds of the entire icons on the map.
         */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        for (LatLng latLng : markerList){
            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
            mGoogleMap.addMarker(options);
            builder.include(options.getPosition());
        }

        /**
         * Include the current location also into the bounds so that the screen can be adjusted to a zoom
         * level inclusive of both current location and other location markers.
         */
        builder.include(new LatLng(mCurrentLocLat, mCurrentLocLng));
        LatLngBounds bounds = builder.build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MARKER_PADDING));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        collapseInfoDialog();
        if(previousSelectedMarker != null) {
            previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
            previousSelectedMarker = null;
        }
    }
}

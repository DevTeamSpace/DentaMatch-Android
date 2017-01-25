package com.appster.dentamatch.ui.jobs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentJobsMapBinding;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Appster on 24/01/17.
 */

public class JobMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private final int MARKER_PADDING = 5;
    private final int REQ_MY_LOCATION = 5;

    private BottomSheetBehavior mBottomSheetBehavior;
    private FragmentJobsMapBinding mMapBinding;
    private GoogleMap mGoogleMap;

    @Override
    public String getFragmentName() {
        return null;
    }

    public static JobMapFragment newInstance() {
        return new JobMapFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showProgressBar("Loading...");
        mMapBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.fragment_jobs_map, container, false);
        mMapBinding.mapView.onCreate(savedInstanceState);
        mBottomSheetBehavior = BottomSheetBehavior.from(mMapBinding.jobMapInfoWindow);
        mBottomSheetBehavior.setHideable(false);
        mMapBinding.mapView.getMapAsync(this);
        return mMapBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        hideProgressBar();
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(this);

        if (PermissionUtils.checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, getActivity())) {
            mGoogleMap.setMyLocationEnabled(true);
        }else{
            PermissionUtils.requestPermission(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_MY_LOCATION);
        }

        addMarker(45.02695045318545, -98.0859375, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_MY_LOCATION) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
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
        openInfoDialog();
        return false;
    }

    private void openInfoDialog() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        /**
         * Lat lng Bounds of the entire icons on the map.
         */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(options.getPosition());
        LatLngBounds bounds = builder.build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MARKER_PADDING));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }
}

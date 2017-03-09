package com.appster.dentamatch.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.model.LocationEvent;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.common.math.DoubleMath;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class PlacesMapActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {

    private static String TAG = "DentaPlaces";
    private GoogleApiClient mGoogleApiClient;

    private GoogleMap mMap;
    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;
    private ImageView mImgClear;
    private RelativeLayout mLayout;

    private String mPostalCode;
    private String mPlaceName;
    private String mLatitude;
    private String mLongitude;
    private String mCountry;
    private String mState;
    private String mCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_map);

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
        mImgClear = (ImageView) findViewById(R.id.img_clear);
        mLayout = (RelativeLayout) findViewById(R.id.layout_done);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mImgClear.setOnClickListener(this);
        mLayout.setOnClickListener(this);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null);
        mAutocompleteView.setAdapter(mAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_LATITUDE)) {
            mLatitude = getIntent().getStringExtra(Constants.EXTRA_LATITUDE);
            mLongitude = getIntent().getStringExtra(Constants.EXTRA_LONGITUDE);
            mPostalCode = getIntent().getStringExtra(Constants.EXTRA_POSTAL_CODE);
            mPlaceName = getIntent().getStringExtra(Constants.EXTRA_PLACE_NAME);

            mAutocompleteView.setAdapter(null);
            mAutocompleteView.setText(mPlaceName);
            mAutocompleteView.setAdapter(mAdapter);

        } else {
            EventBus.getDefault().register(this);
            LocationUtils.addFragment(this);
        }
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMap();

        if (mLatitude != null) {
            Location location = new Location("");
            double lat = Double.parseDouble(mLatitude);
            double longi = Double.parseDouble(mLongitude);

            location.setLatitude(lat);
            location.setLongitude(longi);

            LatLng latLng = new LatLng(lat, longi);

            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.MAP_ZOOM_LEVEL));
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_clear:
                mAutocompleteView.setText("");
                mMap.clear();
                break;

            case R.id.layout_done:
                hideKeyboard();
                if (mAutocompleteView.getText().toString().trim().isEmpty()) {
                    Utils.showToast(this, getString(R.string.error_empty_address));
                    return;
                }

                setResult(RESULT_OK, prepareIntent());
                finish();
                break;
        }
    }

    // This method will be called when Event is posted.
    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        Location location = locationEvent.getMessage();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.MAP_ZOOM_LEVEL));
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        Address address = getReverseGeoCode(latLng);
        setData(address);
    }

    private void setData(Address address) {
        if (address != null) {
            mPlaceName = convertAddressToString(address);
            mPostalCode = address.getPostalCode() == null ? "" : address.getPostalCode();
            mLatitude = String.valueOf(address.getLatitude());
            mLongitude = String.valueOf(address.getLongitude());
            mCountry = address.getCountryName();
            mCity = address.getLocality();
            mState = address.getAdminArea();

            mAutocompleteView.setAdapter(null);
            mAutocompleteView.setText(mPlaceName);
            mAutocompleteView.setAdapter(mAdapter);

        } else {
            mPlaceName = "";
            mPostalCode = "";
            mLatitude = "";
            mLongitude = "";
            mCountry = "";
        }

        LogUtils.LOGD(TAG, "(Postal and Place) " + mPostalCode + ", " + mPlaceName);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));

        Address address = getReverseGeoCode(latLng);

        setData(address);
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            LogUtils.LOGD(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

//            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
//                    Toast.LENGTH_SHORT).show();
            LogUtils.LOGD(TAG, "Called getPlaceById to get Place details for " + placeId);

            hideKeyboard();
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                LogUtils.LOGE(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            LatLng latLng = place.getLatLng();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.MAP_ZOOM_LEVEL));
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng));

            LogUtils.LOGD(TAG, latLng.toString());

            Address address = getReverseGeoCode(latLng);

            setData(address);

            places.release();
        }
    };

    private Intent prepareIntent() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_PLACE_NAME, mPlaceName);
        intent.putExtra(Constants.EXTRA_POSTAL_CODE, mPostalCode);
        intent.putExtra(Constants.EXTRA_LATITUDE, mLatitude);
        intent.putExtra(Constants.EXTRA_LONGITUDE, mLongitude);

        LogUtils.LOGD(TAG, "Postal Code " + mPostalCode + ", Place " + mPlaceName);

        return intent;
    }

    private void setMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMapClickListener(this);
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

        LogUtils.LOGD(TAG, "convertAddressToString " + sb.toString());

        return sb.toString();
    }

    private Address getReverseGeoCode(LatLng latLng) {
        Address address = null;

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addressList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return address;
    }
}

package ai.testtask.fasten;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.text.DateFormat;
import java.util.Date;

import ai.testtask.fasten.databinding.MainActivityBinding;
import ai.testtask.fasten.providers.LocationProvider;

public class MainActivity extends AppCompatActivity implements LocationProvider.LocationCallback {

    private static final String TAG = MainActivity.class.getSimpleName();



    private final static String KEY_REQUESTING_LOCATION_UPDATES = "keys:requestingLocationUpdates";
    private final static String KEY_LOCATION = "keys:location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "keys:lastUpdatedTimeString";

    private boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;

    private Location mCurrentLocation;

    private LocationProvider mLocationProvider;
    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (mRequestingLocationUpdates)
            mLocationProvider.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        mLocationProvider.stopLocationUpdates();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onLocationUpdatesStopped() {
        Log.i(TAG, "location update stopped!");
    }

    @Override
    public void onLocationSettingsError(Exception e) {
        Log.e(TAG, "Location settings error", e);
    }

    @Override
    public void onFusedLocationClientSecurityError(SecurityException e) {
        Log.e(TAG, "Location client security error", e);
    }

    @Override
    public void onLocationResult(LocationResult result) {
        mCurrentLocation = result.getLastLocation();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.i(TAG, "time = " + mLastUpdateTime
                + "; \nlatitude = " + mCurrentLocation.getLatitude()
                + "; \nlongitude = " + mCurrentLocation.getLongitude()
                + "; \naccuracy = " + mCurrentLocation.getAccuracy());

        if (mCurrentLocation.getAccuracy() <= 10.0) {
            mLocationProvider.stopLocationUpdates();
        }
    }
}
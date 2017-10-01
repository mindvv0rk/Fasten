package ai.testtask.fasten.weather.current;

import com.google.android.gms.location.LocationResult;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import ai.testtask.fasten.core.AbstractPresenter;
import ai.testtask.fasten.core.di.Dagger;
import ai.testtask.fasten.core.network.IWeatherAPI;
import ai.testtask.fasten.providers.ILocationProvider;
import ai.testtask.fasten.providers.LocationProvider;

public class CurrentWeatherPresenter extends AbstractPresenter<ICurrentWeatherView> implements LocationProvider.LocationCallback {

    private static final String TAG = CurrentWeatherPresenter.class.getSimpleName();
    private static final int MIN_ACCURACY = 100;

    @Inject
    IWeatherAPI mWeatherAPI;

    private ILocationProvider mLocationProvider;
    private String mLastUpdateTime;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates;


    public CurrentWeatherPresenter(ILocationProvider locationProvider) {
        Dagger.getAppComponent().inject(this);
        mLocationProvider = locationProvider;
        mLocationProvider.setLocationCallback(this);
        mRequestingLocationUpdates = false;
    }

    @Override
    protected void onViewDetached(@NonNull ICurrentWeatherView view) {
        stopLocationUpdates(); //?
        super.onViewDetached(view);
    }

    public void startLocationUpdates() {
        if (!mRequestingLocationUpdates && mCurrentLocation == null) {
            mRequestingLocationUpdates = true;
            mLocationProvider.startLocationUpdates();
        }
    }

    public void stopLocationUpdates() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            mLocationProvider.stopLocationUpdates();
        }
    }

    @Override
    public void onLocationUpdatesStopped() {
        mRequestingLocationUpdates = false;
        Log.i(TAG, "Location updates stopped.");
    }

    @Override
    public void onLocationSettingsError(Exception e) {
        mRequestingLocationUpdates = false;
        Log.i(TAG, "Location settings error.", e);
    }

    @Override
    public void onFusedLocationClientSecurityError(SecurityException e) {
        mRequestingLocationUpdates = false;
        Log.i(TAG, "onFusedLocationClientSecurityError", e);
    }

    @Override
    public void onLocationResult(LocationResult result) {
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        mCurrentLocation = result.getLastLocation();

        Log.i(TAG, "time = " + mLastUpdateTime
                + "; \nlatitude = " + result.getLastLocation().getLatitude()
                + "; \nlongitude = " + result.getLastLocation().getLongitude()
                + "; \naccuracy = " + result.getLastLocation().getAccuracy());
        if (result.getLastLocation().getAccuracy() <= MIN_ACCURACY) {
            stopLocationUpdates();
        }
    }

    private void getForecast(double  latitude, double longitude) {

    }

}

package ai.testtask.fasten.weather.current;

import com.google.android.gms.location.LocationResult;

import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import ai.testtask.fasten.core.AbstractPresenter;
import ai.testtask.fasten.providers.ILocationProvider;
import ai.testtask.fasten.providers.LocationProvider;

public class CurrentWeatherPresenter extends AbstractPresenter<ICurrentWeatherView> implements LocationProvider.LocationCallback {

    private static final String TAG = CurrentWeatherPresenter.class.getSimpleName();

    private ILocationProvider mLocationProvider;

    public CurrentWeatherPresenter(ILocationProvider locationProvider) {
        mLocationProvider = locationProvider;
        mLocationProvider.setLocationCallback(this);
    }

    public void startLocationUpdates() {
        mLocationProvider.startLocationUpdates();
    }

    public void stopLocationUpdates() {
        mLocationProvider.stopLocationUpdates();
    }

    @Override
    public void onLocationUpdatesStopped() {
        Log.i(TAG, "Location updates stopped.");
    }

    @Override
    public void onLocationSettingsError(Exception e) {
        Log.i(TAG, "Location settings error.", e);
    }

    @Override
    public void onFusedLocationClientSecurityError(SecurityException e) {
        Log.i(TAG, "onFusedLocationClientSecurityError", e);
    }

    @Override
    public void onLocationResult(LocationResult result) {
        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.i(TAG, "time = " + mLastUpdateTime
                + "; \nlatitude = " + result.getLastLocation().getLatitude()
                + "; \nlongitude = " + result.getLastLocation().getLongitude()
                + "; \naccuracy = " + result.getLastLocation().getAccuracy());
    }
}

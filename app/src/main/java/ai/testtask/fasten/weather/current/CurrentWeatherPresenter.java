package ai.testtask.fasten.weather.current;

import com.google.android.gms.location.LocationResult;

import ai.testtask.fasten.core.AbstractPresenter;
import ai.testtask.fasten.providers.ILocationProvider;
import ai.testtask.fasten.providers.LocationProvider;

public class CurrentWeatherPresenter extends AbstractPresenter<ICurrentWeatherView> implements LocationProvider.LocationCallback {

    private ILocationProvider mLocationProvider;

    public CurrentWeatherPresenter(ILocationProvider locationProvider) {
        mLocationProvider = locationProvider;
        mLocationProvider.setLocationCallback(this);
    }

    @Override
    public void onLocationUpdatesStopped() {

    }

    @Override
    public void onLocationSettingsError(Exception e) {

    }

    @Override
    public void onFusedLocationClientSecurityError(SecurityException e) {

    }

    @Override
    public void onLocationResult(LocationResult result) {

    }
}

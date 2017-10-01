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
import ai.testtask.fasten.core.di.NetworkModule;
import ai.testtask.fasten.core.network.IWeatherAPI;
import ai.testtask.fasten.core.network.NetworkModuleFactory;
import ai.testtask.fasten.providers.ILocationProvider;
import ai.testtask.fasten.providers.LocationProvider;
import ai.testtask.fasten.weather.model.response.weather.WeatherResponse;
import ai.testtask.fasten.weather.model.weather.Weather;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
            getForecast(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude());
            stopLocationUpdates();
        }
    }

    private void getForecast(double  latitude, double longitude) {
        String param = String.valueOf(latitude)
                .concat(",")
                .concat(String.valueOf(longitude))
                .concat(NetworkModuleFactory.WEATHER_REQUEST_FORMAT);
        mWeatherAPI
                .getForecastFor10DaysByLocation(param)
                .subscribeOn(Schedulers.io())
                .map(new Func1<WeatherResponse, Weather>() {
                    @Override
                    public Weather call(WeatherResponse weatherResponse) {
                        return new Weather(weatherResponse.getLocation(),
                                weatherResponse.getForecast().getSimpleForecast().getForecastDayList());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Weather>() {
                            @Override
                            public void call(Weather weather) {
                                Log.i(TAG, "Weather data by location loaded");
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Error on weather request", throwable);
                                //??
                            }
                        }
                );
    }

}

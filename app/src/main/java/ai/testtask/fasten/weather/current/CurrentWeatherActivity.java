package ai.testtask.fasten.weather.current;

import com.google.android.gms.location.LocationRequest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ai.testtask.fasten.R;
import ai.testtask.fasten.core.PresenterManager;
import ai.testtask.fasten.databinding.CurrentWeatherActivityBinding;
import ai.testtask.fasten.providers.LocationProvider;


public final class CurrentWeatherActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private CurrentWeatherActivityBinding mBinding;
    private CurrentWeatherPresenter mPresenter;
    private int mPresenterId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.current_weather_activity);

        mPresenterId = CurrentWeatherActivity.class.getSimpleName().hashCode();
        mPresenter = PresenterManager.getPresenter(mPresenterId, new PresenterManager.PresenterFactory<CurrentWeatherPresenter>() {
            @Override
            public CurrentWeatherPresenter createPresenter() {
                return new CurrentWeatherPresenter(new LocationProvider(CurrentWeatherActivity.this, createLocationRequest()));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        locationRequest.setNumUpdates(1);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        return locationRequest;
    }
}

package ai.testtask.fasten;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.text.DateFormat;
import java.util.Date;

import ai.testtask.fasten.databinding.MainActivityBinding;
import ai.testtask.fasten.providers.LocationProvider;
import ai.testtask.fasten.weather.current.CurrentWeatherActivity;
import ai.testtask.fasten.weather.search.SearchWeatherActivity;

public class MainActivity extends AppCompatActivity implements MainActivityClickHandler {

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
        mBinding.setHandler(this);

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

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCurrentWeatherClick(View view) {
        CurrentWeatherActivity.start(this);
    }

    @Override
    public void onSearchWeatherClick(View view) {
        SearchWeatherActivity.start(this);
    }
}
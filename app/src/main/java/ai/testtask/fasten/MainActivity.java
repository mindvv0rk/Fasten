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

    private LocationProvider mLocationProvider;
    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mBinding.setHandler(this);
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
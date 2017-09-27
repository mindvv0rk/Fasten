package ai.testtask.fasten.weather.current;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ai.testtask.fasten.R;
import ai.testtask.fasten.databinding.CurrentWeatherActivityBinding;


public final class CurrentWeatherActivity extends AppCompatActivity {

    private CurrentWeatherActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.current_weather_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

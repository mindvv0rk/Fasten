package ai.testtask.fasten.weather.current;

import com.google.android.gms.location.LocationRequest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import ai.testtask.fasten.BuildConfig;
import ai.testtask.fasten.R;
import ai.testtask.fasten.core.FormViewState;
import ai.testtask.fasten.core.PresenterManager;
import ai.testtask.fasten.databinding.CurrentWeatherActivityBinding;
import ai.testtask.fasten.providers.IPermissionsManager;
import ai.testtask.fasten.providers.LocationProvider;
import ai.testtask.fasten.providers.PermissionsManager;
import ai.testtask.fasten.weather.model.weather.Weather;


public final class CurrentWeatherActivity extends AppCompatActivity implements ICurrentWeatherView, CurrentWeatherClickHandler {

    private static final String TAG = CurrentWeatherActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private CurrentWeatherActivityBinding mBinding;
    private CurrentWeatherPresenter mPresenter;
    private int mPresenterId;

    private IPermissionsManager mPermissionsManager;
    private boolean mCheckingPermissions;


    public static void start(Context context) {
        Intent starter = new Intent(context, CurrentWeatherActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.current_weather_activity);

        mBinding.setRetryHandler(this);

        mPermissionsManager = new PermissionsManager();

        mCheckingPermissions = false;

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
        mPresenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionsManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mCheckingPermissions = false;
            mPresenter.startLocationUpdates();
        } else {
            if (mCheckingPermissions) return;

            mCheckingPermissions = true;
            mPermissionsManager.requestPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    REQUEST_PERMISSIONS_REQUEST_CODE,
                    new PermissionsManager.PermissionsCallback() {
                        @Override
                        public void shouldRequestPermissionRationale(String permission) {
                            showSnackbar(R.string.permission_rationale,
                                    android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPermissionsManager.requestPermissionWithoutCheck(
                                                    CurrentWeatherActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                                        }
                                    });
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        mPresenter.detachView(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            PresenterManager.removePresenter(mPresenterId);
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                mCheckingPermissions = false;
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                mCheckingPermissions = false;
                mPresenter.startLocationUpdates();
            } else {
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener)
                .show();
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        return locationRequest;
    }

    @Override
    public void showLoading() {
        mBinding.setViewState(FormViewState.LOADING);
    }

    @Override
    public void showError(String message) {
        mBinding.setViewState(FormViewState.ERROR);
        mBinding.setErrorMessage(message);
    }

    @Override
    public void showWeather(Weather weather) {
        mBinding.setViewState(FormViewState.SUCCESS);
        String cityName = weather.getLocation().getCountryName()
                .concat(", ")
                .concat(weather.getLocation().getCityName());
        mBinding.setCityName(cityName);
        //populate adapter
    }

    @Override
    public void onRetryClick(View view) {
        mPresenter.reloadData();
    }
}

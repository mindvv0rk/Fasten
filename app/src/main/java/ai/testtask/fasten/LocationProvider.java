package ai.testtask.fasten;

import android.content.Context;
import android.content.IntentSender;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationProvider {

    private static final String TAG = LocationProvider.class.getSimpleName();



    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mSettingsClient;

    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private com.google.android.gms.location.LocationCallback mLocationCallback;
    private LocationCallback mLocationProviderCallback;

    public LocationProvider(@NonNull Context context, @NonNull LocationRequest locationRequest, @NonNull LocationCallback locationCallback) {
        mLocationProviderCallback = locationCallback;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mLocationRequest = locationRequest;
        mLocationSettingsRequest = buildLocationSettingsRequest(mLocationRequest);
        mLocationCallback = createLocationCallback();
    }

//    public LocationProvider(@NonNull Context context) {
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
//        mSettingsClient = LocationServices.getSettingsClient(context);
//
//        mLocationRequest = createDefaultLocationRequest();
//        mLocationSettingsRequest = buildLocationSettingsRequest(mLocationRequest);
//        mLocationCallback = createLocationCallback();
//    }

//    private LocationRequest createDefaultLocationRequest() {
//        LocationRequest request = new LocationRequest();
//        request.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        request.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        return request;
//    }

    private com.google.android.gms.location.LocationCallback createLocationCallback() {

        return new com.google.android.gms.location.LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLocationProviderCallback.onLocationResult(locationResult);
            }
        };
    }

    private LocationSettingsRequest buildLocationSettingsRequest(LocationRequest locationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        return builder.build();
    }

    public void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        try {
                            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        } catch (SecurityException e) {
                            mLocationProviderCallback.onFusedLocationClientSecurityError(e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mLocationProviderCallback.onLocationSettingsError(e);
                    }
                });
    }

    public void stopLocationUpdates() {
        mFusedLocationProviderClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mLocationProviderCallback.onLocationUpdatesStopped();
                    }
                });
    }


    public interface LocationCallback {
        void onLocationUpdatesStopped();
        void onLocationSettingsError(Exception e);
        void onFusedLocationClientSecurityError(SecurityException e);
        void onLocationResult(LocationResult result);
    }

}

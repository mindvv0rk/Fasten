package ai.testtask.fasten.providers;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationProvider implements ILocationProvider {

    private static final String TAG = LocationProvider.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mSettingsClient;

    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private com.google.android.gms.location.LocationCallback mLocationCallback;
    private LocationCallback mLocationProviderCallback;

    public LocationProvider(@NonNull Context context, @NonNull LocationRequest locationRequest) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mLocationRequest = locationRequest;
        mLocationSettingsRequest = buildLocationSettingsRequest(mLocationRequest);
        mLocationCallback = createLocationCallback();
    }

    @Override
    public void setLocationCallback(@NonNull LocationCallback locationCallback) {
        mLocationProviderCallback = locationCallback;
    }

    private com.google.android.gms.location.LocationCallback createLocationCallback() {

        return new com.google.android.gms.location.LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (mLocationProviderCallback != null)
                    mLocationProviderCallback.onLocationResult(locationResult);
            }
        };
    }

    private LocationSettingsRequest buildLocationSettingsRequest(LocationRequest locationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        return builder.build();
    }

    @Override
    public void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        try {
                            mFusedLocationProviderClient.requestLocationUpdates(
                                    mLocationRequest,
                                    mLocationCallback,
                                    Looper.myLooper());
                        } catch (SecurityException e) {
                            if (mLocationProviderCallback != null)
                                mLocationProviderCallback.onFusedLocationClientSecurityError(e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mLocationProviderCallback != null)
                            mLocationProviderCallback.onLocationSettingsError(e);
                    }
                });
    }

    @Override
    public void stopLocationUpdates() {
        mFusedLocationProviderClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (mLocationProviderCallback != null)
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

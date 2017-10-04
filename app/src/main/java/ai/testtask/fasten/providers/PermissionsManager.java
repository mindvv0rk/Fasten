package ai.testtask.fasten.providers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public final class PermissionsManager implements IPermissionsManager {
    private static final String TAG = PermissionsManager.class.getSimpleName();

    @Override
    public boolean checkPermission(Context context, String permission) {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                permission);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission(AppCompatActivity appCompatActivity, String permission, int requestCode, PermissionsCallback callback) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity,
                        permission);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            callback.shouldRequestPermissionRationale(permission);
        } else {
            requestPermissionWithoutCheck(appCompatActivity, permission, requestCode);
        }
    }

    @Override
    public void requestPermissionWithoutCheck(AppCompatActivity appCompatActivity, String permission, int requestCode) {
        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(appCompatActivity,
                new String[]{permission},
                requestCode);
    }


    public interface PermissionsCallback {
        void shouldRequestPermissionRationale(String permission);
    }
}

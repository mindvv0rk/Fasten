package ai.testtask.fasten.providers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

public interface IPermissionsManager {

    boolean checkPermission(Context context, String permission);
    void requestPermission(AppCompatActivity appCompatActivity, String permission, int requestCode, PermissionsManager.PermissionsCallback callback);
    void requestPermissionWithoutCheck(AppCompatActivity appCompatActivity, String permission, int requestCode);

}

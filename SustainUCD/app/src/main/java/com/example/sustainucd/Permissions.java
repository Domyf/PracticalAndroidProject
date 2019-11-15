package com.example.sustainucd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    public static void AskExternalStoragePermission(Activity activity, int permission_request)
    {
        //Request the permission
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permission_request);
    }

    public static void AskAccessFineLocationPermission(Activity activity, int permission_request)
    {
        //Request the permission
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permission_request);
    }

    public static boolean HasExternalStoragePermission(Context context)
    {
        return HasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean HasAccessFineLocationPermission(Context context)
    {
        return HasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private static boolean HasPermission(Context context, String permission)
    {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}

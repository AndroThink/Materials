package com.androthink.materials.utils;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.androthink.materials.callback.RequestCallback;
import com.androthink.materials.dialog.RequestPermissionsDialog;

public class PermissionsUtils {

    private final AppCompatActivity activity;

    @NonNull
    public static PermissionsUtils getInstance(AppCompatActivity activity){
        return new PermissionsUtils(activity);
    }

    private PermissionsUtils(AppCompatActivity activity){
        this.activity = activity;
    }

    public boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(activity, permission) ==  PackageManager.PERMISSION_GRANTED;
    }

    public void createPermissionRequest(String[] permissionArray, RequestCallback requestCallback){

        FragmentManager fm = activity.getSupportFragmentManager();
        RequestPermissionsDialog requestPermissionsDialog = new RequestPermissionsDialog(permissionArray,requestCallback);
        requestPermissionsDialog.show(fm,"PermissionRequest");
    }
}

package com.androthink.materials.dialog;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.androthink.materials.callback.RequestCallback;

@SuppressLint("ValidFragment")
public class RequestPermissionsDialog extends DialogFragment {

    private static final int REQUEST_CODE = 4321;

    private final String[] permissionArray;
    private final RequestCallback requestCallback;

    @SuppressLint("ValidFragment")
    public RequestPermissionsDialog(String[] permissionArray,RequestCallback requestCallback){
        this.permissionArray = permissionArray;
        this.requestCallback = requestCallback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        }

        requestPermissions(permissionArray, REQUEST_CODE);

        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            requestCallback.onCallback(permissions, isGranted(grantResults));
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        RequestPermissionsDialog.this.dismiss();
    }

    private boolean[] isGranted(@NonNull int[] result){
        boolean[] grantResults = new boolean[result.length];

        for (int i = 0; i < result.length; i++) {
            grantResults[i] = result[i] == PackageManager.PERMISSION_GRANTED;
        }

        return grantResults;
    }
}

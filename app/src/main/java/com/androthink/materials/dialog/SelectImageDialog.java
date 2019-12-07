package com.androthink.materials.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.androthink.materials.callback.RequestCallback;
import com.androthink.materials.callback.SelectImageCallback;
import com.androthink.materials.utils.PermissionsUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

@SuppressLint("ValidFragment")
public class SelectImageDialog extends DialogFragment implements RequestCallback {

    private static final int REQUEST_CODE = 4331;

    private final SelectImageCallback callback;
    private boolean crop;

    @SuppressLint("ValidFragment")
    public SelectImageDialog(SelectImageCallback callback,boolean crop){
        this.callback = callback;
        this.crop = crop;
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

        if(getActivity() != null){
            PermissionsUtils permissionsUtils = PermissionsUtils.getInstance(((AppCompatActivity)getActivity()));

            if(permissionsUtils.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                selectImage();
            }else {
                permissionsUtils.createPermissionRequest(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},SelectImageDialog.this);
            }
        }else {
            SelectImageDialog.this.dismiss();
        }

        return null;
    }

    @Override
    public void onCallback(String[] permissions,@NonNull boolean[] grantResults) {
        if(grantResults[0]){
            selectImage();
        }else {
            SelectImageDialog.this.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            if(crop) {
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    //Get image
                    callback.onSelected(((Bitmap) extras.getParcelable("data")));
                }
            }else {
                if(getContext() != null && data.getData() != null) {
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        callback.onSelected(BitmapFactory.decodeStream(inputStream));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        SelectImageDialog.this.dismiss();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");

        if (crop) {
            intent.putExtra("crop", "true");
            intent.putExtra("scale", true);
            intent.putExtra("outputX", 256);
            intent.putExtra("outputY", 256);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
        }

        startActivityForResult(intent, REQUEST_CODE);
    }
}

package com.androthink.materials.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;

import com.androthink.materials.R;
import com.androthink.materials.databinding.LayoutCustomMarkerBinding;

public class MapUtils {

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, @ColorRes int color) {

        LayoutCustomMarkerBinding markerView = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_custom_marker, null, false);

        markerView.markerImage.setImageResource(resource);

        // mutate to not share its state with any other drawable
        Drawable drawableWrap = DrawableCompat.wrap(markerView.markerShape.getBackground()).mutate();
        DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, color));
        markerView.markerShape.setBackground(drawableWrap);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        markerView.getRoot().setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.getRoot().measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerView.getRoot().layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerView.getRoot().buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(markerView.getRoot().getMeasuredWidth(),
                markerView.getRoot().getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        markerView.getRoot().draw(new Canvas(bitmap));

        return bitmap;
    }

    public static Bitmap createCustomMarker(Context context,Bitmap bitmap,@ColorRes int color) {

        LayoutCustomMarkerBinding markerView = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_custom_marker, null, false);

        markerView.markerImage.setImageBitmap(bitmap);

        // mutate to not share its state with any other drawable
        Drawable drawableWrap = DrawableCompat.wrap(markerView.markerShape.getBackground()).mutate();
        DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, color));
        markerView.markerShape.setBackground(drawableWrap);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        markerView.getRoot().setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.getRoot().measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerView.getRoot().layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerView.getRoot().buildDrawingCache();

        Bitmap resultBitmap = Bitmap.createBitmap(markerView.getRoot().getMeasuredWidth(),
                markerView.getRoot().getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        markerView.getRoot().draw(new Canvas(resultBitmap));

        return resultBitmap;
    }
}

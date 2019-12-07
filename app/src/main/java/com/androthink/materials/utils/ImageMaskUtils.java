package com.androthink.materials.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.androthink.materials.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageMaskUtils {
    private final Context context;
    private String imageUrl;
    private Bitmap sourceBitmapImage;
    private ImageView imageView;

    private final int maskDrawableId;
    private final int maskBorderDrawableId;

    @NonNull
    public static ImageMaskUtils getInstance(Context context, int maskDrawableId){
        return new ImageMaskUtils(context,maskDrawableId);
    }

    private ImageMaskUtils(Context context, int maskDrawableId){
        this.context = context;
        this.maskDrawableId = maskDrawableId;
        this.maskBorderDrawableId = -1;
    }

    @NonNull
    public static ImageMaskUtils getInstance(Context context, int maskDrawableId, int maskBorderDrawableId){
        return new ImageMaskUtils(context,maskDrawableId,maskBorderDrawableId);
    }

    private ImageMaskUtils(Context context, int maskDrawableId, int maskBorderDrawableId){
        this.context = context;
        this.maskDrawableId = maskDrawableId;
        this.maskBorderDrawableId = maskBorderDrawableId;
    }

    public ImageMaskUtils load(String imageUrl){
        this.imageUrl = imageUrl;
        return this;
    }

    public ImageMaskUtils load(Bitmap bitmap){
        this.imageUrl = null;
        this.sourceBitmapImage = bitmap;
        return this;
    }

    public void into(ImageView imageView){
        if(this.imageUrl == null){
            imageView.setImageBitmap(ImageUtils.getInstance()
                    .applyMask(context,sourceBitmapImage,maskDrawableId,maskBorderDrawableId));
        }else {
            this.imageView = imageView;
            new AsyncGettingBitmapFromUrl().execute(imageUrl);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncGettingBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            imageView.setImageBitmap(ImageUtils.getInstance().applyMask(context,
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_mask_waiting),
                    maskDrawableId,maskBorderDrawableId));
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if(bitmap != null){
                imageView.setImageBitmap(ImageUtils.getInstance().applyMask(context,bitmap,maskDrawableId,maskBorderDrawableId));
            }else {
                imageView.setImageBitmap(ImageUtils.getInstance().applyMask(context,
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_mask_error),
                        maskDrawableId,maskBorderDrawableId));
            }
        }
    }
}

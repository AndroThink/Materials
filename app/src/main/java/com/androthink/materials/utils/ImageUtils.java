package com.androthink.materials.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.androthink.materials.R;
import com.androthink.materials.callback.SelectImageCallback;
import com.androthink.materials.dialog.SelectImageDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class ImageUtils {

    private static ImageUtils imageUtils;

    public static ImageUtils getInstance() {
        if (imageUtils == null) {
            imageUtils = new ImageUtils();
        }
        return imageUtils;
    }

    public void selectImage(FragmentManager fragmentManager, boolean crop, SelectImageCallback callback) {
        SelectImageDialog requestPermissionsDialog = new SelectImageDialog(callback, crop);
        requestPermissionsDialog.show(fragmentManager, "SelectImage");
    }

    public Bitmap convertViewToBitmap(@NonNull View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    public String encodeToBase64(Bitmap image) {

        if (image == null) {
            return "";
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        return getResizedBitmap(bm, newWidth, newHeight);
    }

    private Bitmap getResizedBitmap(@NonNull Bitmap bm, int newWidth, int newHeight) {

        float scaleWidth = ((float) newWidth) / bm.getWidth();
        float scaleHeight = ((float) newHeight) / bm.getHeight();

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);

        if (!bm.isRecycled()) {
            bm.recycle();
        }
        // bm.recycle();

        return resizedBitmap;
    }

    Bitmap applyMask(@NonNull Context context, Bitmap mainImage, int maskDrawableId, int maskBorderDrawableId) {
        Canvas canvas = new Canvas();

        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_mask_background);
        Bitmap mask = BitmapFactory.decodeResource(context.getResources(), maskDrawableId);

        Bitmap maskBorder = null;

        if (maskBorderDrawableId != -1) {
            maskBorder = BitmapFactory.decodeResource(context.getResources(), maskBorderDrawableId);
        }

        mainImage = getResizedBitmap(mainImage, mask.getWidth(), mask.getHeight());
        background = getResizedBitmap(background, mask.getWidth(), mask.getHeight());

        Bitmap result = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Bitmap.Config.ARGB_8888);

        canvas.setBitmap(result);
        Paint paint = new Paint();
        paint.setFilterBitmap(false);

        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(mainImage, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        if (maskBorder != null) {
            canvas.drawBitmap(maskBorder, 0, 0, paint);
        }
        return result;
    }

    @Nullable
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return (BitmapFactory.decodeStream(input));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Bitmap generateBarCode(String data) {
        try {
            return encodeAsBitmap(data, BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Bitmap generateQrCode(String data) {
        try {
            return encodeAsBitmap(data,BarcodeFormat.QR_CODE, 512, 512);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int imgWidth, int imgHeight) throws WriterException {
        if (contents == null)
            return null;

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, imgWidth, imgHeight, hints);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}

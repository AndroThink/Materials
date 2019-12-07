package com.androthink.materials.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TvNoiseView extends View {

    private static final int DIVIDER_WIDTH = 8;
    private static final int DIVIDER_HEIGHT = 4;
    private static final int DIVIDER_WIDTH_SCALED = 2;
    private static final int ANIM_INTERVAL = 50;

    private final Random mRandom = new Random(System.currentTimeMillis());
    private int mTileWidth;
    private int mTileHeight;

    private Bitmap mScaledBitmap;
    private Bitmap mBitmap;
    private int[] mPixels;

    private Timer mTimer;

    public TvNoiseView(Context context) {
        super(context);
    }

    public TvNoiseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mScaledBitmap == null) return;

        for (int i = 0; i < DIVIDER_WIDTH_SCALED; i++) {
            canvas.drawBitmap(mScaledBitmap, i * mScaledBitmap.getWidth(), 0, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int tileWidth = getWidth() / DIVIDER_WIDTH;
        int tileHeight = getHeight() / DIVIDER_HEIGHT;

        if (mTileWidth != tileWidth || mTileHeight != tileHeight || mPixels == null) {
            mPixels = new int[tileWidth * tileHeight];
            mBitmap = Bitmap.createBitmap(tileWidth, tileHeight, Bitmap.Config.RGB_565);
        }

        mTileWidth = tileWidth;
        mTileHeight = tileHeight;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer(150);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            stopTimer();
        } else if (visibility == View.VISIBLE) {
            startTimer(0);
        }
    }

    private void startTimer(long delay) {
        mTimer = new Timer("", false);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                generateNoise();
                postInvalidate();
            }
        }, delay, ANIM_INTERVAL);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    public boolean isFreeze(){
        return mTimer == null;
    }

    public void freeze(){
        stopTimer();
    }

    public void unFreeze(){
        if(mTimer == null) {
            startTimer(0);
        }
    }

    private void generateNoise() {
        if (mPixels == null || mBitmap == null) return;

        for (int i = 0; i < (mTileWidth * mTileHeight); i++) {
            int pix = Math.max(40, mRandom.nextInt(255));
            mPixels[i] = Color.rgb(pix, pix, pix);
        }

        int width = getWidth() / DIVIDER_WIDTH_SCALED;
        mBitmap.setPixels(mPixels, 0, mTileWidth, 0, 0, mTileWidth, mTileHeight);
        mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, width, getHeight(), false);
    }

}
package com.androthink.materials.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

public class FontUtils {

    private static FontUtils fontUtils;
    private final Typeface fontTypeFace;

    public static FontUtils getInstance(Context context,int fontResource){
        if(fontUtils == null){
            fontUtils = new FontUtils(context,fontResource);
        }

        return fontUtils;
    }

    private FontUtils(Context context,int fontResource) {
        // fontResource put fonts in font resource directory ..
        this.fontTypeFace = ResourcesCompat.getFont(context,fontResource);
    }

    public void apply(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                apply(child);
            }
        } else if (v instanceof CheckBox) {
            ((CheckBox) v).setTypeface(fontTypeFace);
        }else if (v instanceof RadioButton) {
            ((RadioButton) v).setTypeface(fontTypeFace);
        } else if (v instanceof Button) {
            ((Button) v).setTypeface(fontTypeFace);
        } else if (v instanceof EditText) {
            ((EditText) v).setTypeface(fontTypeFace);
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(fontTypeFace);
        }
    }

    // For PagerTitle Font ..
    public void applyForTabLayout(@NonNull TabLayout tabLayout){
        for (int i = 0; i < tabLayout.getChildCount(); ++i) {
            View nextChild = tabLayout.getChildAt(i);
            if (nextChild instanceof TextView) {
                ((TextView) nextChild).setTypeface(fontTypeFace);
            }
        }
    }

    public void applyForTextInputLayout(@NonNull TextInputLayout textInputLayout) {
        textInputLayout.setTypeface(fontTypeFace);
    }

    public static void disableEditText(@NonNull EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public static void enableEditText(@NonNull EditText editText,@DrawableRes int resId) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setBackgroundResource(resId);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public static void moveText(@NonNull TextView textView){
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine();
        textView.setFocusable(true);
        textView.setSelected(true);
        textView.setHorizontallyScrolling(true);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
    }
}

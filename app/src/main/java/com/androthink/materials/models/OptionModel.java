package com.androthink.materials.models;

import android.view.View;

public class OptionModel {

    private String option;
    private View.OnClickListener callback;

    public OptionModel(String option, View.OnClickListener callback) {
        this.option = option;
        this.callback = callback;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public View.OnClickListener getCallback() {
        return callback;
    }

    public void setCallback(View.OnClickListener callback) {
        this.callback = callback;
    }
}

package com.androthink.materials.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.androthink.materials.R;
import com.androthink.materials.callback.YesNoDialogCallback;
import com.androthink.materials.databinding.DialogYesNoBinding;

public class YesNoDialog extends Dialog {

    private String messageTxt="";
    private YesNoDialogCallback callback;

    @NonNull
    public static YesNoDialog getInstance(Context context,YesNoDialogCallback callback){
        return new YesNoDialog(context,callback);
    }

    private YesNoDialog(@NonNull Context context,YesNoDialogCallback callback) {
        super(context);

        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null) {
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        DialogYesNoBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.dialog_yes_no, null, false);
        setContentView(binding.getRoot());

        setCancelable(false);

        binding.message.setText(messageTxt);

        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onNo();
                YesNoDialog.this.dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onYes();
                YesNoDialog.this.dismiss();
            }
        });

        //Fonts.apply(getContext(), findViewById(android.R.id.content));
    }

    public YesNoDialog setMessage(String message) { this.messageTxt = message; return this;}
}

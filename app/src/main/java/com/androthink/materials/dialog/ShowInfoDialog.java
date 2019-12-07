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
import com.androthink.materials.databinding.DialogShowInfoBinding;

public class ShowInfoDialog extends Dialog {

    private String titleTxt = "",messageTxt = "",btnOkText = "";

    @NonNull
    public static ShowInfoDialog getInstance(Context context){
        return new ShowInfoDialog(context);
    }

    private ShowInfoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null) {
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        DialogShowInfoBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.dialog_show_info, null, false);
        setContentView(binding.getRoot());

        setCancelable(false);

        binding.headerTitle.setText(titleTxt);
        binding.message.setText(messageTxt);

        if(!btnOkText.equals("")){
            binding.btnOk.setText(btnOkText);
        }

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfoDialog.this.dismiss();
            }
        });

        //Fonts.apply(getContext(), findViewById(android.R.id.content));
    }

    public ShowInfoDialog setTitle(String title) { this.titleTxt = title; return this; }

    public ShowInfoDialog setMessage(String message) { this.messageTxt = message; return this;}

    public ShowInfoDialog setBtnOkText(String text) { this.btnOkText = text; return this;}
}

package com.androthink.materials.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.androthink.materials.R;
import com.androthink.materials.callback.InputDialogCallback;
import com.androthink.materials.databinding.DialogInputBinding;

public class InputDialog extends Dialog {

    private int inputType;
    private String defaultString;
    private String hintString;
    private InputDialogCallback callback;

    private DialogInputBinding binding;

    public class InputTypes {
        public static final int NUMBERS = InputType.TYPE_CLASS_NUMBER;
        public static final int TEXT = InputType.TYPE_CLASS_TEXT;
    }

    @NonNull
    public static InputDialog getInstance(Context context, int inputType, InputDialogCallback callback) {
        return new InputDialog(context, inputType, callback);
    }

    private InputDialog(@NonNull Context context, int inputType, InputDialogCallback callback) {
        super(context);

        this.inputType = inputType;
        this.callback = callback;
    }

    public InputDialog setHintString(String hintString) {
        this.hintString = hintString;
        return this;
    }

    public InputDialog setDefaultValue(String defaultValue) {
        this.defaultString = defaultValue;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null) {
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.dialog_input, null, false);
        setContentView(binding.getRoot());

        setCancelable(false);

        binding.etData.setInputType(inputType);
        binding.etData.setText(("" + defaultString));

        binding.etData.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(binding.etData, InputMethodManager.SHOW_IMPLICIT);

        binding.inputData.setHint("" + hintString);

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etData.getText().length() < 1) {
                    binding.etData.setError("" + hintString);
                    return;
                }
                callback.onData(binding.etData.getText().toString());
                InputDialog.this.dismiss();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.this.dismiss();
            }
        });

        //Fonts.apply(getContext(), findViewById(android.R.id.content));
    }
}

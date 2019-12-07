package com.androthink.materials.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androthink.materials.R;
import com.androthink.materials.adapter.OptionsAdapter;
import com.androthink.materials.databinding.DialogOptionsBinding;
import com.androthink.materials.models.OptionModel;

import java.util.List;

public class OptionsDialog extends Dialog {

    private List<OptionModel> optionsList;

    @NonNull
    public static OptionsDialog getInstance(Context context, List<OptionModel> optionsList){
        return new OptionsDialog(context,optionsList);
    }

    private OptionsDialog(@NonNull Context context,List<OptionModel> optionsList) {
        super(context);

        this.optionsList = optionsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null) {
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        DialogOptionsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.dialog_options, null, false);
        setContentView(binding.getRoot());

        setCancelable(false);

        binding.rvOptions.setItemAnimator(new DefaultItemAnimator());
        binding.rvOptions.setLayoutManager(new LinearLayoutManager(getContext()));

        OptionsAdapter adapter = new OptionsAdapter(optionsList);
        binding.rvOptions.setAdapter(adapter);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsDialog.this.dismiss();
            }
        });

        //Fonts.apply(getContext(), findViewById(android.R.id.content));
    }
}

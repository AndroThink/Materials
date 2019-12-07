package com.androthink.materials.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.androthink.materials.R;
import com.androthink.materials.databinding.LayoutSpinnerItemBinding;
import com.androthink.materials.models.SpinnerModel;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private final List<SpinnerModel> spinnerModelList;

    public SpinnerAdapter(List<SpinnerModel> spinnerModelList) {
        this.spinnerModelList = spinnerModelList;
    }

    @Override
    public int getCount() {
        return spinnerModelList.size();
    }

    @Override
    public SpinnerModel getItem(int i) {
        if (i != -1 && i < spinnerModelList.size()) {
            return spinnerModelList.get(i);
        }
        return null;
    }

    public int getItemIndexById(int id) {

        if(id == 0)
            return -1;

        for (int i = 0; i < spinnerModelList.size(); i++) {
            if(spinnerModelList.get(i).getId() == id)
                return i;
        }

        return -1;
    }

    @Override
    public long getItemId(int i) {
        if (i != -1 && i < spinnerModelList.size()) {
            return spinnerModelList.get(i).getId();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SpinnerViewHolder holder;

        if (view == null) {
            LayoutSpinnerItemBinding binding = DataBindingUtil
                    .inflate(LayoutInflater.from(viewGroup.getContext()),
                            R.layout.layout_spinner_item, viewGroup, false);

            view = binding.getRoot();
            holder = new SpinnerViewHolder(binding);
            view.setTag(holder);
        } else {
            holder = (SpinnerViewHolder) view.getTag();
        }

        holder.bind(spinnerModelList.get(i));
        return view;
    }

    public int getPosition(String name) {

        for (int i = 0; i < spinnerModelList.size(); i++) {
            if (spinnerModelList.get(i).getName().trim().equals(name.trim()))
                return i;
        }

        return -1;
    }

    @Override
    public boolean isEnabled(int position) {
        return spinnerModelList.get(position).isSelectable();
    }

    private class SpinnerViewHolder {

        private final LayoutSpinnerItemBinding spinnerItemBinding;

        SpinnerViewHolder(LayoutSpinnerItemBinding layoutSpinnerItemBinding) {
            this.spinnerItemBinding = layoutSpinnerItemBinding;
        }

        void bind(@NonNull SpinnerModel data) {
            spinnerItemBinding.tvTitle.setText(data.getName());

            if (data.getDrawable() != null) {
                spinnerItemBinding.imgIcon.setImageDrawable(data.getDrawable());
            } else {
                spinnerItemBinding.imgIcon.setVisibility(View.GONE);
            }

            if (!data.isSelectable()) {
                //Set the disable spinner item color fade .
                spinnerItemBinding.tvTitle.setTextColor(spinnerItemBinding.getRoot().getContext().getResources().getColor(R.color.spinnerDisabledFontColor));
            } else {
                spinnerItemBinding.tvTitle.setTextColor(spinnerItemBinding.getRoot().getContext().getResources().getColor(R.color.spinnerFontColor));
            }
        }
    }
}
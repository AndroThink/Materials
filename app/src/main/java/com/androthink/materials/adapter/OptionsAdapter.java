package com.androthink.materials.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.androthink.materials.R;
import com.androthink.materials.databinding.LayoutOptionItemBinding;
import com.androthink.materials.models.OptionModel;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionViewHolder> {

    private List<OptionModel> dataList;

    public OptionsAdapter(List<OptionModel> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, final int position) {
        holder.bind(this.dataList.get(position));
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutOptionItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_option_item, parent, false);

        return new OptionViewHolder(itemBinding);
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOptionItemBinding binding;

        OptionViewHolder(@NonNull LayoutOptionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull OptionModel data) {
            binding.btnOption.setText(data.getOption());
            binding.btnOption.setOnClickListener(data.getCallback());
        }
    }
}
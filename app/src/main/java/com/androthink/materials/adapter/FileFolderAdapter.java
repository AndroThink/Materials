package com.androthink.materials.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.androthink.materials.R;
import com.androthink.materials.callback.FileFolderCallback;
import com.androthink.materials.databinding.LayoutFileFolderItemBinding;
import com.androthink.materials.models.FileFolderModel;

import java.util.ArrayList;


public class FileFolderAdapter extends RecyclerView.Adapter<FileFolderAdapter.FileFolderViewHolder> {

    private FileFolderCallback callback;
    private ArrayList<FileFolderModel> dataList;

    public FileFolderAdapter(ArrayList<FileFolderModel> dataList, FileFolderCallback callback) {
        this.dataList = dataList;
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull FileFolderViewHolder holder, final int position) {
        holder.bind(this.dataList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onFileFolderClicked(position);
            }
        });
    }

    @NonNull
    @Override
    public FileFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutFileFolderItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_file_folder_item, parent, false);

        return new FileFolderViewHolder(itemBinding);
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    class FileFolderViewHolder extends RecyclerView.ViewHolder {

        private final LayoutFileFolderItemBinding binding;

        FileFolderViewHolder(@NonNull LayoutFileFolderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull FileFolderModel data) {
            if (data.isFolder())
                binding.imgIcon.setImageResource(R.drawable.fp_folder);
            else
                binding.imgIcon.setImageResource(R.drawable.fp_file);

            binding.tvName.setText(data.getName());
        }
    }
}
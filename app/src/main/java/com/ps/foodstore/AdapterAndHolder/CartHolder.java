package com.ps.foodstore.AdapterAndHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ps.foodstore.databinding.RowLayoutCartBinding;

public class CartHolder extends RecyclerView.ViewHolder {

    RowLayoutCartBinding binding;

    public CartHolder(@NonNull RowLayoutCartBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

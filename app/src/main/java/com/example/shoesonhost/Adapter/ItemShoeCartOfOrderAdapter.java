package com.example.shoesonhost.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesonhost.Model.ShoesCart;
import com.example.shoesonhost.databinding.ItemShoesoforderBinding;

import java.util.ArrayList;

public class ItemShoeCartOfOrderAdapter extends RecyclerView.Adapter<ItemShoeCartOfOrderAdapter.ViewHolder> {
    ArrayList<ShoesCart> list;

    public ItemShoeCartOfOrderAdapter(ArrayList<ShoesCart> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemShoeCartOfOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShoesoforderBinding binding=ItemShoesoforderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemShoeCartOfOrderAdapter.ViewHolder holder, int position) {
        ShoesCart shoesCart=list.get(position);

        holder.binding.tvId.setText(shoesCart.getId());
        holder.binding.tvPrice.setText(shoesCart.getPriceSell()+"");
        holder.binding.tvQuantity.setText(shoesCart.getQuantity()+"");
        holder.binding.tvTotal.setText(String.valueOf(shoesCart.getQuantity()*shoesCart.getPriceSell()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemShoesoforderBinding binding;
        public ViewHolder(ItemShoesoforderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}

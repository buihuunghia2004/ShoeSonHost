package com.example.shoesonhost.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesonhost.Fragments.GoodsFragment;
import com.example.shoesonhost.MainActivity;
import com.example.shoesonhost.Model.Shoes;
import com.example.shoesonhost.databinding.ItemShoesGoodsBinding;

import java.util.ArrayList;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.Viewholder> {
    ArrayList<Shoes> list;
    Context context;

    public GoodsAdapter(ArrayList<Shoes> list,Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public GoodsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShoesGoodsBinding binding=ItemShoesGoodsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsAdapter.Viewholder holder, int position) {
        Shoes shoes=list.get(position);

        Glide.with(context).load(Uri.parse(shoes.getLinkImg())).into(holder.binding.imgShoes);
        holder.binding.tvName.setText(shoes.getName());
        holder.binding.tvId.setText(shoes.getId());
        holder.binding.tvPrice.setText(String.valueOf(shoes.getPriceSell()));
        holder.binding.tvQuantity.setText(String.valueOf(shoes.getSaq().getTotalSizeHM()));

        //event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).detailsShoes(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity) context).showDialogDeleteShoes(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemShoesGoodsBinding binding;
        public Viewholder(ItemShoesGoodsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

package com.example.shoesonhost.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesonhost.Activities.ChooseAndAddOptionActivity;
import com.example.shoesonhost.Model.Brand;
import com.example.shoesonhost.databinding.ItemBrandBinding;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    Context context;
    ArrayList<Brand> list;
    public BrandAdapter(ArrayList<Brand> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ItemBrandBinding binding=ItemBrandBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.ViewHolder holder, int position) {
        Brand brand=list.get(position);
        holder.binding.tvName.setText(brand.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(">>>>", "onClick: click");
                ((ChooseAndAddOptionActivity) context).resultSelectBrandLaucher(brand.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBrandBinding binding;
        public ViewHolder(ItemBrandBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

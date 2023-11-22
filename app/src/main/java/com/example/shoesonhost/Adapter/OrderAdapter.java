package com.example.shoesonhost.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesonhost.FirebaseDatabase.MyFDB;
import com.example.shoesonhost.Model.Orders;
import com.example.shoesonhost.Model.ShoesCart;
import com.example.shoesonhost.databinding.ItemConfirmOrderBinding;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<Orders> list;

    public OrderAdapter(ArrayList<Orders> list) {
        this.list = list;

    }

    Context context;
    ArrayList<ShoesCart> listShoeCart=new ArrayList<>();
    ItemShoeCartOfOrderAdapter itemAdapter=new ItemShoeCartOfOrderAdapter(listShoeCart);

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        ItemConfirmOrderBinding binding=ItemConfirmOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Orders orders=list.get(position);
        listShoeCart.clear();
        listShoeCart.addAll(orders.getListShoesCarts());

        int total=0;
        for (ShoesCart shoesCart:listShoeCart){
            total+=shoesCart.getQuantity()*shoesCart.getPriceSell();
        }

        holder.binding.tvTotal.setText(total+" VNĐ");
        holder.binding.rcvShoeOfOrder.setAdapter(itemAdapter);
        holder.binding.rcvShoeOfOrder.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.tvId.setText(orders.getId());

        if (orders.getStatus()==1){
            holder.binding.btnDelivery.setVisibility(View.GONE);
            holder.binding.btnCancel.setVisibility(View.VISIBLE);
            holder.binding.btnConfirm.setVisibility(View.VISIBLE);
        }else if (orders.getStatus()==2){
            holder.binding.btnDelivery.setVisibility(View.VISIBLE);
            holder.binding.btnCancel.setVisibility(View.GONE);
            holder.binding.btnConfirm.setVisibility(View.GONE);
        }else{
            holder.binding.btnDelivery.setVisibility(View.GONE);
            holder.binding.btnCancel.setVisibility(View.GONE);
            holder.binding.btnConfirm.setVisibility(View.GONE);
        }

        holder.binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFDB.OrderFDB.setStatusOrder(orders.getId(),orders.getStatus());
            }
        });
        holder.binding.btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFDB.OrderFDB.setStatusOrder(orders.getId(),orders.getStatus());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemConfirmOrderBinding binding;
        public ViewHolder(ItemConfirmOrderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

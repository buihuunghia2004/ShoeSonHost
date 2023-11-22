package com.example.shoesonhost.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesonhost.Model.User;
import com.example.shoesonhost.databinding.ItemCustomerChatBinding;

import java.util.ArrayList;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ViewHolder> {
    Context context;
    ArrayList<User> list;

    public ChatUsersAdapter(ArrayList<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChatUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ItemCustomerChatBinding binding=ItemCustomerChatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUsersAdapter.ViewHolder holder, int position) {
        User user=list.get(position);
        Glide.with(context).load(user.getLinkImg()).into(holder.binding.imgAvatar);
        holder.binding.tvName.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCustomerChatBinding binding;
        public ViewHolder(ItemCustomerChatBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

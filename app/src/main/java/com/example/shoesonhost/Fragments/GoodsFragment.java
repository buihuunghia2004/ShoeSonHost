package com.example.shoesonhost.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoesonhost.Activities.AddShoesActivity;
import com.example.shoesonhost.Adapter.GoodsAdapter;
import com.example.shoesonhost.MainActivity;
import com.example.shoesonhost.Model.Shoes;
import com.example.shoesonhost.databinding.FragmentGoodsBinding;

import java.util.ArrayList;

public class GoodsFragment extends Fragment {
    private static final String ARG_LISTSHOES_PARAM = "listshoes";
    private ArrayList<Shoes> listShoes;
    private GoodsAdapter adapter;
    Context context;
    FragmentGoodsBinding binding;

    //material
    int countShoes;
    private static GoodsFragment instance;

    public GoodsFragment() {
        // Required empty public constructor
    }
    public static GoodsFragment newInstance(ArrayList<Shoes> listShoes) {
        if (instance == null){
            instance=new GoodsFragment();
        }

        Bundle args = new Bundle();
        args.putSerializable(ARG_LISTSHOES_PARAM,listShoes);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listShoes= (ArrayList<Shoes>) getArguments().getSerializable(ARG_LISTSHOES_PARAM);
            countShoes=listShoes.size();
        }

        context=getContext();
        adapter=new GoodsAdapter(listShoes,context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentGoodsBinding.inflate(inflater);
        binding.rcvGoods.setAdapter(adapter);
        binding.rcvGoods.setLayoutManager(new LinearLayoutManager(context));
        binding.tvCount.setText(countShoes+" hàng hóa");
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddShoesActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type",0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void getData(){
        if (getArguments() != null) {
            listShoes= (ArrayList<Shoes>) getArguments().getSerializable(ARG_LISTSHOES_PARAM);
            countShoes=listShoes.size();
            adapter.notifyDataSetChanged();
            binding.tvCount.setText(listShoes.size()+" hàng hóa");
        }
    }
}
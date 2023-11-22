package com.example.shoesonhost.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shoesonhost.Adapter.OrderAdapter;
import com.example.shoesonhost.FirebaseDatabase.MyFDB;
import com.example.shoesonhost.Model.Orders;
import com.example.shoesonhost.R;
import com.example.shoesonhost.databinding.FragmentOrdersBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    FragmentOrdersBinding binding;
    ArrayList<Orders> list;
    ArrayList<Orders> listStamp;
    OrderAdapter adapter;
    public OrdersFragment() {
        // Required empty public constructor
    }
    private static OrdersFragment instance;
    public static OrdersFragment getInstance(ArrayList<Orders> list) {
        if (instance==null){
             instance= new OrdersFragment();
        }
        Bundle args = new Bundle();
        args.putSerializable("list",list);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
        if (getArguments() != null) {
            list= (ArrayList<Orders>) getArguments().getSerializable("list");
        }
        listStamp=getListStamp(1);
        adapter=new OrderAdapter(listStamp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentOrdersBinding.inflate(inflater);

        binding.rcv.setAdapter(adapter);
        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.tly.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        listStamp.clear();
                        listStamp.addAll(getListStamp(1));
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        listStamp.clear();
                        listStamp.addAll(getListStamp(2));
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        listStamp.clear();
                        listStamp.addAll(getListStamp(3));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        MyFDB.OrderFDB.crOrder.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                QueryDocumentSnapshot queryDocumentSnapshot= value.getDocumentChanges().get(0).getDocument();
                Orders orders=queryDocumentSnapshot.toObject(Orders.class);
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getId().equals(orders.getId())){
                        list.set(i,orders);
                        listStamp.clear();
                        listStamp.addAll(getListStamp((int) orders.getStatus()-1));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    private ArrayList<Orders> getListStamp(int type){
        ArrayList<Orders> listStamp=new ArrayList<>();
        for (Orders orders:list) {
            if (orders.getStatus() == type){
                listStamp.add(orders);
            }
        }
        return listStamp;
    }
}
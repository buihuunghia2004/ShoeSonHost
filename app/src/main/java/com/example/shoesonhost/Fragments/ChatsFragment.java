package com.example.shoesonhost.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoesonhost.Adapter.ChatUsersAdapter;
import com.example.shoesonhost.Model.User;
import com.example.shoesonhost.databinding.FragmentChatsBinding;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    ArrayList<User> listUser;
    ChatUsersAdapter adapter;
    FragmentChatsBinding binding;
    public ChatsFragment() {
        // Required empty public constructor
    }


    private static ChatsFragment instance;
    public static ChatsFragment newInstance(ArrayList<User> list) {
        if (instance==null){
            instance=new ChatsFragment();
        }
        Bundle args = new Bundle();
        args.putSerializable("list",list);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listUser=new ArrayList<>();

        if (getArguments() != null) {
            listUser= (ArrayList<User>) getArguments().getSerializable("list");
        }
        adapter=new ChatUsersAdapter(listUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(inflater);

        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcv.setAdapter(adapter);

        return binding.getRoot();
    }
}
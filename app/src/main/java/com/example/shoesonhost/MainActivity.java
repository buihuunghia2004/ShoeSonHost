package com.example.shoesonhost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shoesonhost.Activities.AddShoesActivity;
import com.example.shoesonhost.FirebaseDatabase.MyFDB;
import com.example.shoesonhost.Fragments.ChatsFragment;
import com.example.shoesonhost.Fragments.GoodsFragment;
import com.example.shoesonhost.Fragments.OrdersFragment;
import com.example.shoesonhost.Model.Chats;
import com.example.shoesonhost.Model.Orders;
import com.example.shoesonhost.Model.SAQ;
import com.example.shoesonhost.Model.Shoes;
import com.example.shoesonhost.Model.User;
import  com.example.shoesonhost.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    MyFDB.ShoeFDB.IGetShoesCallBack getShoesCallBack;
    ArrayList<Orders> listOrder;
    ArrayList<Shoes> listShoes;
    ArrayList<User> listUserChat;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    GoodsFragment goodsFragment;
    ChatsFragment chatsFragment;
    OrdersFragment ordersFragment;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(MainActivity.this);

        listShoes=new ArrayList<>();
        listOrder=new ArrayList<>();
        listUserChat=new ArrayList<>();

        goodsFragment=GoodsFragment.newInstance(listShoes);
        ordersFragment=OrdersFragment.getInstance(listOrder);
        chatsFragment=ChatsFragment.newInstance(listUserChat);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameMain.getId(),goodsFragment);
        fragmentTransaction.commit();

        getShoesCallBack=new MyFDB.ShoeFDB.IGetShoesCallBack() {
            @Override
            public void onSuccess(ArrayList<Shoes> list) {
                listShoes.clear();
                listShoes.addAll(list);
                goodsFragment=GoodsFragment.newInstance(listShoes);
                goodsFragment.getData();

            }

            @Override
            public void onFailure(String errorMesage) {

            }
        };
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                int id_item_goods=R.id.item_home;
                int id_item_order=R.id.item_order;
                int id_item_chats=R.id.item_chats;

                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                if (id==id_item_goods){
                    goodsFragment=GoodsFragment.newInstance(listShoes);
                    fragmentTransaction.replace(binding.frameMain.getId(),goodsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (id == id_item_order){
                    ordersFragment=OrdersFragment.getInstance(listOrder);
                    fragmentTransaction.replace(binding.frameMain.getId(),ordersFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (id == id_item_chats){
                    chatsFragment=ChatsFragment.newInstance(listUserChat);
                    fragmentTransaction.replace(binding.frameMain.getId(),chatsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }
    private void loadData(){
        showProcessDialog();

        MyFDB.ShoeFDB.getAllShoes(getShoesCallBack);
        MyFDB.OrderFDB.getAllOrder(new MyFDB.OrderFDB.IGetAllOrder() {
            @Override
            public void onSuccess(ArrayList<Orders> list) {
                listOrder.clear();
                listOrder.addAll(list);
            }

            @Override
            public void onFailure(String erorrMessage) {

            }
        });
        MyFDB.ChatsFDB.getAllChat(new MyFDB.ChatsFDB.IGetAllChats() {
            @Override
            public void onSuccess(ArrayList<Chats> list) {
                for (Chats chats:list){
                    MyFDB.UsersFDB.getChatUserByUid(chats.getId(), new MyFDB.UsersFDB.IGetUserByUid() {
                        @Override
                        public void onSuccess(User user) {
                            listUserChat.add(user);
                        }
                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
                progressDialog.dismiss();
            }
        });
    }

    //goodfragments
    private void showProcessDialog()
    {
        progressDialog.setTitle("HowKteam");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }
    public void detailsShoes(int position)//chuyển activity (fragment goods)
    {
        Intent intent=new Intent(MainActivity.this, AddShoesActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("type",1);
        bundle.putSerializable("shoes",listShoes.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void showDialogDeleteShoes(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tiêu đề hộp thoại")
                .setMessage("Nội dung hộp thoại")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        // Xử lý khi người dùng nhấn nút Đồng ý
                        showProcessDialog();
                        MyFDB.ShoeFDB.deleteShoes(listShoes.get(position).getId(), new MyFDB.IAddCallBack() {
                            @Override
                            public void onSuccess(boolean b) {
                                listShoes.remove(position);
                                goodsFragment=GoodsFragment.newInstance(listShoes);
                                goodsFragment.getData();
                                Log.d(">>>>>>", "onSuccess: kkk"+listShoes.size());

                                progressDialog.dismiss();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng nhấn nút Hủy bỏ
                        dialog.dismiss();
                        Log.d(">>>>>>", "onSuccess: kk2 "+listShoes.size());
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


}
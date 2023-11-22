package com.example.shoesonhost.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shoesonhost.Adapter.BrandAdapter;
import com.example.shoesonhost.FirebaseDatabase.MyFDB;
import com.example.shoesonhost.Model.Brand;
import com.example.shoesonhost.databinding.ActivityChooseAndAddOptionBinding;
import com.example.shoesonhost.databinding.DialogOneEdittextBinding;
import com.google.common.base.Objects;

import java.util.ArrayList;

public class ChooseAndAddOptionActivity extends AppCompatActivity {
    private int type;
    //0 -> thêm thương hiệu
    private static final int TYPE_BRAND=0;
    String title;
    ActivityChooseAndAddOptionBinding binding;
    //recyclerview
    ArrayList<Brand> listBrand;
    BrandAdapter brandAdapter;
    //process dialog
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChooseAndAddOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        if (intent!=null){
            type=intent.getIntExtra("type",0);
        }

        switch (type){
            case TYPE_BRAND:title="Thêm thương hiệu";
        }

        binding.tvTitle.setText(title);

        //recyclerview
        listBrand=new ArrayList<>();
        listBrand.add(new Brand("nikef","nike"));
        brandAdapter=new BrandAdapter(listBrand);
        binding.rcv.setLayoutManager(new LinearLayoutManager(this));
        binding.rcv.setAdapter(brandAdapter);

    }
    @Override
    protected void onResume() {
        super.onResume();
        upLoadData();
        //back
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //add
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Dialog dialog=new Dialog(ChooseAndAddOptionActivity.this);
                DialogOneEdittextBinding dialogBiding =DialogOneEdittextBinding.inflate(dialog.getLayoutInflater());
                dialog.setContentView(dialogBiding.getRoot());

                dialogBiding.tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogBiding.tvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id_name=dialogBiding.edtName.getText().toString();
                        if (!id_name.isEmpty()){
                            Brand brand=new Brand(id_name,id_name);
                            MyFDB.BrandFDB.addBrand(brand, new MyFDB.IAddCallBack() {
                                @Override
                                public void onSuccess(boolean b) {
                                    Toast.makeText(ChooseAndAddOptionActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    upLoadData();
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            Toast.makeText(ChooseAndAddOptionActivity.this, "Vui lòng nhập thương hiệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
    private void showProcessDialog()
    {
        progressDialog = new ProgressDialog(ChooseAndAddOptionActivity.this);
        progressDialog.setTitle("HowKteam");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }
    private void upLoadData()
    {
        showProcessDialog();
        listBrand.clear();
        MyFDB.BrandFDB.getAllShoes(new MyFDB.IGetAllObjectCallBack() {
            @Override
            public void onSuccess(ArrayList<Object> list) {
                for (Object object:list){
                    listBrand.add((Brand) object);
                }
                progressDialog.dismiss();
                brandAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(String errorMesage) {

            }
        });
    }
    public void resultSelectBrandLaucher(String brand)
    {
        Intent intent=new Intent();
        intent.putExtra("brand",brand);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
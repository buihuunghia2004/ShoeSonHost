package com.example.shoesonhost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.shoesonhost.FirebaseDatabase.MyFDB;
import com.example.shoesonhost.Model.SAQ;
import com.example.shoesonhost.Model.Shoes;
import com.example.shoesonhost.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MyFDB.UsersFDB.getChatUserByUid("NwTZPmLjPtbeJmMtkqOcOH7q0o33", new MyFDB.UsersFDB.IGetUserByUid() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
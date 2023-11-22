package com.example.shoesonhost.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoesonhost.FirebaseDatabase.MyFDB;
import com.example.shoesonhost.Model.SAQ;
import com.example.shoesonhost.Model.Shoes;
import com.example.shoesonhost.databinding.ActivityAddShoesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddShoesActivity extends AppCompatActivity {
    int type;
    static final int TYPE_ADD=0;
    static final int TYPE_EDIT=1;
    static final
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    Uri imgUri;
    String linkImg;
    private ActivityResultLauncher<Intent> selectImgLauncher;
    private ActivityResultLauncher<Intent> selectBrandLauncher;
    String brand;
    int total=0;
    boolean bSAQ=false;
    EditText[] arrEdtSize;
    TextWatcher textWatcher;
    ProgressDialog progressDialog;
    //edit
    Shoes shoes;
    ActivityAddShoesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddShoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //add or update
        Intent intent=getIntent();
        if (intent!=null){
            Bundle bundle=intent.getExtras();
            if (bundle!=null){
                type=bundle.getInt("type",TYPE_ADD);
                if (type == TYPE_EDIT){
                    shoes= (Shoes) bundle.getSerializable("shoes");
                    setAperiance(shoes);
                }
            }
        }

        //launcher chọn ảnh
        selectImgLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imgUri=data.getData();
                            binding.imgShoes.setImageURI(imgUri);
                        }
                    }
                });
        //launcher chọn brand
        selectBrandLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data!=null){
                                brand=data.getStringExtra("brand");
                                binding.tvBrand.setText(brand);
                            }
                        }
                    }
                });

        //khởi tạo mảng size và số lượng
        arrEdtSize= new EditText[]{
                binding.edtQuantity35,
                binding.edtQuantity36,
                binding.edtQuantity37,
                binding.edtQuantity38,
                binding.edtQuantity39,
                binding.edtQuantity40,
                binding.edtQuantity41,
                binding.edtQuantity42,
                binding.edtQuantity43
        };

        //biến lắng nghe thay đổi của SAQ
        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTotalSize();
            }
        };

        //process dialog
        progressDialog=new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //thoat
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //chọn ảnh
        binding.imgShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                selectImgLauncher.launch(intent);
            }
        });

        //chọn thương hiệu
        binding.tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddShoesActivity.this, ChooseAndAddOptionActivity.class);
                intent.putExtra("type",0);
                selectBrandLauncher.launch(intent);
            }
        });

        //save
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Đẩy hình ảnh lên fireStorage
                if (checkInfo()){
                    if (type == TYPE_ADD){
                        showProcessDialog();
                        storageRef= storage.getReference().child("imgShoes/"+binding.edtId.getText().toString()+".png");
                        UploadTask uploadTask=storageRef.putFile(imgUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        linkImg=uri.toString();

                                        Shoes shoes=new Shoes(
                                                binding.edtId.getText().toString(),
                                                binding.edtName.getText().toString(),
                                                linkImg,
                                                Integer.parseInt(binding.edtPriceImport.getText().toString()),
                                                Integer.parseInt(binding.edtPriceSell.getText().toString()),
                                                binding.tvBrand.getText().toString()
                                        );

                                        SAQ saq=new SAQ(binding.edtId.getText().toString(),new HashMap<>());

                                        for (int i=0;i<arrEdtSize.length;i++){
                                            if (!arrEdtSize[i].getText().toString().isEmpty()){
                                                Log.d(">>>>>", "onSuccess: 7"+arrEdtSize[i].getText().toString());
                                                saq.putHashMapSize(String.valueOf(35+i),Integer.parseInt(arrEdtSize[i].getText().toString()));
                                            }
                                        }

                                        shoes.setSaq(saq);

                                        MyFDB.ShoeFDB.addShoe(shoes, new MyFDB.IAddCallBack() {
                                            @Override
                                            public void onSuccess(boolean b) {
                                                Toast.makeText(AddShoesActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }else{
                        updateShoes();
                    }
                }
            }
        });

        //lắng nghe số size
        for (EditText editText:arrEdtSize){
            editText.addTextChangedListener(textWatcher);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        //hủy lắng nghe
        for (EditText editText:arrEdtSize){
            editText.removeTextChangedListener(textWatcher);
        }
    }
    private void setTotalSize(){
        total=0;
        for (EditText editText:arrEdtSize){
            if (!editText.getText().toString().isEmpty()){
                int i=Integer.parseInt(editText.getText().toString());
                total+=i;
                bSAQ=true;
            }
        }
        binding.tvSum.setText(String.valueOf(total));
    }
    private boolean checkInfo(){
        boolean b=true;

        if (imgUri ==null ){
            if (type == TYPE_ADD){
                Toast.makeText(this, "Vui lòng chọn ảnh mô tả sản phẩm", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if(binding.edtId.getText().toString().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập mã hàng", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.edtName.getText().toString().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập tên hàng", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.tvBrand.getText().toString().isEmpty()){
            Toast.makeText(this, "Vui lòng chọn thương hiệu", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.edtPriceImport.getText().toString().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập giá nhập hàng", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.edtPriceSell.getText().toString().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập giá bán hàng", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!bSAQ){
            Toast.makeText(this, "Vui lòng số lượng hàng theo size", Toast.LENGTH_SHORT).show();
            return false;
        }

        return b;
    };
    private void showProcessDialog()
    {
        progressDialog.setTitle("HowKteam");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }
    private void setAperiance(Shoes shoes){
        Glide.with(this).load(shoes.getLinkImg()).into(binding.imgShoes);
        binding.edtId.setText(shoes.getId());
        binding.edtName.setText(shoes.getName());
        binding.tvBrand.setText(shoes.getIdBrand());
        binding.edtPriceImport.setText(String.valueOf(shoes.getPriceImport()));
        binding.edtPriceSell.setText(String.valueOf(shoes.getPriceSell()));

        total=0;

        for (Map.Entry<String, Integer> entry : shoes.getSaq().getHashMapSize().entrySet()) {
            String key=entry.getKey();
            int value=entry.getValue();
            switch (key){
                case "35":binding.edtQuantity35.setText(value+"");total+=value;break;
                case "36":binding.edtQuantity36.setText(value+"");total+=value;break;
                case "37":binding.edtQuantity37.setText(value+"");total+=value;break;
                case "38":binding.edtQuantity38.setText(value+"");total+=value;break;
                case "39":binding.edtQuantity39.setText(value+"");total+=value;break;
                case "40":binding.edtQuantity40.setText(value+"");total+=value;break;
                case "41":binding.edtQuantity41.setText(value+"");total+=value;break;
                case "42":binding.edtQuantity42.setText(value+"");total+=value;break;
                case "43":binding.edtQuantity43.setText(value+"");total+=value;break;
            }
        }

        binding.tvSum.setText(String.valueOf(total));
    }

    private void updateShoes(){
        String oldId,newId,name,brand;
        int priceImport,priceSell;

        oldId=shoes.getId();
        newId=binding.edtId.getText().toString();
        name= binding.edtName.getText().toString();
        priceImport=Integer.parseInt(binding.edtPriceImport.getText().toString());
        priceSell=Integer.parseInt(binding.edtPriceSell.getText().toString());
        brand= binding.tvBrand.getText().toString();

        Shoes newShoe=new Shoes(newId,name,linkImg,priceImport,priceSell,brand);

        SAQ saq=new SAQ(binding.edtId.getText().toString(),new HashMap<>());

        for (int i=0;i<arrEdtSize.length;i++){
            if (!arrEdtSize[i].getText().toString().isEmpty()){
                Log.d(">>>>>", "onSuccess: 7"+arrEdtSize[i].getText().toString());
                saq.putHashMapSize(String.valueOf(35+i),Integer.parseInt(arrEdtSize[i].getText().toString()));
            }
        }
        newShoe.setSaq(saq);


        if (imgUri == null){
            showProcessDialog();
            newShoe.setLinkImg(shoes.getLinkImg());
            MyFDB.ShoeFDB.updateShoes(oldId, newShoe, new MyFDB.IAddCallBack() {
                @Override
                public void onSuccess(boolean b) {
                    progressDialog.dismiss();
                }
            });
        }else{
            showProcessDialog();
            MyFDB.ImageFDB.updateImageShoes(oldId, newId, imgUri, new MyFDB.ImageFDB.IAddImageComplete() {
                @Override
                public void onSuccess(String linkImage) {
                    newShoe.setLinkImg(linkImg);
                    MyFDB.ShoeFDB.updateShoes(oldId, newShoe, new MyFDB.IAddCallBack() {
                        @Override
                        public void onSuccess(boolean b) {
                            progressDialog.dismiss();
                            Toast.makeText(AddShoesActivity.this, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }

    }
}
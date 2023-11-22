package com.example.shoesonhost.FirebaseDatabase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.shoesonhost.Model.Brand;
import com.example.shoesonhost.Model.Chats;
import com.example.shoesonhost.Model.Orders;
import com.example.shoesonhost.Model.SAQ;
import com.example.shoesonhost.Model.Shoes;
import com.example.shoesonhost.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFDB {
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    public static class ImageFDB{
        private static StorageReference storageRef;
        public static void addImageShoes(String nameImage,Uri imgUri,IAddImageComplete callback){
            storageRef= storage.getReference().child("imgShoes/"+nameImage+".png");
            UploadTask uploadTask=storageRef.putFile(imgUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            callback.onSuccess(uri.toString());
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(e.getMessage());
                }
            })
            ;
        }
        public static void deleteImageShoes(String nameImage,IDeleteImageComplete callback){
            storageRef= storage.getReference().child("imgShoes/"+nameImage+".png");
            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    callback.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(e.getMessage());
                }
            });
        }
        public static void updateImageShoes(String nameImage,String newNameImage,Uri uriImage,IAddImageComplete callback){
            deleteImageShoes(nameImage, new IDeleteImageComplete() {
                @Override
                public void onSuccess() {
                    addImageShoes(newNameImage, uriImage, new IAddImageComplete() {
                        @Override
                        public void onSuccess(String linkImage) {
                            callback.onSuccess(linkImage.toString());
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                            callback.onFailure(errorMessage);
                        }
                    });
                }
                @Override
                public void onFailure(String erorrMessage) {

                }
            });
        }
        public interface IAddImageComplete{
            void onSuccess(String linkImage);
            void onFailure(String errorMessage);
        }
        public interface IDeleteImageComplete{
            void onSuccess();
            void onFailure(String erorrMessage);
        }
    }
    public static class ShoeFDB{
        private static CollectionReference crShoe = db.collection("products");
        public static void addShoe(Shoes shoe,IAddCallBack callBack){
            // Add a new document with a generated ID
            crShoe.document(shoe.getId())
                    .set(shoe)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callBack.onSuccess(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.onSuccess(false);
                        }
                    });
        }
        public static void updateShoes(String oldId,Shoes newShoes,IAddCallBack callBack){
            deleteShoes(oldId, new IAddCallBack() {
                @Override
                public void onSuccess(boolean b) {
                    if (b){
                        addShoe(newShoes, new IAddCallBack() {
                            @Override
                            public void onSuccess(boolean b) {
                                if (b){
                                    callBack.onSuccess(true);
                                }else{
                                    callBack.onSuccess(false);
                                }
                            }
                        });
                    }
                }
            });
        }
        public static void deleteShoes(String id,IAddCallBack callBack){
            crShoe.document(id).delete()
                    .addOnSuccessListener(aVoid -> {
                        callBack.onSuccess(true);
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý khi xóa thất bại
                        callBack.onSuccess(false);
                    });
        }
        public static void getListIdShoe(final IGetIdShoesCallBack callback) {
            // Lấy dữ liệu từ Firebase Realtime Database
            crShoe.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<String> listId=new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(">>>>>", "onComplete: r"+document.getId());
                                    String productCode = document.getId();
                                    listId.add(productCode);
                                }
                                callback.onSuccess(listId);
                            } else {
                                callback.onFailure("erorr");
                                Log.w(">>>>", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        public static void getAllShoes(final IGetShoesCallBack callback) {
            // Lấy dữ liệu từ Firebase Realtime Database
            crShoe.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Shoes> list=new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Shoes shoes=document.toObject(Shoes.class);
                                    Log.d(">>>>>>", "onComplete: "+shoes.getSaq().getHashMapSize());
                                    list.add(shoes);
                                }
                                callback.onSuccess(list);
                            } else {
                                callback.onFailure("erorr");
                                Log.w(">>>>", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        public interface IGetIdShoesCallBack{
            void onSuccess(ArrayList<String> listId);
            void onFailure(String errorMesage);
        }
        public interface IGetShoesCallBack{
            void onSuccess(ArrayList<Shoes> list);
            void onFailure(String errorMesage);
        }
    }
    public static class BrandFDB {
        private static CollectionReference crBrand = db.collection("brands");

        public static void addBrand(Brand brand, IAddCallBack callBack) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", brand.getId());
            map.put("name", brand.getName());

            // Add a new document with a generated ID
            crBrand.document(brand.getId())
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callBack.onSuccess(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.onSuccess(false);
                        }
                    });
        }

        public static void getAllShoes(final IGetAllObjectCallBack callback) {
            // Lấy dữ liệu từ Firebase Realtime Database
            crBrand.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Object> list = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Brand brand = document.toObject(Brand.class);
                                    list.add(brand);
                                }
                                callback.onSuccess(list);
                            } else {
                                callback.onFailure("erorr");
                                Log.w(">>>>", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }
    public static class OrderFDB
    {
        public static CollectionReference crOrder = db.collection("orders");

        public static void addOrder(Orders orders, IAddCallBack callBack){
            // Add a new document with a generated ID]
            DocumentReference documentReference=crOrder.document();
            orders.setId(documentReference.getId());
            documentReference
                    .set(orders)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callBack.onSuccess(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.onSuccess(false);
                        }
                    });
        }

        public static void getAllOrder(final IGetAllOrder callback) {
            // Lấy dữ liệu từ Firebase Realtime Database
            crOrder.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Orders> list=new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Orders orders=document.toObject(Orders.class);
                                    list.add(orders);
                                }
                                callback.onSuccess(list);
                            } else {
                                callback.onFailure("erorr");
                                Log.w(">>>>", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }

        public static void deleteOrder(String id,IDeleteOrder callback){
            crOrder.document(id).
                    delete().
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        public static void setStatusOrder(String id,long curentStatus){
            crOrder.document(id).update("status",curentStatus+1);
        }

        public interface IGetAllOrder{
            void onSuccess(ArrayList<Orders> list);
            void onFailure(String erorrMessage);
        }
        public interface IDeleteOrder{
            void onSuccess();
            void onFailure(String erorrMessage);
        }
    }
    public static class UsersFDB{
        public static CollectionReference crUser = db.collection("users");
        public static void getUserByUid(String uid, IGetUserByUid callBack){
            // Add a new document with a generated ID]
            crUser.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user=documentSnapshot.toObject(User.class);
                            callBack.onSuccess(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.onFailure(e.getMessage());
                        }
                    });
        }
        public static void getChatUserByUid(String uid, IGetUserByUid callBack){
            // Add a new document with a generated ID]
            crUser.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String id= (String) documentSnapshot.get("id");
                            String name= (String) documentSnapshot.get("name");
                            String link= (String) documentSnapshot.get("linkImg");
                            User user=new User(id,name,link);
                            callBack.onSuccess(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.onFailure(e.getMessage());
                        }
                    });
        }
        public interface IGetUserByUid{
            void onSuccess(User user);
            void onFailure(String errorMessage);
        }
    }
    public static class ChatsFDB{
        public static CollectionReference crChats = db.collection("chats");
        public static void getAllChat(IGetAllChats callback){
            crChats.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        ArrayList<Chats> list=new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Chats chats=document.toObject(Chats.class);
                            list.add(chats);
                        }
                        callback.onSuccess(list);
                    }
                }
            });
        }
        public interface IGetAllChats{
            void onSuccess(ArrayList<Chats> list);
        }
    }
    public interface IAddCallBack {
        void onSuccess(boolean b);
    }
    public interface IGetAllObjectCallBack{
        void onSuccess(ArrayList<Object> list);
        void onFailure(String errorMesage);
    }
}

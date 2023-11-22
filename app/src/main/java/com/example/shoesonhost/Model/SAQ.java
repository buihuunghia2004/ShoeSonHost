package com.example.shoesonhost.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SAQ implements Serializable {
    private String id;
    HashMap<String,Integer> hashMapSize;

    public SAQ() {
    }

    public SAQ(String id, HashMap<String, Integer> hashMapSize) {
        this.id = id;
        this.hashMapSize = hashMapSize;
    }

    public SAQ(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Integer> getHashMapSize() {
        return hashMapSize;
    }

    public void setListSize(HashMap<String, Integer> hashMapSize) {
        this.hashMapSize = hashMapSize;
    }

    public void putHashMapSize(String key,Integer value){
        hashMapSize.put(key,value);
    }
    public int getHashMapSizeByKey(String key){
        return hashMapSize.get(key);
    }
    public int getTotalSizeHM(){
        int total=0;
        for (Map.Entry<String, Integer> entry : hashMapSize.entrySet()) {
            String key=entry.getKey();
            int value=entry.getValue();
            switch (key){
                case "35":total+=value;break;
                case "36":total+=value;break;
                case "37":total+=value;break;
                case "38":total+=value;break;
                case "39":total+=value;break;
                case "40":total+=value;break;
                case "41":total+=value;break;
                case "42":total+=value;break;
                case "43":total+=value;break;
            }
        }
        return total;
    }
}

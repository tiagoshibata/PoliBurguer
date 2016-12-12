package org.burguer.poli.poliburguer.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Order implements FirebaseModel {

    public ArrayList<String> products;
    public int price;
    public long timestamp;
    private String key;
    public String fcm;
    public String uid;

    public Order() {}

    public Order(int price, ArrayList<String> products, long timestamp, String fcm, String uid) {
        this.price = price;
        this.products = products;
        this.timestamp = timestamp;
        this.fcm = fcm;
        this.uid = uid;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFcm() {
        return fcm;
    }

    public String getUid() {
        return uid;
    }

    @Exclude
    public String getFormattedPrice() {
        return Money.format(price);
    }

    @Exclude
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

}

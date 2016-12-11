package org.burguer.poli.poliburguer.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Order implements FirebaseModel {

    public ArrayList<String> products;
    public int price;
    public long timestamp;
    private String key;

    public Order() {}

    public Order(int price, ArrayList<String> products, long timestamp) {
        this.price = price;
        this.products = products;
        this.timestamp = timestamp;
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

    @Exclude
    public String getFormattedPrice() {
        return "R$" + String.valueOf(price / 100) + "," + String.valueOf(price % 100);
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

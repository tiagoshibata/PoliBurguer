package org.burguer.poli.poliburguer.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product implements FirebaseModel {

    public String name;
    public String description;
    public int store;
    public int price;
    private String key;

    public Product() {}

    public Product(String name, String description, int store, int price) {
        this.name = name;
        this.description = description;
        this.store  = store;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    @Exclude
    public String getFormattedPrice() {
        return "R$" + String.valueOf(price / 100) + "," + String.valueOf(price % 100);
    }

}

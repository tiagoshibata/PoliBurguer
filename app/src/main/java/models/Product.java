package models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    public String name;
    public String description;
    public int store;
    public float price;

    public Product() {}

    public Product(String name, String description, int store, float price) {
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

    public String getFormattedPrice() {
        return "R$" + String.format("%.2f", price);
    }

}

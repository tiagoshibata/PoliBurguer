package org.burguer.poli.poliburguer.parcel;

import android.os.Parcel;
import android.os.Parcelable;

import org.burguer.poli.poliburguer.models.Product;

public class ProductParcel implements Parcelable {
    private Product product;

    public ProductParcel(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    protected ProductParcel(Parcel in) {
        product = new Product(in.readString(), in.readString(), in.readInt(), in.readInt());
        product.setKey(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(product.getName());
        out.writeString(product.getDescription());
        out.writeInt(product.getStore());
        out.writeInt(product.getPrice());
        out.writeString(product.getKey());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductParcel> CREATOR = new Creator<ProductParcel>() {
        @Override
        public ProductParcel createFromParcel(Parcel in) {
            return new ProductParcel(in);
        }

        @Override
        public ProductParcel[] newArray(int size) {
            return new ProductParcel[size];
        }
    };
}

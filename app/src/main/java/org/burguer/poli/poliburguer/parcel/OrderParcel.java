package org.burguer.poli.poliburguer.parcel;

import android.os.Parcel;
import android.os.Parcelable;

import org.burguer.poli.poliburguer.models.Order;

public class OrderParcel implements Parcelable {

    private Order order;

    public OrderParcel(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    protected OrderParcel(Parcel in) {
        order = new Order(in.readInt(), in.readArrayList(String.class.getClassLoader()), in.readLong(), in.readString(), in.readString());
        order.setKey(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(order.getPrice());
        out.writeList(order.getProducts());
        out.writeLong(order.getTimestamp());
        out.writeString(order.getFcm());
        out.writeString(order.getUid());
        out.writeString(order.getKey());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderParcel> CREATOR = new Creator<OrderParcel>() {
        @Override
        public OrderParcel createFromParcel(Parcel in) {
            return new OrderParcel(in);
        }

        @Override
        public OrderParcel[] newArray(int size) {
            return new OrderParcel[size];
        }
    };

}

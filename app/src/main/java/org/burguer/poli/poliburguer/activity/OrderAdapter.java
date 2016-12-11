package org.burguer.poli.poliburguer.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, android.R.layout.simple_list_item_2, orders);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Order order = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
        String products = getContext().getResources().getString(order.getProducts().size() == 1 ? R.string.lower_product : R.string.lower_products);
        text1.setText(order.getProducts().size() + " " + products  + " - " + order.getFormattedPrice());
        Date date = new Date(order.getTimestamp());
        text2.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
        return convertView;
    }

}

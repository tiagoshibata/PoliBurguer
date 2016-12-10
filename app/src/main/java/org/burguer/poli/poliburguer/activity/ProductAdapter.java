package org.burguer.poli.poliburguer.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import org.burguer.poli.poliburguer.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private List<Product> products;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence s) {
            FilterResults results = new FilterResults();
            ArrayList<Product> filtered = new ArrayList<>();

            String search = s.toString().toLowerCase();
            Log.i("PoliBurguer", "Filter " + search);
            if (s == null || s.length() == 0) {
                results.values = products;
                results.count = getCount();
            } else {
                for (Product p : products)
                    if (p.getDescription().toLowerCase().contains(search) || p.getName().toLowerCase().contains(search))
                        filtered.add(p);
                results.values = filtered;
                results.count = filtered.size();
            }
            Log.i("PoliBurguer", "Results " + results.count);
            return results;
        }

        @Override
        protected void publishResults(CharSequence s, FilterResults result) {
            setNotifyOnChange(false);
            clear();
            if (result.count > 0) {
                addAll((List<Product>)result.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
            setNotifyOnChange(true);
        }
    };

    public ProductAdapter(Context context, List<Product> products) {
        super(context, android.R.layout.simple_list_item_2);
        addAll(products);
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
        text1.setText(product.getName());
        text2.setText(product.getDescription() + " - " + product.getFormattedPrice());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

}

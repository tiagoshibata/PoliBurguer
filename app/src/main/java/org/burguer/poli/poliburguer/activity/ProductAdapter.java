package org.burguer.poli.poliburguer.activity;

import android.content.Context;
import android.support.annotation.NonNull;
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
        private String lastFilter = null;

        @Override
        protected FilterResults performFiltering(CharSequence s) {
            FilterResults results = new FilterResults();
            ArrayList<Product> filtered = new ArrayList<>();

            String search;
            if (s == null) {
                if (lastFilter != null)
                    search = lastFilter;
                else
                    search = "";
            } else {
                search = s.toString().toLowerCase();
            }
            lastFilter = search;
            if (search.length() == 0) {
                results.values = products;
                results.count = products.size();
            } else {
                for (Product p : products)
                    if (p.getDescription().toLowerCase().contains(search) || p.getName().toLowerCase().contains(search))
                        filtered.add(p);
                results.values = filtered;
                results.count = filtered.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence s, FilterResults result) {
            setNotifyOnChange(false);
            clear();
            if (result.count > 0) {
                addAll((List<Product>)result.values);
                ProductAdapter.super.notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public ProductAdapter(Context context, List<Product> products) {
        super(context, android.R.layout.simple_list_item_2);
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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
    @NonNull
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void notifyDataSetChanged() {
        getFilter().filter(null);
    }

}

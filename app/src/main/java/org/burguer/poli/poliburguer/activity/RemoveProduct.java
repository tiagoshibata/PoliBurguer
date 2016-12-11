package org.burguer.poli.poliburguer.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;

import java.util.ArrayList;
import java.util.List;

import org.burguer.poli.poliburguer.firebase.ListChildEventListener;
import org.burguer.poli.poliburguer.models.Product;
import org.burguer.poli.poliburguer.parcel.ProductParcel;

public class RemoveProduct extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    List<Product> productList = new ArrayList<>();
    ArrayList<ProductParcel> order = new ArrayList<>();
    private DatabaseReference products;
    private ProductAdapter adapter;
    private ListView productListView;
    private EditText buySearch;

    private ChildEventListener productListener = new ListChildEventListener<Product>(Product.class, productList) {
        @Override
        public void onUpdate() {
            adapter = new ProductAdapter(RemoveProduct.this, productList);
            adapter.getFilter().filter(buySearch.getText().toString());
            productListView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Toast.makeText(RemoveProduct.this, R.string.db_read_failed, Toast.LENGTH_LONG).show();
            Log.w(TAG, "Failed to read database:", error.toException());
        }
    };

    private AdapterView.OnItemClickListener mProductListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            products.child(productList.get(position).getKey()).removeValue();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product);

        productListView = (ListView)findViewById(R.id.product_list);
        productListView.setOnItemClickListener(mProductListClickListener);

        buySearch = (EditText)findViewById(R.id.buy_product_search);
        buySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        products = db.getReference("products");
        products.addChildEventListener(productListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        products.removeEventListener(productListener);
    }

}
package org.burguer.poli.poliburguer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;

import models.Product;

public class BuyProduct extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    ArrayAdapter<String> adapter;
    private FirebaseDatabase db;
    private DatabaseReference products;
    private ListView productList;
    private ChildEventListener productListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
            Log.d(TAG, "onChildAdded:" + snapshot.getKey());
            Product product = snapshot.getValue(Product.class);
            adapter.add(product.getName());
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
            String key = snapshot.getKey();
            Product product = snapshot.getValue(Product.class);
            Log.d(TAG, "onChildChanged:" + key);
        }

        @Override
        public void onChildRemoved(DataSnapshot snapshot) {
            String key = snapshot.getKey();
            Log.d(TAG, "onChildRemoved:" + key);
        }

        @Override
        public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            String key = snapshot.getKey();
            Log.d(TAG, "onChildMoved:" + key);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Toast.makeText(BuyProduct.this, R.string.db_read_failed, Toast.LENGTH_LONG).show();
            Log.w(TAG, "Failed to read database:", error.toException());
        }
    };

    private OnItemClickListener mProductListClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            /*Object o = lv.getItemAtPosition(position);
             write you handling code like...
            String st = "sdcard/";
            File f = new File(st+o.toString());
            // do whatever u want to do with 'f' File object
            */
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product);

        productList = (ListView)findViewById(R.id.product_list);
        productList.setOnItemClickListener(mProductListClickListener);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1);
        productList.setAdapter(adapter);

        db = FirebaseDatabase.getInstance();
        products = db.getReference("products");
    }

    @Override
    public void onStart() {
        super.onStart();
        products.addChildEventListener(productListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        products.removeEventListener(productListener);
    }

}

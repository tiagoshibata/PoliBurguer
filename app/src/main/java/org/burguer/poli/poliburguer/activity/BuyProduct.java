package org.burguer.poli.poliburguer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class BuyProduct extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    List<Product> productList = new ArrayList<>();
    List<Product> order = new ArrayList<>();
    private FirebaseDatabase db;
    private DatabaseReference products;
    private ListView productListView;

    private ChildEventListener productListener = new ListChildEventListener<Product>(Product.class, productList) {
        @Override
        public void onUpdate() {
            productListView.setAdapter(new ProductAdapter(BuyProduct.this, productList));
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
            order.add(productList.get(position));
            setTitle("Carrinho - " + order.size() + " itens");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_buy_order:
                startActivity(new Intent(BuyProduct.this, Login.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product);

        productListView = (ListView)findViewById(R.id.product_list);
        productListView.setOnItemClickListener(mProductListClickListener);

        db = FirebaseDatabase.getInstance();
        products = db.getReference("products");
        products.addChildEventListener(productListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        products.removeEventListener(productListener);
    }

}

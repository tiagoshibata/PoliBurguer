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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.models.Money;
import org.burguer.poli.poliburguer.models.Order;
import org.burguer.poli.poliburguer.models.Product;
import org.burguer.poli.poliburguer.parcel.ProductParcel;

import java.util.ArrayList;

public class Checkout extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    private ArrayList<Product> order;
    private ProductAdapter adapter;
    private FirebaseDatabase db;
    private DatabaseReference dbOrders;
    private boolean menuEnabled = true;
    private int price;

    private OnItemLongClickListener mOrderListClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            order.remove(position);
            adapter.notifyDataSetChanged();
            updatePrice();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ArrayList<ProductParcel> parcel = getIntent().getParcelableArrayListExtra("order");
        order = new ArrayList<>();
        for (ProductParcel p : parcel) {
            order.add(p.getProduct());
        }

        ListView orderListView = (ListView)findViewById(R.id.list_view_item_list);
        orderListView.setOnItemLongClickListener(mOrderListClickListener);
        adapter = new ProductAdapter(this, order);
        orderListView.setAdapter(adapter);
        updatePrice();

        db = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbOrders = db.getReference("users/" + uid + "/orders");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.checkout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_checkout:
                if (menuEnabled) {
                    if (order.isEmpty()) {
                        Toast.makeText(Checkout.this, R.string.checkout_empty, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    menuEnabled = false;
                    DatabaseReference ref = dbOrders.push();
                    ArrayList<String> productId = new ArrayList<>();
                    for (Product p : order) {
                        productId.add(p.getKey());
                    }
                    String fcm = FirebaseInstanceId.getInstance().getToken();
                    ref.setValue(new Order(price, productId, System.currentTimeMillis(), fcm), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Toast.makeText(Checkout.this, R.string.db_communication_failed, Toast.LENGTH_LONG).show();
                                Log.e(TAG, error.toString());
                                menuEnabled = true;
                                return;
                            }
                            startActivity(new Intent(Checkout.this, MainMenu.class));
                            Toast.makeText(Checkout.this, R.string.checkout_success, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePrice() {
        price = 0;
        for (Product p : order) {
            price += p.getPrice();
        }
        setTitle(getResources().getString(R.string.confirm_checkout) + " - " + Money.format(price));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, BuyProduct.class);
        ArrayList<ProductParcel> parcel = new ArrayList<>();
        for (Product p : order)
            parcel.add(new ProductParcel(p));
        intent.putExtra("order", parcel);
        finish();
        startActivity(intent);
    }

}

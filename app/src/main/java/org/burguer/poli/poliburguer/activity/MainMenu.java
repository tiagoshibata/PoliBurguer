package org.burguer.poli.poliburguer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.firebase.ListChildEventListener;
import org.burguer.poli.poliburguer.models.Order;
import org.burguer.poli.poliburguer.parcel.OrderParcel;

import java.util.ArrayList;
import java.util.Comparator;

public class MainMenu extends AppCompatActivity {

    private static final String TAG = "PoliBurger";
    private ArrayList<Order> orderList = new ArrayList<>();
    private OrderComparator orderComparator = new OrderComparator();
    private OrderAdapter adapter;
    private DatabaseReference orders;
    private FirebaseDatabase db;

    private ChildEventListener orderListener = new ListChildEventListener<Order>(Order.class, orderList) {
        @Override
        public void onUpdate() {
            adapter.notifyDataSetChanged();
            adapter.sort(orderComparator);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            //Toast.makeText(MainMenu.this, R.string.db_read_failed, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Failed to read database:", error.toException());
        }
    };

    private OnClickListener mNewOrderClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenu.this, BuyProduct.class));
        }
    };

    private OnClickListener mOrderHistoryClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenu.this, OrderHistory.class));
        }
    };

    private OnItemClickListener mOrderListClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DialogFragment fragment = new OrderDetailsDialog();
            Bundle args = new Bundle();
            final Order order = orderList.get(position);
            args.putParcelable("order", new OrderParcel(order));
            fragment.setArguments(args);
            fragment.show(getSupportFragmentManager(), TAG + "OrderDetailsDialog");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button newOrder = (Button)findViewById(R.id.new_order);
        newOrder.setOnClickListener(mNewOrderClickListener);

        Button orderHistory = (Button)findViewById(R.id.order_history);
        orderHistory.setOnClickListener(mOrderHistoryClickListener);

        adapter = new OrderAdapter(this, orderList);
        ListView orderListView = (ListView)findViewById(R.id.pending_orders);
        orderListView.setAdapter(adapter);
        orderListView.setOnItemClickListener(mOrderListClickListener);

        if (Privileges.isAdmin()) {
            Button addProduct = (Button)findViewById(R.id.add_product);
            addProduct.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainMenu.this, AddProduct.class));
                }
            });
            Button removeProduct = (Button)findViewById(R.id.remove_product);
            removeProduct.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainMenu.this, RemoveProduct.class));
                }
            });
        } else {
            findViewById(R.id.admin_layout).setVisibility(LinearLayout.GONE);
        }

        db = FirebaseDatabase.getInstance();
        if (Privileges.isAdmin()) {
            db.getReference("/users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot o : dataSnapshot.getChildren()) {
                        if (o.getKey().equals("orders")) {
                            o.getRef().addChildEventListener(orderListener);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // TODO
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // TODO
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    // TODO
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    //Toast.makeText(MainMenu.this, R.string.db_read_failed, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Failed to read database:", error.toException());
                }
            });
        } else {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            orders = db.getReference("/users/" + uid + "/orders");
            orders.addChildEventListener(orderListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orders.removeEventListener(orderListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenu.this, Login.class));
                Toast.makeText(MainMenu.this, R.string.logged_out, Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

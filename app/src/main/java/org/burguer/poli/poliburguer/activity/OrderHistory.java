package org.burguer.poli.poliburguer.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.firebase.ListChildEventListener;
import org.burguer.poli.poliburguer.models.Order;
import org.burguer.poli.poliburguer.parcel.OrderParcel;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    private static final String TAG = "PoliBurger";
    private ArrayList<Order> orderList = new ArrayList<>();
    private OrderAdapter adapter;
    private DatabaseReference orders;
    private FirebaseDatabase db;

    private ChildEventListener orderListener = new ListChildEventListener<Order>(Order.class, orderList) {
        @Override
        public void onUpdate() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError error) {
            //Toast.makeText(OrderHistory.this, R.string.db_read_failed, Toast.LENGTH_LONG).show();
            Log.w(TAG, "Failed to read database:", error.toException());
        }
    };

    private OnItemClickListener mOrderListClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DialogFragment fragment = new HistoryDetailsDialog();
            Bundle args = new Bundle();
            final Order order = orderList.get(position);
            args.putParcelable("order", new OrderParcel(order));
            fragment.setArguments(args);
            fragment.show(getSupportFragmentManager(), TAG + "HistoryDetailsDialog");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        adapter = new OrderAdapter(this, orderList);
        ListView orderListView = (ListView)findViewById(R.id.list_view_item_list);
        orderListView.setAdapter(adapter);
        orderListView.setOnItemClickListener(mOrderListClickListener);

        db = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orders = db.getReference("/users/" + uid + "/history");
        orders.addChildEventListener(orderListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orders.removeEventListener(orderListener);
    }

}

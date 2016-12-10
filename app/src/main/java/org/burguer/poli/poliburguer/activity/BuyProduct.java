package org.burguer.poli.poliburguer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.burguer.poli.poliburguer.models.Product;

public class BuyProduct extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    List<Map<String, String>> listViewData = new ArrayList<>();
    private FirebaseDatabase db;
    private DatabaseReference products;
    private ListView productList;
    private ChildEventListener productListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
            listViewData.add(snapshotToMap(snapshot));
            updateView();
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
            String key = snapshot.getKey();
            for (Map<String, String> m : listViewData) {
                if (m.get("key").equals(key)) {
                    m.putAll(snapshotToMap(snapshot));
                    updateView();
                    return;
                }
            }
            throw new RuntimeException("Changing nonexistent id");
        }

        @Override
        public void onChildRemoved(DataSnapshot snapshot) {
            String key = snapshot.getKey();
            for (Iterator<Map<String, String>> i = listViewData.listIterator(); i.hasNext(); ) {
                Map<String, String> m = i.next();
                if (m.get("key").equals(key)) {
                    i.remove();
                    updateView();
                    return;
                }
            }
            throw new RuntimeException("Removing nonexistent id");
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

    private void updateView() {
        SimpleAdapter adapter = new SimpleAdapter(BuyProduct.this, listViewData,
                android.R.layout.simple_list_item_2, new String[] {"name", "description"},
                new int[] {android.R.id.text1, android.R.id.text2});
        productList.setAdapter(adapter);
    }

    private Map<String, String> snapshotToMap(DataSnapshot snapshot) {
        Product product = snapshot.getValue(Product.class);
        Map<String, String> data = new HashMap<>(3);
        data.put("name", product.getName());
        data.put("description", product.getDescription() + " - " + product.getFormattedPrice());
        data.put("key", snapshot.getKey());
        return data;
    }

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

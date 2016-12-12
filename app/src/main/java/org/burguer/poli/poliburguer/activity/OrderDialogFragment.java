package org.burguer.poli.poliburguer.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.firebase.ListChildEventListener;
import org.burguer.poli.poliburguer.models.Order;
import org.burguer.poli.poliburguer.models.Product;
import org.burguer.poli.poliburguer.parcel.OrderParcel;

import java.util.ArrayList;

public class OrderDialogFragment extends DialogFragment {

    private static final String TAG = "PoliBurger";
    private DatabaseReference productsRef;
    private ChildEventListener productListener;
    private ProductAdapter productAdapter;
    protected FirebaseDatabase db;
    protected final ArrayList<Product> productList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
        ListView productListView = (ListView)getDialog().findViewById(R.id.list_view_item_list);
        productListView.setAdapter(productAdapter);
    }

    protected AlertDialog.Builder createDialogBuilder() {
        final Activity activity = getActivity();
        final Bundle args = getArguments();
        final Order order = ((OrderParcel)args.getParcelable("order")).getOrder();
        final ArrayList<Product> products = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        productAdapter = new ProductAdapter(activity, productList);
        productListener = new ListChildEventListener<Product>(Product.class, products) {
            @Override
            public void onUpdate() {
                productList.clear();
                for (String key : order.getProducts())
                    for (Product p : products)
                        if (p.getKey().equals(key)) {
                            productList.add(p);
                            break;
                        }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Toast.makeText(activity, R.string.db_read_failed, Toast.LENGTH_LONG).show();
                Log.w(TAG, "Failed to read database:", error.toException());
            }
        };
        db = FirebaseDatabase.getInstance();
        productsRef = db.getReference("/products");
        productsRef.addChildEventListener(productListener);

        return builder.setTitle(R.string.order_details_dialog)
                .setView(R.layout.list_view)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productsRef.addChildEventListener(productListener);
    }

}

package org.burguer.poli.poliburguer.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import org.burguer.poli.poliburguer.models.Product;
import org.burguer.poli.poliburguer.parcel.OrderParcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderDetailsDialog extends DialogFragment {

    private static final String TAG = "PoliBurger";
    private DatabaseReference products;
    private ChildEventListener productListener;
    private ProductAdapter productAdapter;

    @Override
    public void onStart() {
        super.onStart();
        ListView productListView = (ListView)getDialog().findViewById(R.id.checkout_list);
        productListView.setAdapter(productAdapter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final Bundle args = getArguments();
        final Order order = ((OrderParcel)args.getParcelable("order")).getOrder();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final ArrayList<Product> productList = new ArrayList<>();
        productAdapter = new ProductAdapter(activity, productList);

        productListener = new ListChildEventListener<Product>(Product.class, productList) {
            @Override
            public void onUpdate() {
                for (Iterator<Product> i = productList.listIterator(); i.hasNext(); ) {
                    Product p = i.next();
                    if (!order.getProducts().contains(p.getKey()))
                        i.remove();
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(activity, R.string.db_read_failed, Toast.LENGTH_LONG).show();
                Log.w(TAG, "Failed to read database:", error.toException());
            }
        };
        products = FirebaseDatabase.getInstance().getReference("/products");
        products.addChildEventListener(productListener);

        return builder.setTitle(R.string.order_details_dialog)
                .setView(R.layout.checkout)
                .setPositiveButton(R.string.order_ready, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Map<String, Object> update = new HashMap<>();
                        DatabaseReference dbUser = db.getReference("users/" + uid);

                        DatabaseReference historyRef = dbUser.child("history").push();
                        update.put("history/" + historyRef.getKey(), order);
                        update.put("orders/" + order.getKey(), null);
                        dbUser.updateChildren(update, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                if (error != null) {
                                    Toast.makeText(activity, R.string.db_communication_failed, Toast.LENGTH_LONG).show();
                                    Log.e(TAG, error.toString());
                                    return;
                                }
                                dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                }).create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        products.addChildEventListener(productListener);
    }

}

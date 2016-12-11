package org.burguer.poli.poliburguer.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.models.Product;
import org.burguer.poli.poliburguer.parcel.ProductParcel;

import java.util.ArrayList;

public class HistoryDetailsDialog extends OrderDialogFragment {

    private static final String TAG = "PoliBurger";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        AlertDialog.Builder builder = createDialogBuilder();
        builder.setPositiveButton(R.string.repeat_order, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ArrayList<ProductParcel> order = new ArrayList<ProductParcel>();
                for (Product p :productList)
                    order.add(new ProductParcel(p));
                Intent intent = new Intent(activity, Checkout.class);
                intent.putExtra("order", order);
                activity.finish();
                startActivity(intent);
            }
        });
        return builder.create();
    }

}

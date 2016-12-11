package org.burguer.poli.poliburguer.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.burguer.poli.poliburguer.R;
import org.burguer.poli.poliburguer.firebase.ListChildEventListener;
import org.burguer.poli.poliburguer.firebase.NotificationsService;

import org.burguer.poli.poliburguer.models.Order;
import org.burguer.poli.poliburguer.parcel.OrderParcel;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailsDialog extends OrderDialogFragment {

    private static final String TAG = "PoliBurger";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final Order order = ((OrderParcel)getArguments().getParcelable("order")).getOrder();
        AlertDialog.Builder builder = createDialogBuilder();

        if (Privileges.isAdmin())
            builder.setPositiveButton(R.string.order_ready, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
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
                    String destination = order.getFcm();
                    NotificationsService.sendNotification(destination);
                }
            });
        return builder.create();
    }

}

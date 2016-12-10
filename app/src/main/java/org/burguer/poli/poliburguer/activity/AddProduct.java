package org.burguer.poli.poliburguer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;

import org.burguer.poli.poliburguer.models.Product;

public class AddProduct extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    private FirebaseDatabase db;
    private DatabaseReference products;
    private EditText name;
    private EditText description;
    private EditText store;
    private EditText price;
    private Button addButton;

    private OnClickListener mAddClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Product product = new Product(name.getText().toString(),
                    description.getText().toString(),
                    Integer.parseInt(store.getText().toString()),
                    Math.round(100 * Float.parseFloat(price.getText().toString())));
            addButton.setEnabled(false);
            DatabaseReference ref = products.push();
            ref.setValue(product, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    addButton.setEnabled(true);
                    if (error != null) {
                        Toast.makeText(AddProduct.this, R.string.product_add_failed, Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.toString());
                        return;
                    }
                    Toast.makeText(AddProduct.this, R.string.product_add_success, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        addButton = (Button)findViewById(R.id.new_product_add);
        addButton.setOnClickListener(mAddClickListener);

        name = (EditText)findViewById(R.id.new_product_name);
        description = (EditText)findViewById(R.id.new_product_description);
        store = (EditText)findViewById(R.id.new_product_store);
        price = (EditText)findViewById(R.id.new_product_price);

        db = FirebaseDatabase.getInstance();
        products = db.getReference("products");
    }

}

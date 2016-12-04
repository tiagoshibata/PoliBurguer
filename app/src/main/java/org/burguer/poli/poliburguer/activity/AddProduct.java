package org.burguer.poli.poliburguer.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.burguer.poli.poliburguer.R;

import models.Product;

public class AddProduct extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    private FirebaseDatabase db;
    private DatabaseReference products;
    private EditText name;
    private EditText description;
    private EditText store;
    private EditText price;

    private OnClickListener mAddClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Product product = new Product(name.getText().toString(),
                    description.getText().toString(),
                    Integer.parseInt(store.getText().toString()),
                    Integer.parseInt(price.getText().toString()));
            DatabaseReference ref = products.push();
            ref.setValue(product);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        Button addButton = (Button)findViewById(R.id.new_product_add);
        addButton.setOnClickListener(mAddClickListener);

        name = (EditText)findViewById(R.id.new_product_name);
        description = (EditText)findViewById(R.id.new_product_description);
        store = (EditText)findViewById(R.id.new_product_store);
        price = (EditText)findViewById(R.id.new_product_price);

        db = FirebaseDatabase.getInstance();
        products = db.getReference("products");
    }

}

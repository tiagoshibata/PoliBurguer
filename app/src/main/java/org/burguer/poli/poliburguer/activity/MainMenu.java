package org.burguer.poli.poliburguer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import org.burguer.poli.poliburguer.R;

public class MainMenu extends AppCompatActivity {

    private OnClickListener mNewOrderClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenu.this, BuyProduct.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button newOrder = (Button)findViewById(R.id.new_order);
        newOrder.setOnClickListener(mNewOrderClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenu.this, Login.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

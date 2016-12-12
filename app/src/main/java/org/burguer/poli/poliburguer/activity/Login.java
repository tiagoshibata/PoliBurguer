package org.burguer.poli.poliburguer.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.burguer.poli.poliburguer.R;

public class Login extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;

    private AlertDialog.Builder loginDialogBuilder;
    private AlertDialog loginDialog;

    private boolean startIfLoginSuccessful(FirebaseAuth auth) {
        boolean success = auth.getCurrentUser() != null;
        if (success) {
            finish();
            startActivity(new Intent(Login.this, MainMenu.class));
        }
        return success;
    }


    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
            startIfLoginSuccessful(auth);
        }
    };

    private boolean validatePassword(String pass) {
        if (pass.length() < 6) {
            Toast.makeText(Login.this, R.string.password_too_short, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private OnClickListener mLoginClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String pass = mPassword.getText().toString();

            if (!validatePassword(pass))
                return;

            loginDialogBuilder = new AlertDialog.Builder(Login.this);
            loginDialogBuilder.setMessage(R.string.logging_in);
            loginDialog = loginDialogBuilder.create();
            loginDialog.show();
            //Toast.makeText(Login.this, R.string.logging_in, Toast.LENGTH_SHORT).show();

            mAuth.signInWithEmailAndPassword(mEmail.getText().toString(),
                    pass)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(Login.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, R.string.logged_in, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private OnClickListener mCreateAccountClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText repeatPassword = (EditText)findViewById(R.id.repeatPassword);
            String pass = mPassword.getText().toString();

            if (!pass.equals(repeatPassword.getText().toString())) {
                Toast.makeText(Login.this, R.string.password_mismatch, Toast.LENGTH_LONG).show();
                return;
            }
            if (!validatePassword(pass))
                return;

            loginDialogBuilder = new AlertDialog.Builder(Login.this);
            loginDialogBuilder.setMessage(R.string.creating_account);
            loginDialog = loginDialogBuilder.create();
            loginDialog.show();
            //Toast.makeText(Login.this, R.string.creating_account, Toast.LENGTH_SHORT).show();

            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), pass)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "createUserWithEmail:failed");
                                Toast.makeText(Login.this, R.string.account_creation_failed,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Login.this, R.string.account_created, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (!startIfLoginSuccessful(mAuth)) {
            setContentView(R.layout.login);

            Button login = (Button)findViewById(R.id.loginButton);
            login.setOnClickListener(mLoginClickListener);

            Button createAccount = (Button)findViewById(R.id.createAccount);
            createAccount.setOnClickListener(mCreateAccountClickListener);

            mEmail = (EditText)findViewById(R.id.email);
            mPassword = (EditText)findViewById(R.id.password);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}

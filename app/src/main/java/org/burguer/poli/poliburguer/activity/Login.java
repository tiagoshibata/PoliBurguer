package org.burguer.poli.poliburguer.activity;

import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseUser;

import org.burguer.poli.poliburguer.R;

public class Login extends AppCompatActivity {

    private static final String TAG = "PoliBurger";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail;
    private EditText mPassword;

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

            mAuth.signInWithEmailAndPassword(mEmail.getText().toString(),
                    pass)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(Login.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
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

            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), pass)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful())
                                Toast.makeText(Login.this, R.string.account_creation_failed,
                                        Toast.LENGTH_LONG).show();
                        }
                    });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(mLoginClickListener);

        Button createAccount = (Button)findViewById(R.id.createAccount);
        createAccount.setOnClickListener(mCreateAccountClickListener);

        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

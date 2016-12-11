package org.burguer.poli.poliburguer.activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Privileges {
    static boolean isAdmin(FirebaseUser user) {
        return user != null && user.getEmail().equals("admin@gmail.com");
    }

    static boolean isAdmin() {
        return isAdmin(FirebaseAuth.getInstance().getCurrentUser());
    }
}

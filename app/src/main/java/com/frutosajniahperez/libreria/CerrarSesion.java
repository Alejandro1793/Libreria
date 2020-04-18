package com.frutosajniahperez.libreria;

import com.google.firebase.auth.FirebaseAuth;

public class CerrarSesion {

    public static void cerrarSesion(FirebaseAuth mAuth){
        mAuth.signOut();
    }
}

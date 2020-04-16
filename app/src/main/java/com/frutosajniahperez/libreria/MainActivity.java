package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        Button bntSet = findViewById(R.id.btnSet);
        Button btnGet = findViewById(R.id.btnGet);

        bntSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario("1111", "1111", "1111", "rol");
                database.collection("users").document("usuarioPrueba").set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Usuario añadido", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.collection("users").document("usuarioPrueba").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                usuario = document.toObject(Usuario.class);
                                Toast.makeText(MainActivity.this, "Ha funcionado", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }
}

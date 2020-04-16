package com.frutosajniahperez.libreria;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Principal extends AppCompatActivity {
    Button btnRegistro, btnInicioSesion;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_principal);
        btnRegistro = (findViewById(R.id.btnRegistro));
        btnInicioSesion = (findViewById(R.id.btnInicioSesion));

        //Pantalla para iniciar Sesion

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, IniciarSesion.class));
                //startActivity(new Intent(Principal.this, MainActivity.class));
            }
        });


        //Pantalla para registrar usuario
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, EleccionRegistro.class));
            }
        });
    }




}

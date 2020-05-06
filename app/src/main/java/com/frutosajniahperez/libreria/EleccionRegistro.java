package com.frutosajniahperez.libreria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EleccionRegistro extends AppCompatActivity {

    Button btnRegistroProfe, btnRegistroAdmin, btnRegistroAlumno;
    ImageView btregresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_registro);

        btnRegistroAdmin = findViewById(R.id.btnISAdmin);
        btnRegistroProfe = findViewById(R.id.btnISProfe);
        btnRegistroAlumno = findViewById(R.id.btnISAlumno);
        btregresar = findViewById(R.id.btnRegresar);

        btnRegistroProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EleccionRegistro.this, RegistroProfesor.class));
            }
        });

        btnRegistroAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EleccionRegistro.this, RegistroAdministrador.class));
            }
        });

        btnRegistroAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EleccionRegistro.this, RegistroAlumno.class));
            }
        });

        btregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EleccionRegistro.this, Principal.class));
            }
        });

    }
}

package com.frutosajniahperez.libreria.ui.libreria;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Dialogo_libreria_manual {

    private EditText txtTitulo, txtAutores, txtEditorial, txtAnioPublicacion, txtIsbn, txtSinopsis;
    private String isbn, titulo, editorial, sinopsis, anio;
    private Dialog dialog;
    private HashMap<String, String> autores = new HashMap<>();


    public interface ResultadoDialogoBusqueda {
        void ResultadoDialogoBusqueda(Libro libro);
    }

    private ResultadoDialogoBusqueda interfaz;

    public Dialogo_libreria_manual(Context context, ResultadoDialogoBusqueda actividad) {

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_libreria_manual);
        dialog.setCanceledOnTouchOutside(true);

        txtTitulo = dialog.findViewById(R.id.etTituloLibro);
        txtAutores = dialog.findViewById(R.id.etAutor1);
        txtEditorial = dialog.findViewById(R.id.etEditorial);
        txtAnioPublicacion = dialog.findViewById(R.id.etAnioPublicacion);
        txtIsbn = dialog.findViewById(R.id.etIsbn);
        txtSinopsis = dialog.findViewById(R.id.etSinopsis);
        TextView btnAceptarLibro = dialog.findViewById(R.id.btnAceptarLibro);


        btnAceptarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo = txtTitulo.getText().toString();
                autores.put("1", txtAutores.getText().toString());
                editorial = txtEditorial.getText().toString();
                anio = txtAnioPublicacion.getText().toString();
                isbn = txtIsbn.getText().toString();
                sinopsis = txtSinopsis.getText().toString();
                if (titulo.length() != 0 && autores.size() != 0 && isbn.length() != 0) {
                    if (editorial.length() == 0){
                        editorial = " - ";
                    }
                    if (anio.length() == 0){
                        anio = " - ";
                    }
                    if (sinopsis.length() == 0){
                        sinopsis = " - ";
                    }
                    Libro libro = new Libro(isbn, titulo, autores, editorial, sinopsis, anio, "Sin imagen");
                    interfaz.ResultadoDialogoBusqueda(libro);
                    dialog.dismiss();
                } else {
                    Toast.makeText(dialog.getContext(), "Tienes que rellenar los tres primeros campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}

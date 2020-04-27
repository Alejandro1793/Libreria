package com.frutosajniahperez.libreria.ui.libreria;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;
import com.frutosajniahperez.libreria.ui.alumnos.Dialogo_alumno;

public class Dialogo_busqueda_google {

    public interface ResultadoDialogoBusquedaGoogle {
        void ResultadoDialogoBusquedaGoogle(Libro libro);
    }
    private ResultadoDialogoBusquedaGoogle interfaz;

    public Dialogo_busqueda_google(Context context, ResultadoDialogoBusquedaGoogle actividad) {

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_busqueda_google);
        dialog.setCanceledOnTouchOutside(true);

        final SearchView searchView = dialog.findViewById(R.id.svLibros);
        final ImageView portada = dialog.findViewById(R.id.ivPortada);
        final TextView txtTitulo = dialog.findViewById(R.id.txtTituloLibro);
        final TextView txtAutor = dialog.findViewById(R.id.txtAutorLibro);
        final TextView txtEditorial = dialog.findViewById(R.id.txtEditorialLibro);
        final TextView txtAnioPublicacion = dialog.findViewById(R.id.txtAnioPublicacion);
        final TextView txtIsbn = dialog.findViewById(R.id.txtIsbnLibro);
        TextView btnAceptarLibroGoogle  = dialog.findViewById(R.id.btnAceptarLibroGoogle);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new EncontrarLibro(portada, txtTitulo, txtAutor, txtEditorial, txtAnioPublicacion, txtIsbn).execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnAceptarLibroGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }
}

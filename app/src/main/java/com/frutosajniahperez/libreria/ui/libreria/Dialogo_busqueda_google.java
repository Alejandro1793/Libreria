package com.frutosajniahperez.libreria.ui.libreria;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;

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
                if (query.length() != 0) {
                    new EncontrarLibro(portada, txtTitulo, txtAutor, txtEditorial, txtAnioPublicacion, txtIsbn, dialog.getContext()).execute(query);

                    return true;
                } else {
                    return false;
                }
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

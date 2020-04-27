package com.frutosajniahperez.libreria.ui.libreria;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

public class EncontrarLibro extends AsyncTask<String, Void, String> {

    ImageView portada;
    TextView txtTitulo, txtAutores, txtEditorial, txtAnioPublicacion, txtIsbn;

    public EncontrarLibro(ImageView portada, TextView txtTitulo, TextView txtAutores, TextView txtEditorial, TextView txtAnioPublicacion, TextView txtIsbn) {
        this.portada = portada;
        this.txtTitulo = txtTitulo;
        this.txtAutores = txtAutores;
        this.txtEditorial = txtEditorial;
        this.txtAnioPublicacion = txtAnioPublicacion;
        this.txtIsbn = txtIsbn;
    }

    @Override
    protected String doInBackground(String... strings) {
        return Conexion.getLibroInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

package com.frutosajniahperez.libreria.ui.libreria;


import android.os.AsyncTask;

public class EncontrarLibro extends AsyncTask<String, Void, String> {

    private ObtenerDatos datos;

    public EncontrarLibro(ObtenerDatos datos) {
        this.datos = datos;
    }

    public interface ObtenerDatos{
        void datosJson(String s);
    }


    @Override
    protected String doInBackground(String... strings) {
        return Conexion.getLibroInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        datos.datosJson(s);
    }


}

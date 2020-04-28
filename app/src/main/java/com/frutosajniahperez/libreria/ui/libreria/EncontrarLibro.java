package com.frutosajniahperez.libreria.ui.libreria;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frutosajniahperez.libreria.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EncontrarLibro extends AsyncTask<String, Void, String> implements CargarImagen.Listener {

    ImageView portada;
    TextView txtTitulo, txtAutores, txtEditorial, txtAnioPublicacion, txtIsbn;
    Context context;

    public EncontrarLibro(ImageView portada, TextView txtTitulo, TextView txtAutores, TextView txtEditorial, TextView txtAnioPublicacion, TextView txtIsbn, Context context) {
        this.portada = portada;
        this.txtTitulo = txtTitulo;
        this.txtAutores = txtAutores;
        this.txtEditorial = txtEditorial;
        this.txtAnioPublicacion = txtAnioPublicacion;
        this.txtIsbn = txtIsbn;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        return Conexion.getLibroInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            JSONObject libro = itemsArray.getJSONObject(0);
            String titulo;
            ArrayList<String> autores = new ArrayList<>();
            String editorial;
            String anio;
            String isbn;
            String imagen;
            try {
                JSONObject libroInfo = libro.getJSONObject("volumeInfo");
                try {
                    titulo = libroInfo.getString("title");
                    txtTitulo.setText(titulo);
                } catch (JSONException e) {
                    txtTitulo.setText(R.string.no_ha_dado_resultados);
                }
                try {
                    JSONArray authors = libroInfo.getJSONArray("authors");
                    for (int i = 0; i < authors.length(); i++) {
                        autores.add(authors.getString(i));
                    }
                    StringBuilder aut = new StringBuilder();
                    for (String autor : autores){
                        aut.append(autor).append(System.getProperty("line.separator"));
                    }
                    txtAutores.setText(aut);
                } catch (JSONException e) {
                    txtAutores.setText(R.string.no_ha_dado_resultados);
                }
                try {
                    editorial = libroInfo.getString("publisher");
                    txtEditorial.setText(editorial);
                } catch (JSONException e) {
                    txtEditorial.setText(R.string.no_ha_dado_resultados);
                }
                try {
                    anio = libroInfo.getString("publishedDate");
                    txtAnioPublicacion.setText(anio);
                } catch (JSONException e) {
                    txtAnioPublicacion.setText(R.string.no_ha_dado_resultados);
                }
                try {
                    JSONArray isbnArray = libroInfo.getJSONArray("industryIdentifiers");
                    JSONObject isbn13 = isbnArray.getJSONObject(1);
                    isbn = isbn13.getString("identifier");
                    txtIsbn.setText(isbn);
                } catch (JSONException e) {
                    txtIsbn.setText(R.string.no_ha_dado_resultados);
                }
                try {
                    JSONObject imagenes = libroInfo.getJSONObject("imageLinks");
                    imagen = imagenes.getString("smallThumbnail");
                    new CargarImagen(EncontrarLibro.this).execute(imagen);
                } catch (JSONException e) {
                    portada.setImageResource(R.drawable.noimg);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } catch (Exception ex){
            Toast.makeText(context, "No se encuentra el libro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        portada.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {
        portada.setImageResource(R.drawable.noimg);
    }
}

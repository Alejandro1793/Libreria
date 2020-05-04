package com.frutosajniahperez.libreria.ui.libreria;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



public class Dialogo_busqueda_google implements EncontrarLibro.ObtenerDatos {

    private ImageView portada;
    private TextView txtTitulo;
    private TextView txtAutores;
    private TextView txtEditorial;
    private TextView txtAnioPublicacion;
    private TextView btnAceptarLibroGoogle;
    public static SearchView searchView;
    private Dialog dialog;
    private String titulo, editorial, anio, isbn, imagen, sinopsis;
    private HashMap<String, String> autores = new HashMap<>();
    private int contAutores = 1;


    public interface ResultadoDialogoBusquedaGoogle {
        void ResultadoDialogoBusquedaGoogle(Libro libro);
    }
    private ResultadoDialogoBusquedaGoogle interfaz;

    public Dialogo_busqueda_google(Context context, ResultadoDialogoBusquedaGoogle actividad) {

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_busqueda_google);
        dialog.setCanceledOnTouchOutside(true);


        searchView = dialog.findViewById(R.id.svLibros);
        portada = dialog.findViewById(R.id.ivPortada);
        txtTitulo = dialog.findViewById(R.id.txtTituloLibro);
        txtAutores = dialog.findViewById(R.id.txtAutorLibro);
        txtEditorial = dialog.findViewById(R.id.txtEditorialLibro);
        txtAnioPublicacion = dialog.findViewById(R.id.txtAnioPublicacion);
        TextView btnIniciarCamara = dialog.findViewById(R.id.btnIniciarCamara);

        btnAceptarLibroGoogle  = dialog.findViewById(R.id.btnAceptarLibroGoogle);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    new EncontrarLibro(Dialogo_busqueda_google.this).execute(query);
                    isbn = query;
                } else {
                    Toast.makeText(dialog.getContext(), "Tienes que introducir un ISBN", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnIniciarCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.getContext().startActivity(new Intent(dialog.getContext(), LectorCodigoBarras.class));
            }
        });



        btnAceptarLibroGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Libro libro = new Libro(isbn, titulo, autores, editorial, sinopsis, anio, imagen);
                interfaz.ResultadoDialogoBusquedaGoogle(libro);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void datosJson(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            JSONObject libro = itemsArray.getJSONObject(0);
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
                        autores.put(String.valueOf(contAutores), authors.getString(i));
                        contAutores++;
                    }
                    StringBuilder aut = new StringBuilder();
                    for (String autor : autores.values()){
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
                    sinopsis = libroInfo.getString("description");
                } catch (JSONException e) {
                    sinopsis = "Sin sinopsis";
                }
                try {
                    anio = libroInfo.getString("publishedDate");
                    txtAnioPublicacion.setText(anio);
                } catch (JSONException e) {
                    txtAnioPublicacion.setText(R.string.no_ha_dado_resultados);
                }
                try {
                    JSONObject imagenes = libroInfo.getJSONObject("imageLinks");
                    imagen = imagenes.getString("smallThumbnail");
                    portada.setImageBitmap(new CargarImagen().execute(imagen).get());
                } catch (JSONException e) {
                    imagen = "Sin imagen";
                    portada.setImageResource(R.drawable.noimg);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            btnAceptarLibroGoogle.setEnabled(true);
        } catch (Exception ex){
            Toast.makeText(dialog.getContext(), "No se encuentra el libro", Toast.LENGTH_SHORT).show();
        }
    }
}

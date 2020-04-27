package com.frutosajniahperez.libreria.ui.libreria;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conexion {

    //Conexion con la API de GoogleBooks
    public static final String URL_BASE_LIBROS = "https://www.googleapis.com/books/v1/volumes?";
    public static final String PARAMETROS_QUERY = "q";
    public static final String RESULTADOS_MAX = "maxResults";
    public static final String PRINT_TYPE = "printType";

    static String getLibroInfo (String queryString){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try{
            //Construir la URI, limitando los resultados a 1 y solo libros impresos
            Uri buildUri = Uri.parse(URL_BASE_LIBROS).buildUpon()
                    .appendQueryParameter(PARAMETROS_QUERY, queryString)
                    .appendQueryParameter(RESULTADOS_MAX, "1")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(buildUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0){
                return null;
            }
            bookJSONString = buffer.toString();
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("LIBRO", bookJSONString);
            return  bookJSONString;
        }

    }
}

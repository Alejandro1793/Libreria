package com.frutosajniahperez.libreria.ui.libreria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CargarImagen extends AsyncTask <String, Void, Bitmap> {


    public CargarImagen(){
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(strings[0]).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

package com.frutosajniahperez.libreria.ui.libreria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArrayAdapterLibreria extends ArrayAdapter<Libro> {

    DecimalFormat df;

    public ArrayAdapterLibreria(@NonNull Context context, @NonNull List<Libro> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Libro libro = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_libros, parent, false);
        df = new DecimalFormat("0.0");
        TextView tv = v.findViewById(R.id.txtTituloLibro);
        ImageView iv = v.findViewById(R.id.ivPortada);
        tv.setText(libro.getTitulo());
        tv = v.findViewById(R.id.txtAutorLibro);
        StringBuilder cadena = new StringBuilder();
        for (String autor : libro.getAutores().values()){
            cadena.append(autor).append(System.getProperty("line.separator"));
        }
        tv.setText(cadena);
        tv = v.findViewById(R.id.txtAnioPublicacion);
        tv.setText(libro.getAño());
        tv = v.findViewById(R.id.txtEditorialLibro);
        tv.setText(libro.getEditorial());
        tv = v.findViewById(R.id.txtIsbnLibro);
        tv.setText(libro.getIsbn());
        tv = v.findViewById(R.id.txtSinopsis);
        tv.setText(libro.getSinopsis());
        if (libro.getImagen().equals("Sin imagen")){
            iv.setImageResource(R.drawable.noimg);
        } else {
            try {
                iv.setImageBitmap(new CargarImagen().execute(libro.getImagen()).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        tv = v.findViewById(R.id.txtPuntuacionAlumnado);
        tv.setText(df.format(libro.getValoracion()));
        return v;
    }
}





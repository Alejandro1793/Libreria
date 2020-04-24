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

import java.util.List;

public class ArrayAdapterLibreria extends ArrayAdapter<Libro> {

    public ArrayAdapterLibreria(@NonNull Context context, @NonNull List<Libro> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Libro libro = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_libros, parent, false);
        TextView tv = v.findViewById(R.id.txtTituloLibro);
        ImageView iv = v.findViewById(R.id.ivPortada);
        tv.setText(libro.getTitulo());
        tv = v.findViewById(R.id.txtAutorLibro);
        StringBuilder cadena = new StringBuilder();
        int tamaño = libro.getAutores().size();
        if (libro.getAutores().size() > 1) {
            for (int i = 0; i < tamaño - 1; i++) {
                cadena.append(libro.getAutores().get(i));
                cadena.append(", ");
            }
            cadena.append(libro.getAutores().get(tamaño - 1));
        } else {
            cadena.append(libro.getAutores().get(0));
        }
        tv.setText(cadena);
        tv = v.findViewById(R.id.txtAnioPublicacion);
        tv.setText(libro.getAño());
        tv = v.findViewById(R.id.txtEditorialLibro);
        tv.setText(libro.getEditorial());
        tv = v.findViewById(R.id.txtIsbnLibro);
        tv.setText(libro.getIsbn());
        if (libro.hasImagen()){

        }

        return v;
    }
}

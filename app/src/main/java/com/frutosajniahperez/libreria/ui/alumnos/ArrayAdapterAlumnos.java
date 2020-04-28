package com.frutosajniahperez.libreria.ui.alumnos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;

import java.util.List;

public class ArrayAdapterAlumnos extends ArrayAdapter<Alumno> {

    public ArrayAdapterAlumnos(@NonNull Context context, @NonNull List<Alumno> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Alumno alumno = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_alumnos, parent, false);
        TextView tv = v.findViewById(R.id.txtNombreAlumnoLista);
        tv.setText(alumno.getNombre());
        tv = v.findViewById(R.id.txtListaLibrosLeidos);
        StringBuilder cadena = new StringBuilder();
        for (Libro libro : alumno.getLibrosLeidos()){
            cadena.append(libro.getTitulo()).append(System.getProperty("line.separator"));
        }
        tv.setText(cadena);
        return v;
    }
}

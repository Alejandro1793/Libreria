package com.frutosajniahperez.libreria.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ArrayAdapterInicio extends ArrayAdapter<Prestamo> {

    HashMap<String, Alumno> alumnos;

    public ArrayAdapterInicio(@NonNull Context context, ArrayList<Prestamo> listaPrestamos, HashMap<String, Alumno> alumnos) {
        super(context, 0, listaPrestamos);
        this.alumnos = alumnos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Prestamo prestamo = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_alumno_libro, parent, false);
        TextView tv = v.findViewById(R.id.txtNombreAlumno);
        String nombre = alumnos.get(prestamo.getAlumno()).getNombre();
        tv.setText(nombre);
        tv = v.findViewById(R.id.txtTitulo);
        tv.setText(prestamo.getLibro());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tv = v.findViewById(R.id.txtFechaPrestamo);
        String fechaPrestamo = sdf.format(prestamo.getFechaPrestamo());
        tv.setText(fechaPrestamo);
        tv = v.findViewById(R.id.txtFechaEntrega);
        String fechaEntrega = sdf.format(prestamo.getFechaEntrega());
        tv.setText(fechaEntrega);
        return v;
    }
}

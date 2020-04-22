package com.frutosajniahperez.libreria.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;

import java.util.ArrayList;

public class ArrayAdapterInicio extends ArrayAdapter<Prestamo> {

    public ArrayAdapterInicio(@NonNull Context context, ArrayList<Prestamo> listaPrestamos) {
        super(context, 0, listaPrestamos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Prestamo prestamo = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_alumno_libro, parent, false);
        TextView tv = v.findViewById(R.id.txtNombreAlumno);
        tv.setText(prestamo.getAlumno());
        tv = v.findViewById(R.id.txtTitulo);
        tv.setText(prestamo.getLibro());
        tv = v.findViewById(R.id.txtFechaPrestamo);
        tv.setText(prestamo.getFechaPrestamo().toString());
        tv = v.findViewById(R.id.txtFechaEntrega);
        tv.setText(prestamo.getFechaEntrega().toString());
        return v;
    }
}

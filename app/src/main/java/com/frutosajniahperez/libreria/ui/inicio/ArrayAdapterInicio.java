package com.frutosajniahperez.libreria.ui.inicio;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.frutosajniahperez.libreria.Alumno;;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ArrayAdapterInicio extends ArrayAdapter<Prestamo> {

    private HashMap<String, Alumno> alumnos;
    private String nombreBuscado;

    public ArrayAdapterInicio(@NonNull Context context, ArrayList<Prestamo> listaPrestamos, HashMap<String, Alumno> alumnos) {
        super(context, 0, listaPrestamos);
        this.alumnos = alumnos;
        this.nombreBuscado = "";
    }

    public ArrayAdapterInicio(@NonNull Context context, ArrayList<Prestamo> listaPrestamos, HashMap<String, Alumno> alumnos, String nombreBuscado) {
        super(context, 0, listaPrestamos);
        this.alumnos = alumnos;
        this.nombreBuscado = nombreBuscado;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Prestamo prestamo = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_alumno_libro, parent, false);
        TextView tv = v.findViewById(R.id.txtNombreAlumno);
        String nombre = alumnos.get(prestamo.getAlumno()).getNombre();
        if (nombreBuscado.length() == 0 || nombre.contains(nombreBuscado)) {
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
            if (prestamo.getFechaEntrega().before(new Date())) {
                LinearLayout linearLayout = v.findViewById(R.id.LinealFechaEntrega);
                linearLayout.setBackgroundColor(Color.rgb(225, 155, 155));

            }
        } else {
            LinearLayout linearLayout = v.findViewById(R.id.lyPrestamo);
            linearLayout.setVisibility(View.GONE);
        }
        return v;
    }
}

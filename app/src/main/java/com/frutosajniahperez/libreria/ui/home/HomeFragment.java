package com.frutosajniahperez.libreria.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;
import com.frutosajniahperez.libreria.ui.libreria.CargarImagen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    private String idCole;
    private Alumno alumno;
    private Libro libro;
    private Colegio cole;
    private Prestamo prestamo;
    private FirebaseFirestore database;
    private HashMap<String, Libro> listadoLibros;
    private ListView lvPrestamos;
    private ImageView imPortadaImagen;
    private TextView txtTituloLibroPrestado, txtFechaEntregaAlumno;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            idCole = getArguments().getString("idcole");
            alumno = (Alumno) getArguments().getSerializable("alumno");
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imPortadaImagen = root.findViewById(R.id.imPortadaAlumno);
        txtTituloLibroPrestado = root.findViewById(R.id.txtTituloLibroPrestado);
        txtFechaEntregaAlumno = root.findViewById(R.id.txtFechaEntregaAlumno);

        if (idCole != null) {
            database.collection("Colegios").document(idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            cole = document.toObject(Colegio.class);
                            prestamo = cole.getAulas().get(alumno.getIdAula()).getListadoPrestamos().get(alumno.getIdAlumno());
                            listadoLibros = cole.getAulas().get(alumno.getIdAula()).getLibreria();
                            if (prestamo != null) {
                                for (Libro lib : listadoLibros.values()) {
                                    if (lib.getTitulo().equals(prestamo.getLibro())) {
                                        libro = lib;
                                        break;
                                    }
                                }
                                imPortadaImagen.setVisibility(View.VISIBLE);
                                txtTituloLibroPrestado.setVisibility(View.VISIBLE);
                                txtFechaEntregaAlumno.setVisibility(View.VISIBLE);
                                txtTituloLibroPrestado.setText(prestamo.getLibro());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String fechaPrestamo = sdf.format(prestamo.getFechaEntrega());
                                txtFechaEntregaAlumno.setText(fechaPrestamo);
                                if (libro.getImagen().equals("Sin imagen")) {
                                    imPortadaImagen.setImageResource(R.drawable.noimg);
                                } else {
                                    try {
                                        imPortadaImagen.setImageBitmap(new CargarImagen().execute(libro.getImagen()).get());
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), "No estás leyendo ningún libro actualmente", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            });
        }
        return root;
    }
}

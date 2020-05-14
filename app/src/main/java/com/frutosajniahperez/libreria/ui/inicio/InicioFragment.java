package com.frutosajniahperez.libreria.ui.inicio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class InicioFragment extends Fragment {

    private String idCole, idAula, idProfe;
    private Colegio cole;
    private HashMap<String, Prestamo> prestamos;
    private HashMap<String, Alumno> alumnos;
    private HashMap<String, Libro> libreria;
    private ArrayList<Prestamo> listaPrestamos;
    private FirebaseFirestore database;
    private ListView lvPrestamos;

    public InicioFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            idCole = getArguments().getString("idcole");
            idAula = getArguments().getString("idaula");
            idProfe = getArguments().getString("idprofe");
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        lvPrestamos = root.findViewById(R.id.listInicio);
        SearchView svLista = root.findViewById(R.id.svLista);
        //Cargar lista de préstamos de la base de datos
        if (idCole != null) {
            database.collection("Colegios").document(idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            cole = document.toObject(Colegio.class);
                            if (idAula == null) {
                                idAula = cole.getProfesorado().get(idProfe).getIdAula();
                            }
                            prestamos = cole.getAulas().get(idAula).getListadoPrestamos();
                            libreria = cole.getAulas().get(idAula).getLibreria();
                            alumnos = cole.getAlumnado();
                            if (prestamos.isEmpty()) {
                                Toast.makeText(getContext(), "Todavía no hay préstamos", Toast.LENGTH_SHORT).show();
                            } else {
                                cargarDatos();
                            }
                        }
                    }
                }
            });
        }

        svLista.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cargarDatos(newText);
                return false;
            }
        });



        lvPrestamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Entregar")
                        .setMessage("¿Seguro que quieres marcar este préstamo como entregado?")
                        .setPositiveButton("Sí",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alumnos.get(listaPrestamos.get(position).getAlumno()).getLibrosLeidos().add(listaPrestamos.get(position).getLibro());
                                        for (Libro libro : libreria.values()){
                                            if (libro.getTitulo().equals(listaPrestamos.get(position).getLibro())){
                                                libro.setPrestado(false);
                                                break;
                                            }
                                        }
                                        prestamos.remove(listaPrestamos.get(position).getAlumno());
                                        listaPrestamos.remove(position);
                                        cole.getAulas().get(idAula).setListadoPrestamos(prestamos);
                                        cole.getAulas().get(idAula).setLibreria(libreria);
                                        subirDatos();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        return root;
    }

    public void cargarDatos() {
        listaPrestamos = new ArrayList<>(prestamos.values());
        ArrayAdapterInicio adapterInicio = new ArrayAdapterInicio(getContext(), listaPrestamos, alumnos);
        lvPrestamos.setAdapter(adapterInicio);
    }

    public void cargarDatos(String nombre) {
        listaPrestamos = new ArrayList<>(prestamos.values());
        ArrayAdapterInicio adapterInicio = new ArrayAdapterInicio(getContext(), listaPrestamos, alumnos, nombre);
        lvPrestamos.setAdapter(adapterInicio);
    }

    public void subirDatos() {
        database.collection("Colegios").document(idCole).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Base de datos actualizada", Toast.LENGTH_SHORT).show();
                    cargarDatos();
                }
            }
        });
    }
}

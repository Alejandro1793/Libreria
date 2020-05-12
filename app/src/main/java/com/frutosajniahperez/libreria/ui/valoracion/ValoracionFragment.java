package com.frutosajniahperez.libreria.ui.valoracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;
import com.frutosajniahperez.libreria.ui.libreria.ArrayAdapterLibreria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ValoracionFragment extends Fragment {

    private String idCole;
    private FirebaseFirestore database;
    private Colegio cole;
    private Alumno alumno;
    private HashMap<String, Libro> libreria;
    private ArrayList<Libro> libros;
    private ListView listValoracion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCole = getArguments().getString("idcole");
            alumno = (Alumno) getArguments().getSerializable("alumno");
        }
        database = FirebaseFirestore.getInstance();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_valoracion, container, false);
        listValoracion = root.findViewById(R.id.lvListadoValoracion);
        libros = new ArrayList<>();
        if (idCole != null) {
            database.collection("Colegios").document(idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            cole = document.toObject(Colegio.class);
                            libreria = cole.getAulas().get(alumno.getIdAula()).getLibreria();
                            if (libreria.isEmpty()) {
                                Toast.makeText(getContext(), "Todavía no hay libros", Toast.LENGTH_SHORT).show();
                            } else {
                                cargarDatos();
                            }
                        }
                    }
                }
            });
        }
        return root;
    }

    private void cargarDatos() {
        for (Libro lib: libreria.values()){
            for (String titulo : alumno.getLibrosLeidos()){
                if (lib.getTitulo().equals(titulo)){
                    libros.add(lib);
                    break;
                }
            }
        }
        ArrayAdapterValoracion adapterValoracion = new ArrayAdapterValoracion(getContext(), libros, idCole, alumno.getIdAula());
        listValoracion.setAdapter(adapterValoracion);
    }
}

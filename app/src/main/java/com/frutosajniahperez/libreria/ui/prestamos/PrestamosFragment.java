package com.frutosajniahperez.libreria.ui.prestamos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.ModificarColegio;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class PrestamosFragment extends Fragment {

    String idCole, idAula, idProfe;
    Colegio cole;
    Map<String, Alumno> alumnado;
    Map<String, Libro> libreria;
    ArrayList<String> listadoAlumnos, listadoLibros;
    FirebaseFirestore database;

    public PrestamosFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCole = getArguments().getString("idcole");
            idAula = getArguments().getString("idaula");
            idProfe = getArguments().getString("idprofe");
        }
        database = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prestamos, container, false);
        final Spinner spAlumno = root.findViewById(R.id.spAlumno);
        final Spinner spTituloLibro = root.findViewById(R.id.spTituloLibro);
        final Button btnAceptarPrestamo = root.findViewById(R.id.btnAceptarPrestamo);
        final CalendarView calendarView = root.findViewById(R.id.calendario);
        listadoAlumnos = new ArrayList<>();
        listadoLibros = new ArrayList<>();

        if (idCole != null){
            database.collection("Colegios").document(idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            cole = document.toObject(Colegio.class);
                            if (idAula == null) {
                                idAula = cole.getProfesorado().get(idProfe).getIdAula();
                            }
                            alumnado =cole.getAulas().get(idAula).getListadoAlumnos();
                            libreria = cole.getAulas().get(idAula).getLibreria();
                            if (!alumnado.isEmpty() && !libreria.isEmpty()) {
                                for (Alumno alumno : alumnado.values()) {
                                    listadoAlumnos.add(alumno.getNombre());
                                }
                                for (Libro libro : libreria.values()) {
                                    listadoLibros.add(libro.getTitulo());
                                }
                                ArrayAdapter<String> adapterAlumno = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listadoAlumnos);
                                ArrayAdapter<String> adapterLibro = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listadoLibros);
                                spAlumno.setAdapter(adapterAlumno);
                                spTituloLibro.setAdapter(adapterLibro);
                                btnAceptarPrestamo.setEnabled(true);
                            } else {
                                Toast.makeText(getContext(), "Faltan datos que mostrar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }

        btnAceptarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cole.getAulas().get(idAula).getListadoPrestamos().put(spAlumno.getSelectedItem().toString(), new Prestamo(spAlumno.getSelectedItem().toString(), spTituloLibro.getSelectedItem().toString(), new Date(), new Date(calendarView.getDate() * 1000)));
                database.collection("Colegios").document(idCole).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Préstamo guardado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return root;
    }
}

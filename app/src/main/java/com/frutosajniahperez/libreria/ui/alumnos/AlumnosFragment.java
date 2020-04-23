package com.frutosajniahperez.libreria.ui.alumnos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.frutosajniahperez.libreria.ui.home.ArrayAdapterInicio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class AlumnosFragment extends Fragment implements Dialogo_alumno.ResultadoDialogoAlumno {

    String idCole, idAula, idProfe;
    Colegio cole;
    Map<String, Alumno> alumnos;
    ArrayList<Alumno> listaAlumnos;
    FirebaseFirestore database;
    ListView listAlumno;

    public AlumnosFragment() {
    }

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alumnos, container, false);
        Button btnAnadirAlumno = root.findViewById(R.id.btnAceptarAlumno);
        listAlumno = root.findViewById(R.id.listAlumnos);
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
                            alumnos = cole.getAulas().get(idAula).getListadoAlumnos();
                            if (alumnos.isEmpty()) {
                                Toast.makeText(getContext(), "Todavía no hay alumnos", Toast.LENGTH_SHORT).show();
                            } else {
                                cargarDatos();
                            }
                        }
                    }
                }
            });
        }


        btnAnadirAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_alumno(getContext(), AlumnosFragment.this);
            }
        });
        return root;
    }

    @Override
    public void ResultadoDialogoAlumno(String idAlumno, String nombreAlumno, String email) {

        if (!cole.getAulas().get(idAula).getListadoAlumnos().containsKey(idAlumno)) {
            Alumno alumno = new Alumno();
            alumno.setIdAlumno(idAlumno);
            alumno.setNombre(nombreAlumno);
            alumno.setEmail(email);
            alumno.setIdAula(idAula);
            alumno.setLibrosLeidos(new ArrayList<Libro>());
            cole.getAulas().get(idAula).getListadoAlumnos().put(idAlumno, alumno);

            database.collection("Colegios").document(idCole).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Alumno registrado", Toast.LENGTH_SHORT).show();
                        cargarDatos();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Ya existe un alumno con este ID", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarDatos(){
        listaAlumnos = new ArrayList<>(alumnos.values());
        ArrayAdapterAlumnos adapterInicio = new ArrayAdapterAlumnos(getContext(), listaAlumnos);
        listAlumno.setAdapter(adapterInicio);
    }
}
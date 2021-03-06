package com.frutosajniahperez.libreria.ui.alumnos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.R;
import com.frutosajniahperez.libreria.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AlumnosFragment extends Fragment implements Dialogo_alumno.ResultadoDialogoAlumno {

    String idCole, idAula, idProfe;
    Colegio cole;
    HashMap<String, Alumno> alumnos;
    ArrayList<Alumno> listaAlumnos;
    FirebaseFirestore database;
    FirebaseAuth mAuth;
    Usuario usuario;
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
        mAuth = FirebaseAuth.getInstance();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alumnos, container, false);
        FloatingActionButton btnAnadirAlumno = root.findViewById(R.id.btnAceptarAlumno);
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
                            alumnos = cole.getAlumnado();
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

        listAlumno.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Borrado")
                        .setMessage("¿Seguro que quieres eliminar este alumno?")
                        .setPositiveButton("Sí",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        database.collection("users").whereEqualTo("idUsuario", listaAlumnos.get(position).getIdAlumno()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                        usuario = documentSnapshot.toObject(Usuario.class);
                                                    }
                                                    if (usuario != null) {
                                                        database.collection("users").document(usuario.getEmail()).delete();
                                                        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getContraseña()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                mAuth.getCurrentUser().delete();
                                                            }
                                                        });
                                                    }

                                                }
                                            }
                                        });
                                        alumnos.remove(listaAlumnos.get(position).getIdAlumno());
                                        cole.getAulas().get(idAula).getListadoAlumnos().remove(listaAlumnos.get(position).getIdAlumno());
                                        listaAlumnos.remove(position);
                                        cole.setAlumnado(alumnos);
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

        if (!alumnos.containsKey(idAlumno)) {
            Alumno alumno = new Alumno(idAlumno, nombreAlumno, email, new ArrayList<String>(), idAula);
            cole.getAlumnado().put(idAlumno, alumno);
            cole.getAulas().get(idAula).getListadoAlumnos().add(idAlumno);
            subirDatos();
        } else {
            Toast.makeText(getContext(), "Ya existe un alumno con este ID", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarDatos() {
        listaAlumnos = new ArrayList<>();
        for (Alumno alumno : alumnos.values()) {
            if (alumno.getIdAula().equals(idAula)) {
                listaAlumnos.add(alumno);
            }
        }
        ArrayAdapterAlumnos adapterInicio = new ArrayAdapterAlumnos(getContext(), listaAlumnos);
        listAlumno.setAdapter(adapterInicio);
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
package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ModificarColegio extends AppCompatActivity implements Dialogo_aula.ResultadoDialogoAula, Dialogo_profe.ResultadoDialogoProfe, Dialogo_eliminar_aula.ResultadoDialogoEliminarAula, Dialogo_eliminar_profe.ResultadoDialogoEliminarProfe, Dialogo_modificar_profe.ResultadoDialogoModificarProfe {

    TextView txtIdColeMod, lbEliminarProfe, lbEliminarAula, lbAnadirAula, lbAnadirProfe, lbModificarProfe, lbReiniciarCurso;
    FloatingActionButton fab_eliminarProfe, fab_eliminarAula, fab_modificarProfe, fab_añadirAula, fab_añadirProfe, fab_opciones, btnCerrarSesion,fab_reiniciarCurso;
    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    Context context;
    Colegio cole;
    Usuario usuario;
    HashMap<String, Usuario> users;
    HashMap<String, Aula> aulas;
    HashMap<String, Profesor> profesorado;
    ArrayList<String> listadoAulas, listadoProfes;
    ArrayList<String> arrayAlumnos;
    String idColegio;
    FirebaseAuth mAuth;
    FirebaseFirestore database;

    boolean menuAbierto = false;

    private ProgressDialog progressDialog;


    public void mostrarDialogo() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Reinicio curso");
        progressDialog.setMessage("Reiniciando el curso...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_colegio);

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idColegio = getIntent().getStringExtra("idcole");


        context = this;
        listadoAulas = new ArrayList<>();
        listadoProfes = new ArrayList<>();
        users = new HashMap<>();
        progressDialog = new ProgressDialog(ModificarColegio.this);




        database.collection("Colegios").document(idColegio).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cole = document.toObject(Colegio.class);
                        aulas = cole.getAulas();
                        profesorado = cole.getProfesorado();
                    }
                }
            }
        });

        txtIdColeMod = findViewById(R.id.txtIdColeMod);
        fab_añadirAula = findViewById(R.id.fab_añadirAula);
        fab_añadirProfe = findViewById(R.id.fab_añadirProfe);
        fab_eliminarAula = findViewById(R.id.fab_eliminarAula);
        fab_eliminarProfe = findViewById(R.id.fab_EliminarProfe);
        fab_modificarProfe = findViewById(R.id.fab_ModificarProfe);
        fab_reiniciarCurso = findViewById(R.id.fab_reiniciarCurso);
        fab_opciones = findViewById(R.id.fab_opciones);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        lbEliminarProfe = findViewById((R.id.lbEliminarProfe));
        lbEliminarAula = findViewById(R.id.lbEliminarAula);
        lbAnadirProfe = findViewById(R.id.lbAnadirProfe);
        lbAnadirAula = findViewById(R.id.lbAnadirAula);
        lbModificarProfe = findViewById(R.id.lbModificarProfe);
        lbReiniciarCurso = findViewById(R.id.lbReiniciarCurso);

        //El setAlpha y el setTranslation es para la animación del boton flotante
        fab_añadirAula.setAlpha(0f);
        fab_añadirProfe.setAlpha(0f);
        fab_eliminarAula.setAlpha(0f);
        fab_eliminarProfe.setAlpha(0f);
        fab_modificarProfe.setAlpha(0f);
        fab_reiniciarCurso.setAlpha(0f);
        lbEliminarProfe.setAlpha(0f);
        lbEliminarAula.setAlpha(0f);
        lbAnadirProfe.setAlpha(0f);
        lbAnadirAula.setAlpha(0f);
        lbModificarProfe.setAlpha(0f);
        lbReiniciarCurso.setAlpha(0f);

        fab_añadirAula.setTranslationY(translationY);
        fab_añadirProfe.setTranslationY(translationY);
        fab_eliminarAula.setTranslationY(translationY);
        fab_eliminarProfe.setTranslationY(translationY);
        fab_modificarProfe.setTranslationY(translationY);
        fab_reiniciarCurso.setTranslationY(translationY);
        lbEliminarProfe.setTranslationY(translationY);
        lbEliminarAula.setTranslationY(translationY);
        lbAnadirProfe.setTranslationY(translationY);
        lbAnadirAula.setTranslationY(translationY);
        lbModificarProfe.setTranslationY(translationY);
        lbReiniciarCurso.setTranslationY(translationY);

        txtIdColeMod.setText(idColegio);

        //Inicia el dialogo para añadir aula
        fab_añadirAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_aula(context, ModificarColegio.this);
            }
        });

        //Carga un arrayadapter de los ID de las aulas y se pasa al dialogo para cargar su spinner
        fab_añadirProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_profe(context, ModificarColegio.this, cargarListados("aulas"));
            }
        });

        //Carga un arrayadapter de los ID de las aulas y se pasa al dialogo para cargar su spinner
        fab_eliminarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_eliminar_aula(context, ModificarColegio.this, cargarListados("aulas"));
            }
        });

        //Carga un arrayadapter de los ID de las profesores y se pasa al dialogo para cargar su spinner
        fab_eliminarProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_eliminar_profe(context, ModificarColegio.this, cargarListados("profes"));
            }
        });

        fab_modificarProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_modificar_profe(context, ModificarColegio.this, cargarListados("profes"), cargarListados("aulas"));
            }
        });

        fab_reiniciarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModificarColegio.this);
                builder.setTitle("Reiniciar curso")
                        .setMessage("¿Estás seguro de que quieres reiniciar el curso?")
                        .setPositiveButton("Sí",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reiniciarCurso();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog dialog2 = builder.create();
                dialog2.show();
            }
        });

        //Esto es para que cuando se pulse el boton flotante muestre o no el menu desplegado
        fab_opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_opciones) {
                    if (menuAbierto) {
                        cierraMenu();
                    } else {
                        abreMenu();
                    }
                }
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cole.setProfesorado(profesorado);
                cole.setAulas(aulas);

                database.collection("Colegios").document(cole.getIdColegio()).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ModificarColegio.this, "Colegio modificado con éxito", Toast.LENGTH_SHORT).show();
                            CerrarSesion.cerrarSesion(mAuth);
                            Intent intent = new Intent(ModificarColegio.this, Principal.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ModificarColegio.this, "Fallo al modificar el colegio", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    //Crea los listados necesarios para los spinners de los dialogos
    public ArrayAdapter<String> cargarListados(String listado) {

        ArrayAdapter<String> adapter = null;

        if (listado.equals("aulas")) {
            Set<String> setAulas = aulas.keySet();
            listadoAulas = new ArrayList<>(setAulas);
            adapter = new ArrayAdapter<>(ModificarColegio.this, android.R.layout.simple_spinner_item, listadoAulas);


        } else if (listado.equals("profes")) {
            Set<String> profes = profesorado.keySet();
            listadoProfes = new ArrayList<>(profes);
            adapter = new ArrayAdapter<>(ModificarColegio.this, android.R.layout.simple_spinner_item, listadoProfes);

        }
        return adapter;
    }

    public void reiniciarCurso(){
        mostrarDialogo();
        arrayAlumnos = new ArrayList<>(cole.getAlumnado().keySet());
        database.collection("users").whereEqualTo("rol", "Alumno").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        users.put(usuario.getEmail(), usuario);
                    }
                    for(int i = 0; i < arrayAlumnos.size(); i++){
                        Alumno alumno = cole.getAlumnado().get(arrayAlumnos.get(i));
                        usuario = users.get(alumno.getEmail());
                        cole.getAlumnado().remove(alumno.getIdAlumno());
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
                    for (Aula aula : cole.getAulas().values()){
                        aula.getListadoAlumnos().clear();
                        aula.getListadoPrestamos().clear();
                    }
                    database.collection("Colegios").document(idColegio).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ModificarColegio.this, "Reinicio completado. Vuelve a iniciar sesión", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(ModificarColegio.this, IniciarSesion.class));
                            finish();
                        }
                    });
                }
            }
        });
    }

    public void abreMenu() {
        menuAbierto = !menuAbierto;

        fab_opciones.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fab_añadirAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_añadirProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_modificarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_reiniciarCurso.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


        lbEliminarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbEliminarAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbModificarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbReiniciarCurso.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    public void cierraMenu() {
        menuAbierto = !menuAbierto;

        fab_opciones.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fab_añadirAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_añadirProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_modificarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_reiniciarCurso.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

        lbEliminarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbEliminarAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbModificarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbReiniciarCurso.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }


    @Override
    public void ResultadoDialogoAula(String idAula) {
        if (aulas.containsKey(idAula)) {
            Toast.makeText(ModificarColegio.this, "Aula ya existe", Toast.LENGTH_LONG).show();
        } else {
            Aula aula = new Aula(idAula, new HashMap<String, Libro>(), new ArrayList<String>(), new HashMap<String, Prestamo>());
            aulas.put(idAula, aula);
            Toast.makeText(ModificarColegio.this, "Aula creada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void ResultadoDialogoProfe(String idProfe, String idAula) {

        if (profesorado.containsKey(idProfe)) {
            Toast.makeText(ModificarColegio.this, "Profesor ya existe", Toast.LENGTH_LONG).show();
        } else {
            Profesor profe = new Profesor();
            profe.setIdProfesor(idProfe);
            profe.setIdAula(idAula);
            profesorado.put(idProfe, profe);
            Toast.makeText(ModificarColegio.this, "Profesor creado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void ResultadoDialogoEliminarAula(String idAula) {
        aulas.remove(idAula);
    }

    @Override
    public void ResultadoDialogoEliminarProfe(final String idProfe) {
        database.collection("users").whereEqualTo("idUsuario", idProfe).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
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
        profesorado.remove(idProfe);
    }

    @Override
    public void ResultadoDialogoModificarProfe(String idProfe, String idAula) {
        profesorado.get(idProfe).setIdAula(idAula);
    }
}

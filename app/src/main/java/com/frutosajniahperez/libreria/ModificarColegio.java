package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ModificarColegio extends AppCompatActivity implements Dialogo_aula.ResultadoDialogoAula, Dialogo_profe.ResultadoDialogoProfe, Dialogo_eliminar_aula.ResultadoDialogoEliminarAula, Dialogo_eliminar_profe.ResultadoDialogoEliminarProfe, Dialogo_modificar_profe.ResultadoDialogoModificarProfe {

    TextView txtIdColeMod, lbEliminarProfe, lbEliminarAula, lbAnadirAula, lbAnadirProfe, lbModificarProfe;
    FloatingActionButton fab_eliminarProfe, fab_eliminarAula, fab_modificarProfe, fab_añadirAula, fab_añadirProfe, fab_opciones, btnCerrarSesion;
    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    Context context;
    Colegio cole;
    HashMap<String, Aula> aulas;
    HashMap<String, Profesor> profesorado;
    ArrayList<String> listadoAulas, listadoProfes;
    java.lang.String idColegio;
    FirebaseAuth mAuth;

    boolean menuAbierto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_colegio);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idColegio = getIntent().getStringExtra("idcole");


        context = this;
        listadoAulas = new ArrayList<>();
        listadoProfes = new ArrayList<>();


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
        fab_opciones = findViewById(R.id.fab_opciones);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        lbEliminarProfe = findViewById((R.id.lbEliminarProfe));
        lbEliminarAula = findViewById(R.id.lbEliminarAula);
        lbAnadirProfe = findViewById(R.id.lbAnadirProfe);
        lbAnadirAula = findViewById(R.id.lbAnadirAula);
        lbModificarProfe = findViewById(R.id.lbModificarProfe);


        //El setAlpha y el setTranslation es para la animación del boton flotante
        fab_añadirAula.setAlpha(0f);
        fab_añadirProfe.setAlpha(0f);
        fab_eliminarAula.setAlpha(0f);
        fab_eliminarProfe.setAlpha(0f);
        fab_modificarProfe.setAlpha(0f);
        lbEliminarProfe.setAlpha(0f);
        lbEliminarAula.setAlpha(0f);
        lbAnadirProfe.setAlpha(0f);
        lbAnadirAula.setAlpha(0f);
        lbModificarProfe.setAlpha(0f);

        fab_añadirAula.setTranslationY(translationY);
        fab_añadirProfe.setTranslationY(translationY);
        fab_eliminarAula.setTranslationY(translationY);
        fab_eliminarProfe.setTranslationY(translationY);
        fab_modificarProfe.setTranslationY(translationY);
        lbEliminarProfe.setTranslationY(translationY);
        lbEliminarAula.setTranslationY(translationY);
        lbAnadirProfe.setTranslationY(translationY);
        lbAnadirAula.setTranslationY(translationY);
        lbModificarProfe.setTranslationY(translationY);

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

    public void abreMenu() {
        menuAbierto = !menuAbierto;

        fab_opciones.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fab_añadirAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_añadirProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_modificarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

        lbEliminarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbEliminarAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbModificarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    public void cierraMenu() {
        menuAbierto = !menuAbierto;

        fab_opciones.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fab_añadirAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_añadirProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_modificarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

        lbEliminarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbEliminarAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbModificarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }


    @Override
    public void ResultadoDialogoAula(String idAula) {
        if (aulas.containsKey(idAula)) {
            Toast.makeText(ModificarColegio.this, "Aula ya existe", Toast.LENGTH_LONG).show();
        } else {
            Aula aula = new Aula();
            aula.setIdAula(idAula);
            aula.setLibreria(new HashMap<String, Libro>());
            aula.setListadoAlumnos(new HashMap<String, Alumno>());
            aula.setListadoPrestamos(new HashMap<String, Prestamo>());
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
    public void ResultadoDialogoEliminarProfe(String idProfe) {
        profesorado.remove(idProfe);
    }

    @Override
    public void ResultadoDialogoModificarProfe(String idProfe, String idAula) {
        profesorado.get(idProfe).setIdAula(idAula);
    }
}

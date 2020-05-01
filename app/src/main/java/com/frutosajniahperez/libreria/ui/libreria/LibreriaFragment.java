package com.frutosajniahperez.libreria.ui.libreria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;
import com.frutosajniahperez.libreria.ui.alumnos.ArrayAdapterAlumnos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class LibreriaFragment extends Fragment implements Dialogo_busqueda_google.ResultadoDialogoBusquedaGoogle, Dialogo_libreria_manual.ResultadoDialogoBusqueda {

    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    FloatingActionButton fab_opcionesLibro, fab_anadirLibroManual, btnAnadirGoogle;
    TextView lbAnadirLibroManual, lbAnadirLibroGoogle;
    String idCole, idAula, idProfe;
    FirebaseFirestore database;
    Colegio cole;
    HashMap<String, Libro> libreria;
    ArrayList<Libro> libros;
    ListView listLibros;

    //CAMBIAR EL FONDO CUANDO PASE FECHA || AVISAR SI VA A REPETIR LIBRO || COMENZAR CON ALUMNO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    boolean menuAbierto = false;

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
        View root = inflater.inflate(R.layout.fragment_libreria, container, false);

        listLibros = root.findViewById(R.id.listaLibros);

        fab_opcionesLibro = root.findViewById(R.id.fab_opciones_libro);
        fab_anadirLibroManual = root.findViewById(R.id.fab_anadirLibroManual);
        btnAnadirGoogle = root.findViewById(R.id.fab_anadirLibroGoogle);


        lbAnadirLibroGoogle = root.findViewById(R.id.lbAnadirLibroGoogle);
        lbAnadirLibroManual = root.findViewById(R.id.lbAnadirLibroManual);

        //El setAlpha y el setTranslation es para la animación del boton flotante
        btnAnadirGoogle.setAlpha(0f);
        fab_anadirLibroManual.setAlpha(0f);
        lbAnadirLibroManual.setAlpha(0f);
        lbAnadirLibroGoogle.setAlpha(0f);

        btnAnadirGoogle.setTranslationY(translationY);
        fab_anadirLibroManual.setTranslationY(translationY);
        lbAnadirLibroManual.setTranslationY(translationY);
        lbAnadirLibroGoogle.setTranslationY(translationY);

        //Esto es para que cuando se pulse el boton flotante muestre o no el menu desplegado
        fab_opcionesLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (menuAbierto) {
                        cierraMenu();
                    } else {
                        abreMenu();

                }
            }
        });

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
                            libreria = cole.getAulas().get(idAula).getLibreria();
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

        listLibros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Borrado")
                        .setMessage("¿Seguro que quieres eliminar este libro?")
                        .setPositiveButton("Sí",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        libreria.remove(libros.get(position).getIsbn());
                                        libros.remove(position);
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

        btnAnadirGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_busqueda_google(getContext(), LibreriaFragment.this);
            }
        });

        fab_anadirLibroManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_libreria_manual(getContext(), LibreriaFragment.this);
            }
        });

        return root;
    }

    @Override
    public void ResultadoDialogoBusquedaGoogle(Libro libro) {
        subirLibro(libro);
    }

    @Override
    public void ResultadoDialogoBusqueda(Libro libro) {
        subirLibro(libro);
    }

    public void subirLibro(Libro libro){
        if (!cole.getAulas().get(idAula).getLibreria().containsKey(libro.getIsbn())) {
            libreria.put(libro.getIsbn(), libro);
            cole.getAulas().get(idAula).setLibreria(libreria);
            subirDatos();
        } else {
            Toast.makeText(getContext(), "Este libro ya existe en tu aula", Toast.LENGTH_SHORT).show();
        }
    }

    public void abreMenu() {
        menuAbierto = !menuAbierto;

        fab_opcionesLibro.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fab_anadirLibroManual.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        btnAnadirGoogle.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirLibroGoogle.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirLibroManual.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    public void cierraMenu() {
        menuAbierto = !menuAbierto;

        fab_opcionesLibro.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fab_anadirLibroManual.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        btnAnadirGoogle.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirLibroGoogle.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirLibroManual.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    public void cargarDatos() {
        libros = new ArrayList<>(libreria.values());
        ArrayAdapterLibreria adapterInicio = new ArrayAdapterLibreria(getContext(), libros);
        listLibros.setAdapter(adapterInicio);
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

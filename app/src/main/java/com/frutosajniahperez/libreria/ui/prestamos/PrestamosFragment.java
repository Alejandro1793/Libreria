package com.frutosajniahperez.libreria.ui.prestamos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrestamosFragment extends Fragment {

    private String idCole, idAula, idProfe;
    private Colegio cole;
    private HashMap<String, Alumno> alumnado;
    private HashMap<String, Libro> libreria;
    private ArrayList<String> listadoAlumnos, listadoLibros;
    private FirebaseFirestore database;
    private Date fechaEntrega;
    private Spinner spAlumno, spTituloLibro;
    private TextView btnElegirFecha;
    private FloatingActionButton btnAceptarPrestamo;
    private boolean releer, sobreescribir;

    public PrestamosFragment() {
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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prestamos, container, false);
        spAlumno = root.findViewById(R.id.spAlumno);
        spTituloLibro = root.findViewById(R.id.spTituloLibro);
        btnAceptarPrestamo = root.findViewById(R.id.btnAceptarPrestamo);
        btnElegirFecha = root.findViewById(R.id.btnElegirFecha);
        listadoAlumnos = new ArrayList<>();
        listadoLibros = new ArrayList<>();
        Calendar.getInstance();

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
                            alumnado = cole.getAlumnado();
                            libreria = cole.getAulas().get(idAula).getLibreria();
                            if (!alumnado.isEmpty() && !libreria.isEmpty()) {
                                for (Alumno alumno : alumnado.values()) {
                                    if (alumno.getIdAula().equals(idAula)) {
                                        listadoAlumnos.add(alumno.getIdAlumno());
                                    }
                                }
                                ArrayAdapter<String> adapterAlumno = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listadoAlumnos);
                                spAlumno.setAdapter(adapterAlumno);
                                cargarDatosLibros();
                            } else {
                                Toast.makeText(getContext(), "Faltan datos que mostrar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }

        btnElegirFecha.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(requireContext());
                pickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String fecha = dayOfMonth + "-" + (month + 1) + "-" + year;
                        btnElegirFecha.setText(fecha);
                        try {
                            fechaEntrega = sdf.parse(fecha);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                pickerDialog.show();
            }
        });

        btnAceptarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fechaEntrega != null) {

                    releer = alumnado.get(spAlumno.getSelectedItem().toString()).getLibrosLeidos().contains(spTituloLibro.getSelectedItem().toString());
                    sobreescribir = cole.getAulas().get(idAula).getListadoPrestamos().containsKey(spAlumno.getSelectedItem().toString());

                    if (!sobreescribir && !releer) {
                        subirPrestamo();
                        cargarDatosLibros();
                    } else if (sobreescribir && !releer){
                        crearDialogoSobreescribir();
                    } else if (!sobreescribir){
                        crearDialogoReleer();
                    } else {
                        crearDialogoSobreescribir();
                    }
                } else {
                    Toast.makeText(getContext(), "Debes seleccionar una fecha de entrega", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    public void cargarDatosLibros() {
        btnAceptarPrestamo.setEnabled(false);
        listadoLibros = new ArrayList<>();
        for (Libro libro : libreria.values()) {
            if (!libro.isPrestado()) {
                listadoLibros.add(libro.getTitulo());
            }
        }
        ArrayAdapter<String> adapterLibro = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listadoLibros);
        spTituloLibro.setAdapter(adapterLibro);
        if (!listadoLibros.isEmpty()) {
            btnAceptarPrestamo.setEnabled(true);
        } else {
            Toast.makeText(getContext(), "Todos los libros están prestados", Toast.LENGTH_SHORT).show();
        }

    }

    public void subirPrestamo() {
        cole.getAulas().get(idAula).getListadoPrestamos().put(spAlumno.getSelectedItem().toString(), new Prestamo(spAlumno.getSelectedItem().toString(), spTituloLibro.getSelectedItem().toString(), new Timestamp(new Date().getTime()), new Timestamp(fechaEntrega.getTime())));
        for (Libro libro : libreria.values()) {
            if (libro.getTitulo().equals(spTituloLibro.getSelectedItem().toString())) {
                libro.setPrestado(true);
                break;
            }
        }
        database.collection("Colegios").document(idCole).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Préstamo guardado", Toast.LENGTH_SHORT).show();
                    btnElegirFecha.setText(R.string.selecciona_la_fecha_de_entrega);
                }
            }
        });
    }

    public void subirPrestamo(String sustituto) {
        cole.getAulas().get(idAula).getListadoPrestamos().put(spAlumno.getSelectedItem().toString(), new Prestamo(spAlumno.getSelectedItem().toString(), spTituloLibro.getSelectedItem().toString(), new Timestamp(new Date().getTime()), new Timestamp(fechaEntrega.getTime())));
        int contFor = 0;
        for (Libro libro : libreria.values()) {
            if (libro.getTitulo().equals(spTituloLibro.getSelectedItem().toString())) {
                libro.setPrestado(true);
                contFor++;
            }
            if (libro.getTitulo().equals(sustituto)) {
                libro.setPrestado(false);
                contFor++;
            }
            if (contFor == 2) {
                break;
            }
        }
        database.collection("Colegios").document(idCole).set(cole).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Préstamo guardado", Toast.LENGTH_SHORT).show();
                    btnElegirFecha.setText(R.string.selecciona_la_fecha_de_entrega);
                }
            }
        });
    }

    public void crearDialogoReleer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Libro leído")
                .setMessage("Este alumno ya ha leído este libro. ¿Seguro que quiere volver a leerlo?")
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                subirPrestamo();
                                cargarDatosLibros();
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
    }

    public void crearDialogoSobreescribir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sobreescribir")
                .setMessage("Este alumno ya tiene un préstamo activo. ¿Seguro que quieres sobreescribirlo?")
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (releer){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Libro leído")
                                            .setMessage("Este alumno ya ha leído este libro. ¿Seguro que quiere volver a leerlo?")
                                            .setPositiveButton("Sí",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String libroSust = cole.getAulas().get(idAula).getListadoPrestamos().get(spAlumno.getSelectedItem().toString()).getLibro();
                                                            subirPrestamo(libroSust);
                                                            cargarDatosLibros();
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
                                } else {
                                    String libroSust = cole.getAulas().get(idAula).getListadoPrestamos().get(spAlumno.getSelectedItem().toString()).getLibro();
                                    subirPrestamo(libroSust);
                                    cargarDatosLibros();
                                }
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
    }

}

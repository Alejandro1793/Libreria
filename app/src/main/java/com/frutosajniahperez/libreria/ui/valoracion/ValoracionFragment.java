package com.frutosajniahperez.libreria.ui.valoracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.text.DecimalFormat;
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
    private ImageView star1, star2, star3, star4, star5;
    private Button btnEnviarValoracion;
    private TextView tvPuntuacion;
    private int valoracion = 1;
    private boolean puntuado = false;
    private final DecimalFormat df = new DecimalFormat("0.0");

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
        for (Libro lib : libreria.values()) {
            for (String titulo : alumno.getLibrosLeidos()) {
                if (lib.getTitulo().equals(titulo)) {
                    libros.add(lib);
                    break;
                }
            }
        }

        listValoracion.setAdapter(new ArrayAdapterValoracion(getContext(), libros, idCole, alumno.getIdAula()) {
            @Override
            public void onLibro(final Libro libroLista, View v, int posicion) {

                star1 = v.findViewById(R.id.ivEstrella1);
                star1.setTag("star1" + posicion);
                star2 = v.findViewById(R.id.ivEstrella2);
                star2.setTag("star2" + posicion);
                star3 = v.findViewById(R.id.ivEstrella3);
                star3.setTag("star3" + posicion);
                star4 = v.findViewById(R.id.ivEstrella4);
                star4.setTag("star4" + posicion);
                star5 = v.findViewById(R.id.ivEstrella5);
                star5.setTag("star5" + posicion);
                btnEnviarValoracion = v.findViewById(R.id.btEnviarValoracion);
                btnEnviarValoracion.setTag("btn" + posicion);

                for (String librosPunt : alumno.getLibrosPuntuados().keySet()) {
                    if (librosPunt.equals(libroLista.getTitulo())) {
                        puntuado = true;
                        valoracion = alumno.getLibrosPuntuados().get(librosPunt);
                        break;
                    }
                }
                if (!puntuado) {
                    star1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            valoracion = 1;
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_off);
                            star3.setImageResource(android.R.drawable.btn_star_big_off);
                            star4.setImageResource(android.R.drawable.btn_star_big_off);
                            star5.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    });

                    star2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            valoracion = 2;
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_off);
                            star4.setImageResource(android.R.drawable.btn_star_big_off);
                            star5.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    });

                    star3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            valoracion = 3;
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_on);
                            star4.setImageResource(android.R.drawable.btn_star_big_off);
                            star5.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    });

                    star4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            valoracion = 4;
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_on);
                            star4.setImageResource(android.R.drawable.btn_star_big_on);
                            star5.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    });

                    star5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            valoracion = 5;
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_on);
                            star4.setImageResource(android.R.drawable.btn_star_big_on);
                            star5.setImageResource(android.R.drawable.btn_star_big_on);
                        }
                    });

                    btnEnviarValoracion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alumno.getLibrosPuntuados().put(libroLista.getTitulo(), valoracion);
                            libroLista.setValoracion(valoracion);
                            tvPuntuacion.setText(df.format(libroLista.getValoracion()));
                            cole.getAulas().get(alumno.getIdAula()).getLibreria().put(libroLista.getIsbn(), libroLista);
                            database.collection("Colegios").document(idCole).set(cole);
                            btnEnviarValoracion.setEnabled(false);
                        }
                    });
                } else {
                    btnEnviarValoracion.setEnabled(false);
                    switch (valoracion) {
                        case 2:
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            break;
                        case 3:
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_on);
                            break;
                        case 4:
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_on);
                            star4.setImageResource(android.R.drawable.btn_star_big_on);
                            break;
                        case 5:
                            star1.setImageResource(android.R.drawable.btn_star_big_on);
                            star2.setImageResource(android.R.drawable.btn_star_big_on);
                            star3.setImageResource(android.R.drawable.btn_star_big_on);
                            star4.setImageResource(android.R.drawable.btn_star_big_on);
                            star5.setImageResource(android.R.drawable.btn_star_big_on);
                            break;
                    }
                }
            }
        });

        /*listValoracion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                star1 = view.findViewWithTag("star1" + position);
                star2 = view.findViewWithTag("star2" + position);
                star3 = view.findViewWithTag("star3" + position);
                star4 = view.findViewWithTag("star4" + position);
                star5 = view.findViewWithTag("star5" + position);
                btnEnviarValoracion = view.findViewWithTag("btn" + position);

                final Libro libroSeleccionado = (Libro) (parent.getItemAtPosition(position));
                tvPuntuacion = view.findViewWithTag("tv" + position);
                tvPuntuacion.setText(df.format(libroSeleccionado.getValoracion()));


            }
        });*/
    }
}

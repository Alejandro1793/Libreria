package com.frutosajniahperez.libreria.ui.valoracion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.frutosajniahperez.libreria.Alumno;
import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;
import com.frutosajniahperez.libreria.ui.libreria.CargarImagen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArrayAdapterValoracion extends ArrayAdapter<Libro> {

    private Button btnEnviarValoracion;
    private int valoracion;
    private TextView tvPuntuacion;
    private Colegio cole;
    private FirebaseFirestore database;
    private String idCole;
    private Alumno alumno;
    private DecimalFormat df;
    private List<Libro> libros;
    private boolean puntuado;

    public ArrayAdapterValoracion(@NonNull Context context, @NonNull List<Libro> objects, String idCole, Alumno alumno) {
        super(context, 0, objects);
        this.libros = objects;
        this.idCole = idCole;
        this.alumno = alumno;
        database = FirebaseFirestore.getInstance();
        database.collection("Colegios").document(this.idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cole = document.toObject(Colegio.class);
                    }
                }
            }
        });
        df = new DecimalFormat("0.0");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v;
        final ViewHolder viewHolder;
        if (convertView == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.lista_valoracion, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTitulo = v.findViewById(R.id.txtTituloLibroValoracion);
            viewHolder.txtValoracion = v.findViewById(R.id.txtPuntuacion);
            viewHolder.imagen = v.findViewById(R.id.ivPortadaValoracion);
            viewHolder.star1 = v.findViewById(R.id.ivEstrella1);
            viewHolder.star2 = v.findViewById(R.id.ivEstrella2);
            viewHolder.star3 = v.findViewById(R.id.ivEstrella3);
            viewHolder.star4 = v.findViewById(R.id.ivEstrella4);
            viewHolder.star5 = v.findViewById(R.id.ivEstrella5);
            viewHolder.btnEnviar = v.findViewById(R.id.btEnviarValoracion);
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
        }
        final Libro libro = getItem(position);

        for (String librosPunt : alumno.getLibrosPuntuados().keySet()) {
            if (librosPunt.equals(libro.getTitulo())) {
                puntuado = true;
                valoracion = alumno.getLibrosPuntuados().get(librosPunt);
                break;
            }
        }

        viewHolder.txtTitulo.setText(libro.getTitulo());
        viewHolder.txtValoracion.setText(df.format(libro.getValoracionMedia()));
        if (libro.getImagen().equals("Sin imagen")) {
            viewHolder.imagen.setImageResource(R.drawable.noimg);
        } else {
            try {
                viewHolder.imagen.setImageBitmap(new CargarImagen().execute(libro.getImagen()).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!puntuado) {
            viewHolder.star1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valoracion = 1;
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_off);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_off);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_off);
                    viewHolder.star5.setImageResource(android.R.drawable.btn_star_big_off);
                }
            });

            viewHolder.star2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valoracion = 2;
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_off);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_off);
                    viewHolder.star5.setImageResource(android.R.drawable.btn_star_big_off);
                }
            });

            viewHolder.star3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valoracion = 3;
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_off);
                    viewHolder.star5.setImageResource(android.R.drawable.btn_star_big_off);
                }
            });

            viewHolder.star4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valoracion = 4;
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star5.setImageResource(android.R.drawable.btn_star_big_off);
                }
            });

            viewHolder.star5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valoracion = 5;
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star5.setImageResource(android.R.drawable.btn_star_big_on);
                }
            });

            viewHolder.btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alumno.getLibrosPuntuados().put(getItem(position).getTitulo(), valoracion);
                    getItem(position).añadirValoracion(valoracion);
                    viewHolder.txtValoracion.setText(df.format(getItem(position).getValoracionMedia()));
                    cole.getAlumnado().put(alumno.getIdAlumno(),alumno);
                    cole.getAulas().get(alumno.getIdAula()).getLibreria().put(getItem(position).getIsbn(), getItem(position));
                    database.collection("Colegios").document(idCole).set(cole);
                    viewHolder.btnEnviar.setEnabled(false);
                    viewHolder.star1.setClickable(false);
                    viewHolder.star2.setClickable(false);
                    viewHolder.star3.setClickable(false);
                    viewHolder.star4.setClickable(false);
                    viewHolder.star5.setClickable(false);
                }
            });

        } else {
            viewHolder.btnEnviar.setEnabled(false);
            switch (valoracion) {
                case 2:
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    break;
                case 3:
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_on);
                    break;
                case 4:
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_on);
                    break;
                case 5:
                    viewHolder.star1.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star2.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star3.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star4.setImageResource(android.R.drawable.btn_star_big_on);
                    viewHolder.star5.setImageResource(android.R.drawable.btn_star_big_on);
                    break;
            }
        }
        return v;
    }
}

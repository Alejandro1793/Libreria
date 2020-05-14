package com.frutosajniahperez.libreria.ui.valoracion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public abstract class ArrayAdapterValoracion extends ArrayAdapter<Libro> {

    private ImageView star1, star2, star3, star4, star5;
    private Button btnEnviarValoracion;
    private int valoracion;
    private TextView tvPuntuacion;
    private Colegio cole;
    private FirebaseFirestore database;
    private String idCole, idAula;
    private DecimalFormat df;
    private List<Libro> libros;

    public ArrayAdapterValoracion(@NonNull Context context, @NonNull List<Libro> objects, String idCole, String idAula) {
        super(context, 0, objects);
        this.libros = objects;
        this.idCole = idCole;
        this.idAula = idAula;
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

        Libro libro = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_valoracion, parent, false);
        onLibro(libros.get(position), v, position);
        TextView tv = v.findViewById(R.id.txtTituloLibroValoracion);
        tv.setText(libro.getTitulo());
        ImageView iv = v.findViewById(R.id.ivPortadaValoracion);
        tvPuntuacion = v.findViewById(R.id.txtPuntuacion);
        tvPuntuacion.setTag("tv" + position);
        tvPuntuacion.setText(df.format(libro.getValoracion()));
        if (libro.getImagen().equals("Sin imagen")) {
            iv.setImageResource(R.drawable.noimg);
        } else {
            try {
                iv.setImageBitmap(new CargarImagen().execute(libro.getImagen()).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    public abstract void onLibro(Libro libro, View view, int position);
}

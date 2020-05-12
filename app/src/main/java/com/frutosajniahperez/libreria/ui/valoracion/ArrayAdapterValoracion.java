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

public class ArrayAdapterValoracion extends ArrayAdapter<Libro> {

    ImageView star1, star2, star3, star4, star5;
    Button btnEnviarValoracion;
    int valoracion;
    TextView tv, tvPuntuacion;
    Colegio cole;
    FirebaseFirestore database;
    String idCole, idAula;
    DecimalFormat df;

    public ArrayAdapterValoracion(@NonNull Context context, @NonNull List<Libro> objects, String idCole, String idAula) {
        super(context, 0, objects);
        this.idCole = idCole;
        this.idAula = idAula;
        database = FirebaseFirestore.getInstance();
        df = new DecimalFormat("#.0");
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
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Libro libro = getItem(position);
        View v;
        v = LayoutInflater.from(getContext()).inflate(R.layout.lista_valoracion, parent, false);
        tv = v.findViewById(R.id.txtTituloLibroValoracion);
        tv.setText(libro.getTitulo());
        tvPuntuacion = v.findViewById(R.id.txtPuntuacion);
        tvPuntuacion.setText(df.format(getItem(position).getValoracion()));
        ImageView iv = v.findViewById(R.id.ivPortadaValoracion);
        if (libro.getImagen().equals("Sin imagen")){
            iv.setImageResource(R.drawable.noimg);
        } else {
            try {
                iv.setImageBitmap(new CargarImagen().execute(libro.getImagen()).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        star1 = v.findViewById(R.id.ivEstrella1);
        star2 = v.findViewById(R.id.ivEstrella2);
        star3 = v.findViewById(R.id.ivEstrella3);
        star4 = v.findViewById(R.id.ivEstrella4);
        star5 = v.findViewById(R.id.ivEstrella5);
        btnEnviarValoracion = v.findViewById(R.id.btEnviarValoracion);

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
                getItem(position).setValoracion(valoracion);
                tvPuntuacion.setText(df.format(getItem(position).getValoracion()));
                cole.getAulas().get(idAula).getLibreria().put(getItem(position).getIsbn(), getItem(position));
                database.collection("Colegios").document(idCole).set(cole);
                btnEnviarValoracion.setEnabled(false);
                //ELIMINAR EL LIBRO CUANDO VALORE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });
        return v;
    }


}

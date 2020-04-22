package com.frutosajniahperez.libreria.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.frutosajniahperez.libreria.Colegio;
import com.frutosajniahperez.libreria.Prestamo;
import com.frutosajniahperez.libreria.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    String idCole, idProfe;
    Colegio cole;
    Map<String, Prestamo> prestamos;
    ArrayList<Prestamo> listaPrestamos;
    FirebaseFirestore database;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCole = getArguments().getString("idcole");
            idProfe = getArguments().getString("idprofe");
        }
        database = FirebaseFirestore.getInstance();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView lv = root.findViewById(R.id.listInicio);
        //Cargar lista de préstamos de la base de datos
        if (idCole != null) {
            database.collection("Colegios").document(idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            cole = document.toObject(Colegio.class);
                            prestamos = cole.getProfesorado().get(idProfe).getAula().getListadoPrestamos();
                            if (prestamos == null) {
                                Toast.makeText(getContext(), "Todavía no hay préstamos", Toast.LENGTH_SHORT).show();
                            } else {
                                listaPrestamos = new ArrayList<>(prestamos.values());
                                ArrayAdapterInicio adapterInicio = new ArrayAdapterInicio(getContext(), listaPrestamos);
                                lv.setAdapter(adapterInicio);
                            }
                        }
                    }
                }
            });
        }
        return root;
    }
}

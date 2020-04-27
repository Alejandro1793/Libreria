package com.frutosajniahperez.libreria.ui.libreria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LibreriaFragment extends Fragment implements Dialogo_busqueda_google.ResultadoDialogoBusquedaGoogle {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_libreria, container, false);
        FloatingActionButton btnAnadirGoogle = root.findViewById(R.id.fab_añadirLibroGoogle);
        btnAnadirGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_busqueda_google(getContext(), LibreriaFragment.this);
            }
        });

        return root;
    }

    @Override
    public void ResultadoDialogoBusquedaGoogle(Libro libro) {

    }
}

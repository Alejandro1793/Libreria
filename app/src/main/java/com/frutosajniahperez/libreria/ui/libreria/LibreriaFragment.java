package com.frutosajniahperez.libreria.ui.libreria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.frutosajniahperez.libreria.Libro;
import com.frutosajniahperez.libreria.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LibreriaFragment extends Fragment implements Dialogo_busqueda_google.ResultadoDialogoBusquedaGoogle {

    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    FloatingActionButton fab_opcionesLibro, fab_anadirLibroManual, btnAnadirGoogle;
    TextView lbAnadirLibroManual, lbAnadirLibroGoogle;

    boolean menuAbierto = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_libreria, container, false);

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
}

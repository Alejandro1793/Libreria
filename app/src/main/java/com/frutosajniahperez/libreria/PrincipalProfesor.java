package com.frutosajniahperez.libreria;

import android.os.Bundle;
import android.view.MenuItem;

import com.frutosajniahperez.libreria.ui.alumnos.AlumnosFragment;
import com.frutosajniahperez.libreria.ui.inicio.InicioFragment;
import com.frutosajniahperez.libreria.ui.libreria.LibreriaFragment;
import com.frutosajniahperez.libreria.ui.prestamos.PrestamosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PrincipalProfesor extends AppCompatActivity {

    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_profesor);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        final String idCole = getIntent().getStringExtra("idcole");
        final String idAula = getIntent().getStringExtra("idaula");
        final String idProfe = getIntent().getStringExtra("idprofe");

        //Cargamos el bundle y lanzamos el fragment_inicio
        bundle.putString("idcole", idCole);
        bundle.putString("idaula", idAula);
        bundle.putString("idprofe", idProfe);
        InicioFragment inicio = new InicioFragment();
        inicio.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, inicio).commit();

        //Acciones a realizar cada vez que se selecciona un apartado del menú
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.navigation_inicio:
                        selectedFragment = new InicioFragment();
                        break;
                    case R.id.navigation_libreria:
                        selectedFragment = new LibreriaFragment();
                        break;
                    case R.id.navigation_alumnos:
                        selectedFragment = new AlumnosFragment();
                        break;
                    case R.id.navigation_prestamos:
                        selectedFragment = new PrestamosFragment();
                        break;
                }
                bundle.putString("idcole", idCole);
                bundle.putString("idaula", idAula);
                bundle.putString("idprofe", idProfe);
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
                return true;
            }
        });

    }


}

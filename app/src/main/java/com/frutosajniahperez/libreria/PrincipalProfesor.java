package com.frutosajniahperez.libreria;

import android.os.Bundle;
import android.view.MenuItem;

import com.frutosajniahperez.libreria.ui.alumnos.AlumnosFragment;
import com.frutosajniahperez.libreria.ui.home.HomeFragment;
import com.frutosajniahperez.libreria.ui.libreria.LibreriaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class PrincipalProfesor extends AppCompatActivity {

    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_profesor);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        final String idCole = getIntent().getStringExtra("idcole");
        final String idProfe = getIntent().getStringExtra("idprofe");

        //Cargamos el bundle y lanzamos el fragment_home
        bundle.putString("idcole", idCole);
        bundle.putString("idprofe", idProfe);
        HomeFragment inicio = new HomeFragment();
        inicio.setArguments(bundle);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, inicio).commit();

        //Acciones a realizar cada vez que se selecciona un apartado del menú
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        bundle.putString("idcole", idCole);
                        bundle.putString("idprofe", idProfe);
                        break;
                    case R.id.navigation_libreria:
                        selectedFragment = new LibreriaFragment();
                        break;
                    case R.id.navigation_alumnos:
                        selectedFragment = new AlumnosFragment();
                        break;
                }
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
                return true;
            }
        });

    }


}

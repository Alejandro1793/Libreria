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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_profesor);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        final String idCole = getIntent().getStringExtra("idcole");
        final String idProfe = getIntent().getStringExtra("idprofe");

        /*Bundle bundle = new Bundle();
        bundle.putString("idcole", idCole);
        bundle.putString("idprofe", idProfe);
        HomeFragment inicio = new HomeFragment();
        inicio.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, inicio).commit();*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_libreria, R.id.navigation_alumnos)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        /*navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                Bundle bundle = new Bundle();
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
        });*/

    }


}

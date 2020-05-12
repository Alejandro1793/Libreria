package com.frutosajniahperez.libreria;

import android.os.Bundle;
import android.view.MenuItem;

import com.frutosajniahperez.libreria.ui.home.HomeFragment;
import com.frutosajniahperez.libreria.ui.libreria_alumno.LibreriaAlumnoFragment;
import com.frutosajniahperez.libreria.ui.valoracion.ValoracionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PrincipalAlumno extends AppCompatActivity {

    Bundle bundle = new Bundle();
    private FirebaseFirestore database;
    private Colegio cole;
    private Alumno alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_alumno);
        BottomNavigationView navView = findViewById(R.id.nav_view_alumno);

        final String idCole = getIntent().getStringExtra("idcole");
        final String idAlumno = getIntent().getStringExtra("idalumno");
        database = FirebaseFirestore.getInstance();
        database.collection("Colegios").document(idCole).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        cole = documentSnapshot.toObject(Colegio.class);
                        alumno = cole.getAlumnado().get(idAlumno);
                        //Cargamos el bundle y lanzamos el fragment_inicio
                        bundle.putString("idcole", idCole);
                        bundle.putSerializable("alumno", alumno);
                        HomeFragment inicio = new HomeFragment();
                        inicio.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_alumno, inicio).commit();
                    }
                }
            }
        });


        //Acciones a realizar cada vez que se selecciona un apartado del menú
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_libreria_alumno:
                        selectedFragment = new LibreriaAlumnoFragment();
                        break;
                    case R.id.navigation_valoracion:
                        selectedFragment = new ValoracionFragment();
                        break;
                }
                bundle.putString("idcole", idCole);
                bundle.putSerializable("alumno", alumno);
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_alumno, selectedFragment).commit();
                return true;
            }
        });
    }

}

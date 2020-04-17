package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroProfesor extends AppCompatActivity {

    Button btnGenerar, btnAceptarDatos;
    TextView txtPassGenerada;
    EditText txtEmail, txtIdProfeRegistro;
    FirebaseAuth mAuth;
    ImageView btnRegresar;
    ArrayList<String> listadoColegios;
    ArrayAdapter<String> adapter;
    Colegio cole;
    Usuario usuario;
    String idCole;
    Map<String, Colegio> colegios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_profesor);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        listadoColegios = new ArrayList<>();
        colegios = new HashMap<>();

        btnGenerar = findViewById(R.id.btnGenerar);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtPassGenerada = findViewById(R.id.txtPassGenerada);
        txtEmail = findViewById(R.id.txtEmail);
        txtIdProfeRegistro = findViewById(R.id.txtIdProfeRegistro);
        btnAceptarDatos = findViewById(R.id.btnAceptarDatos);
        final Spinner spIdColegios = findViewById(R.id.spIdColegios);

        database.collection("Colegios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()){
                        cole = document.toObject(Colegio.class);
                        colegios.put(cole.getIdColegio(), cole);
                        listadoColegios.add(cole.getIdColegio());
                    }
                    adapter = new ArrayAdapter<>(RegistroProfesor.this, android.R.layout.simple_spinner_item, listadoColegios);
                    spIdColegios.setAdapter(adapter);
                }

            }
        });

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmail.getText().toString())) {
                    txtPassGenerada.setText(GeneradorContrasena.getPassword());
                    txtEmail.setEnabled(false);
                    btnGenerar.setEnabled(false);
                    btnAceptarDatos.setEnabled(true);
                } else {
                    Toast.makeText(RegistroProfesor.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                }
            }
        });

        btnAceptarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCole = spIdColegios.getSelectedItem().toString();
                cole = colegios.get(idCole);
                if (cole.getProfesorado().containsKey(txtIdProfeRegistro.getText().toString())){
                    mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassGenerada.getText().toString())
                            .addOnCompleteListener(RegistroProfesor.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registro del usuario realizado con éxito
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        usuario = new Usuario(txtEmail.getText().toString(), txtPassGenerada.getText().toString(), idCole, "Profesor");
                                        database.collection("users").document(txtEmail.getText().toString()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegistroProfesor.this, "Usuario creado con éxito.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        updateUI(user);
                                    } else {
                                        Toast.makeText(RegistroProfesor.this, "Error al registrar el usuario",
                                                Toast.LENGTH_SHORT).show();
                                        mAuth.getCurrentUser().delete();
                                        startActivity(new Intent(RegistroProfesor.this, EleccionRegistro.class));
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistroProfesor.this, "No estás registrado en este colegio como profesor. Habla con tu administrador", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroProfesor.this, Principal.class));
                finish();
            }
        });
    }

    public boolean comprobarEmail(String email) {

        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        return mather.find();
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(RegistroProfesor.this, Principal.class);
            intent.putExtra("idcole", idCole);
            startActivity(intent);
            finish();
        }

    }
}

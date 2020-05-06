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

public class RegistroAlumno extends AppCompatActivity {

    Button btnAceptarDatosAlumno;
    EditText txtEmailAlumno, txtIdAlumnoRegistro, txtContraseniaAlumno, txtIdAulaAlumno;
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
        setContentView(R.layout.activity_registro_alumno);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        listadoColegios = new ArrayList<>();
        colegios = new HashMap<>();

        btnRegresar = findViewById(R.id.btnRegresar);
        txtEmailAlumno = findViewById(R.id.txtEmailAlumno);
        txtIdAlumnoRegistro = findViewById(R.id.txtIdAlumnoRegistro);
        btnAceptarDatosAlumno = findViewById(R.id.btnAceptarDatosAlumno);
        txtContraseniaAlumno = findViewById((R.id.txtContraseniaAlumno));
        txtIdAulaAlumno = findViewById((R.id.txtIdAulaAlumno));
        final Spinner spIdColegios = findViewById(R.id.spIdColegioAlumno);

        database.collection("Colegios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()){
                        cole = document.toObject(Colegio.class);
                        colegios.put(cole.getIdColegio(), cole);
                        listadoColegios.add(cole.getIdColegio());
                    }
                    adapter = new ArrayAdapter<>(RegistroAlumno.this, android.R.layout.simple_spinner_item, listadoColegios);
                    spIdColegios.setAdapter(adapter);
                }

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroAlumno.this, Principal.class));
                finish();
            }
        });

        btnAceptarDatosAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmailAlumno.getText().toString())) {
                    txtEmailAlumno.setEnabled(false);
                } else {
                    Toast.makeText(RegistroAlumno.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmailAlumno.setText("");
                }
                if (comprobarContrasenia(txtContraseniaAlumno.getText().toString())) {
                    txtContraseniaAlumno.setEnabled(false);
                } else {
                    txtContraseniaAlumno.setText("");
                }
                idCole = spIdColegios.getSelectedItem().toString();
                cole = colegios.get(idCole);
                if (cole.getAulas().get(txtIdAulaAlumno.getText().toString()).getListadoAlumnos().containsKey(txtIdAlumnoRegistro.getText().toString())){
                    mAuth.createUserWithEmailAndPassword(txtEmailAlumno.getText().toString(), txtContraseniaAlumno.getText().toString())
                            .addOnCompleteListener(RegistroAlumno.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //Registro del usuario realizado con éxito
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        usuario = new Usuario(txtEmailAlumno.getText().toString(), txtContraseniaAlumno.getText().toString(), idCole, txtIdAlumnoRegistro.getText().toString(),"Alumno");
                                        database.collection("users").document(txtEmailAlumno.getText().toString()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegistroAlumno.this, "Usuario creado con éxito.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //updateUI(user);
                                    } else {
                                        Toast.makeText(RegistroAlumno.this, "Error al registrar el usuario",
                                                Toast.LENGTH_SHORT).show();
                                        mAuth.getCurrentUser().delete();
                                        startActivity(new Intent(RegistroAlumno.this, EleccionRegistro.class));
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistroAlumno.this, "No estás registrado en este colegio como alumno. Habla con tu administrador", Toast.LENGTH_LONG).show();
                }

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
            Intent intent = new Intent(RegistroAlumno.this, PrincipalProfesor.class);
            intent.putExtra("idcole", idCole);
            //intent.putExtra("idaula", cole.getProfesorado().get(txtIdProfeRegistro.getText().toString()).getIdAula());
            startActivity(intent);
            finish();
        }

    }

    public boolean comprobarContrasenia(String contrasenia) {
        boolean valida = true;

        int minuscula = 0;
        int mayuscula = 0;
        int numero = 0;
        int especial = 0;

        if (contrasenia.length() < 8 || contrasenia.length() > 16) {
            Toast.makeText(RegistroAlumno.this, "La contraseña debe tener 8-16 carácteres", Toast.LENGTH_LONG).show();
            return false; // tamaño
        }
        for (int i = 0; i < contrasenia.length(); i++) {
            char c = contrasenia.charAt(i);
            if (c <= ' ' || c > '~') {
                valida = false; //Espacio o fuera de rango
                break;
            }
            if ((c > ' ' && c < '0') || (c >= ':' && c < 'A') || (c >= '[' && c < 'a') || (c >= '{' && c < 127)) {
                especial++;
            }
            if (c >= '0' && c < ':') numero++;
            if (c >= 'A' && c < '[') mayuscula++;
            if (c >= 'a' && c < '{') minuscula++;

        }
        valida = valida && especial > 0 && numero > 0 && minuscula > 0 && mayuscula > 0;
        if (!valida) {
            Toast.makeText(RegistroAlumno.this, "La contraseña debe contener una mayúscula, una minúscula, un número y un carcacter especial", Toast.LENGTH_LONG).show();
        }
        return valida;
    }

}

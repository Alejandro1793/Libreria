package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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

    private Button btnAceptarDatosAlumno;
    private EditText txtEmailAlumno, txtIdAlumnoRegistro, txtContraseniaAlumno;
    private FirebaseAuth mAuth;
    private ImageView btnRegresar;
    private ArrayList<String> listadoColegios;
    private ArrayAdapter<String> adapterColegios, adapterAulas;
    private Colegio cole;
    private Usuario usuario;
    private String idCole;
    private Map<String, Colegio> colegios;
    private Spinner spIdAula;

    private ProgressDialog progressDialog;

    public void mostrarDialogo() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Creando usuario alumno/a");
        progressDialog.setMessage("creando tu usuario...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
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
        btnAceptarDatosAlumno.setEnabled(false);
        txtContraseniaAlumno = findViewById((R.id.txtContraseniaAlumno));
        spIdAula = findViewById((R.id.spIdAulaAlumno));
        final Spinner spIdColegios = findViewById(R.id.spIdColegioAlumno);

        //Obtenemos todos los colegios de la base de datos y cargamos el spinner con sus ID
        database.collection("Colegios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cole = document.toObject(Colegio.class);
                        colegios.put(cole.getIdColegio(), cole);
                        listadoColegios.add(cole.getIdColegio());
                    }
                    adapterColegios = new ArrayAdapter<>(RegistroAlumno.this, android.R.layout.simple_spinner_item, listadoColegios);
                    spIdColegios.setAdapter(adapterColegios);
                    if(!listadoColegios.isEmpty()){
                        btnAceptarDatosAlumno.setEnabled(true);
                        adapterAulas = new ArrayAdapter<>(RegistroAlumno.this, android.R.layout.simple_spinner_item, new ArrayList<>(colegios.get(spIdColegios.getSelectedItem().toString()).getAulas().keySet()));
                        spIdAula.setAdapter(adapterAulas);
                    }else{
                        Toast toast = Toast.makeText(RegistroAlumno.this, "No se permite el registro, espera hasta que el profesor active tu perfil",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER|0,0,0);
                        toast.show();
                    }

                }

            }
        });

        spIdColegios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterAulas = new ArrayAdapter<>(RegistroAlumno.this, android.R.layout.simple_spinner_item, new ArrayList<>(colegios.get(spIdColegios.getSelectedItem().toString()).getAulas().keySet()));
                spIdAula.setAdapter(adapterAulas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                if (txtEmailAlumno.getText().toString().isEmpty() || txtIdAlumnoRegistro.getText().toString().isEmpty()) {
                    Toast.makeText(RegistroAlumno.this, "Rellena todos los datos", Toast.LENGTH_LONG).show();
                } else {
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
                    if (cole.getAulas().containsKey(spIdAula.getSelectedItem().toString())) {
                        Aula aula = cole.getAulas().get(spIdAula.getSelectedItem().toString());
                        if (aula.getListadoAlumnos().contains(txtIdAlumnoRegistro.getText().toString())) {
                            Alumno alumno = cole.getAlumnado().get(txtIdAlumnoRegistro.getText().toString());
                            if (alumno.getEmail().equals(txtEmailAlumno.getText().toString())) {
                                progressDialog = new ProgressDialog(RegistroAlumno.this);
                                mostrarDialogo();
                                mAuth.createUserWithEmailAndPassword(txtEmailAlumno.getText().toString(), txtContraseniaAlumno.getText().toString())
                                        .addOnCompleteListener(RegistroAlumno.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    //Registro del usuario realizado con éxito
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    usuario = new Usuario(txtEmailAlumno.getText().toString(), txtContraseniaAlumno.getText().toString(), idCole, txtIdAlumnoRegistro.getText().toString(), "Alumno");
                                                    database.collection("users").document(txtEmailAlumno.getText().toString()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(RegistroAlumno.this, "Usuario creado con éxito.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    progressDialog.dismiss();
                                                    updateUI(user);
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegistroAlumno.this, "Error al registrar el usuario",
                                                            Toast.LENGTH_SHORT).show();
                                                    mAuth.getCurrentUser().delete();
                                                    startActivity(new Intent(RegistroAlumno.this, EleccionRegistro.class));
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(RegistroAlumno.this, "Tu profesor te ha registrado con otro correo", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegistroAlumno.this, "No estás registrado en este aula como alumno. Habla con tu profesor", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegistroAlumno.this, "Este aula no existe en este colegio", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public boolean comprobarEmail(String email) {

        // Patrón para verificar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        return mather.find();
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(RegistroAlumno.this, PrincipalAlumno.class);
            intent.putExtra("idcole", idCole);
            intent.putExtra("idalumno", txtIdAlumnoRegistro.getText().toString());
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

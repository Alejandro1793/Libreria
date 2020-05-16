package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

public class RegistroProfesor extends AppCompatActivity {

    private Button btnAceptarDatos;
    private EditText txtEmail, txtIdProfeRegistro, txtContrasenia;
    private FirebaseAuth mAuth;
    private ImageView btnRegresar;
    private ArrayList<String> listadoColegios;
    private ArrayAdapter<String> adapter;
    private Colegio cole;
    private Usuario usuario;
    private  String idCole;
    private Map<String, Colegio> colegios;

    private ProgressDialog progressDialog;


    public void mostrarDialogo() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Creando usuario profesor/a");
        progressDialog.setMessage("creando tu usuario...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_profesor);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        listadoColegios = new ArrayList<>();
        colegios = new HashMap<>();

        btnRegresar = findViewById(R.id.btnRegresar);
        txtEmail = findViewById(R.id.txtEmail);
        txtIdProfeRegistro = findViewById(R.id.txtIdProfeRegistro);
        btnAceptarDatos = findViewById(R.id.btnAceptarDatos);
        txtContrasenia = findViewById((R.id.txtContraseniaProfe));
        final Spinner spIdColegios = findViewById(R.id.spIdColegios);
        btnAceptarDatos.setEnabled(false);

        database.collection("Colegios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cole = document.toObject(Colegio.class);
                        colegios.put(cole.getIdColegio(), cole);
                        listadoColegios.add(cole.getIdColegio());
                    }
                    adapter = new ArrayAdapter<>(RegistroProfesor.this, android.R.layout.simple_spinner_item, listadoColegios);
                    spIdColegios.setAdapter(adapter);
                    if(!listadoColegios.isEmpty()){
                        btnAceptarDatos.setEnabled(true);
                    }else{
                        Toast toast = Toast.makeText(RegistroProfesor.this, "Espera hasta que el administrador active tu centro escolar",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER|0,0,0);
                        toast.show();
                    }
                }

            }
        });


        btnAceptarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmail.getText().toString())) {
                    txtEmail.setEnabled(false);
                } else {
                    Toast.makeText(RegistroProfesor.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                }
                if (comprobarContrasenia(txtContrasenia.getText().toString())) {
                    txtContrasenia.setEnabled(false);
                } else {
                    txtContrasenia.setText("");
                }
                idCole = spIdColegios.getSelectedItem().toString();
                cole = colegios.get(idCole);
                if (cole.getProfesorado().containsKey(txtIdProfeRegistro.getText().toString())){
                    progressDialog = new ProgressDialog(RegistroProfesor.this);
                    mostrarDialogo();
                    mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtContrasenia.getText().toString())
                            .addOnCompleteListener(RegistroProfesor.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registro del usuario realizado con éxito
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        usuario = new Usuario(txtEmail.getText().toString(), txtContrasenia.getText().toString(), idCole, txtIdProfeRegistro.getText().toString(),"Profesor");
                                        database.collection("users").document(txtEmail.getText().toString()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegistroProfesor.this, "Usuario creado con éxito.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        progressDialog.dismiss();
                                        updateUI(user);
                                    } else {
                                        progressDialog.dismiss();
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
            Intent intent = new Intent(RegistroProfesor.this, PrincipalProfesor.class);
            intent.putExtra("idcole", idCole);
            intent.putExtra("idaula", cole.getProfesorado().get(txtIdProfeRegistro.getText().toString()).getIdAula());
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
            Toast.makeText(RegistroProfesor.this, "La contraseña debe tener 8-16 carácteres", Toast.LENGTH_LONG).show();
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
            Toast.makeText(RegistroProfesor.this, "La contraseña debe contener una mayúscula, una minúscula, un número y un carcacter especial", Toast.LENGTH_LONG).show();
        }
        return valida;
    }
}

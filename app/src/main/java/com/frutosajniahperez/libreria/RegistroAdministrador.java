package com.frutosajniahperez.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroAdministrador extends AppCompatActivity {

    Button btnGenerar, btnAceptarDatos;
    TextView txtPassGenerada, txtEmail, txtRegistroCole;
    FirebaseAuth mAuth;
    ImageView btnRegresar;
    Boolean existe = false;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_administrador);

        btnGenerar = findViewById(R.id.btnGenerar);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtPassGenerada = findViewById(R.id.txtPassGenerada);
        txtEmail = findViewById(R.id.txtEmail);
        btnAceptarDatos = findViewById(R.id.btnAceptarDatos);
        txtRegistroCole = findViewById(R.id.txtRegistroCole);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();


        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmail.getText().toString())) {
                    txtPassGenerada.setText(GeneradorContrasena.getPassword());
                    txtEmail.setEnabled(false);
                    btnGenerar.setEnabled(false);
                    btnAceptarDatos.setEnabled(true);
                } else {
                    Toast.makeText(RegistroAdministrador.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                }
            }
        });

        btnAceptarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (comprobarID(txtRegistroCole.getText().toString())) {
                    database.collection("Colegios").document(txtRegistroCole.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(RegistroAdministrador.this, "Fallo en el registro. El colegio ya existe",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassGenerada.getText().toString())
                                            .addOnCompleteListener(RegistroAdministrador.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Registro del usuario realizado con éxito
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        usuario = new Usuario(txtEmail.getText().toString(), txtPassGenerada.getText().toString(), txtRegistroCole.getText().toString(), "Administrador");
                                                        database.collection("users").document(txtEmail.getText().toString()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(RegistroAdministrador.this, "Usuario creado con éxito.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        updateUI(user);
                                                    } else {
                                                        Toast.makeText(RegistroAdministrador.this, "Error al registrar el usuario",
                                                                Toast.LENGTH_SHORT).show();
                                                        mAuth.getCurrentUser().delete();
                                                        startActivity(new Intent(RegistroAdministrador.this, EleccionRegistro.class));
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistroAdministrador.this, "El ID tiene que tener 8 números", Toast.LENGTH_LONG).show();
                    txtRegistroCole.setText("");
                }

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroAdministrador.this, Principal.class));
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
            Intent intent = new Intent(RegistroAdministrador.this, RegistroColegio.class);
            intent.putExtra("idcole", txtRegistroCole.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    public boolean comprobarID(String codigo) {

        if (codigo.length() == 8) {
            return TextUtils.isDigitsOnly(codigo);
        } else {
            return false;
        }

    }
}

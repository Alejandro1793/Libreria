package com.frutosajniahperez.libreria;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class IniciarSesion extends AppCompatActivity {

    Usuario usuario;
    String email, password;
    FirebaseFirestore database;

    private ProgressDialog progressDialog;


    public void mostrarDialogo() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Iniciando sesión");
        progressDialog.setMessage("cargando tu sesión...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        database = FirebaseFirestore.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Button btnInicioSesion = findViewById(R.id.btnInicioSesion);
        ImageView btnRegresar = findViewById(R.id.btnRegresar);
        final EditText txtEmail = findViewById(R.id.txtCorreo);
        final EditText txtPassword = findViewById(R.id.txtContrasenia);


        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(IniciarSesion.this, "El usuario no puede estar vacío", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(IniciarSesion.this, "La contraseña no puede estar vacía", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog = new ProgressDialog(IniciarSesion.this);
                mostrarDialogo();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(IniciarSesion.this, "Sesión Iniciada",
                                    Toast.LENGTH_SHORT).show();
                            //Obtiene el usuario de la base de datos
                            database.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            usuario = document.toObject(Usuario.class);
                                            comprobacionUsuario(usuario);
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(IniciarSesion.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion.this, Principal.class));
                finish();
            }
        });
    }

    public void comprobacionUsuario(final Usuario usuario) {

        database.collection("Colegios").document(usuario.getIdColegio()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Intent intent = null;
                    if (document.exists()) {
                        //FALTAN LOS ROLES DE ALUMNO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        switch (usuario.getRol()) {
                            case "Alumno":
                                break;
                            case "Administrador":
                                intent = new Intent(IniciarSesion.this, ModificarColegio.class);
                                break;
                            case "Profesor":
                                intent = new Intent(IniciarSesion.this, PrincipalProfesor.class);
                                intent.putExtra("idprofe", usuario.getIdUsuario());
                                break;
                        }
                    } else {
                        Toast.makeText(IniciarSesion.this, "Aún no has registrado tu colegio", Toast.LENGTH_LONG).show();
                        intent = new Intent(IniciarSesion.this, RegistroColegio.class);
                    }
                    intent.putExtra("idcole", usuario.getIdColegio());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}

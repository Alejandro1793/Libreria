package com.frutosajniahperez.libreria.ui.alumnos;

import android.app.Dialog;
import android.content.ContentProvider;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.frutosajniahperez.libreria.R;

public class Dialogo_alumno {

    public interface ResultadoDialogoAlumno {
        void ResultadoDialogoAlumno(String idAlumno, String nombreAlumno, String email);
    }

    private ResultadoDialogoAlumno interfaz;

    public Dialogo_alumno(Context context, ResultadoDialogoAlumno actividad) {

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_alumno);
        dialog.setCanceledOnTouchOutside(true);

        final EditText etIdAlumno = dialog.findViewById(R.id.etIdAlumno);
        final EditText etNombreAlumno = dialog.findViewById(R.id.etNombreAlumno);
        final EditText etEmailAlumno = dialog.findViewById(R.id.etEmailAlumno);
        TextView btnAceptarAlumno = dialog.findViewById(R.id.btnAceptarAlumno);

        btnAceptarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etIdAlumno.getText().toString()) && !TextUtils.isEmpty(etNombreAlumno.getText().toString()) && !TextUtils.isEmpty(etEmailAlumno.getText().toString())){
                    interfaz.ResultadoDialogoAlumno(etIdAlumno.getText().toString(), etNombreAlumno.getText().toString(), etEmailAlumno.getText().toString());
                    dialog.dismiss();
                } else {
                    Toast.makeText(dialog.getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }
}

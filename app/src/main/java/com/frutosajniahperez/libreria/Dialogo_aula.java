package com.frutosajniahperez.libreria;

import android.app.Dialog;
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

public class Dialogo_aula {

    //Creamos la interfaz para poder implementar el código en la clases CrearColegio y ModificarColegio
    public interface ResultadoDialogoAula {
        void ResultadoDialogoAula(String idAula);
    }

    private ResultadoDialogoAula interfaz;

    public Dialogo_aula(Context context, ResultadoDialogoAula actividad){

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_aula);
        dialog.setCanceledOnTouchOutside(true);

        final EditText etIdAula = dialog.findViewById(R.id.etIdAula);
        etIdAula.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        TextView btnAceptarAula = dialog.findViewById(R.id.btnAceptarAula);

        btnAceptarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etIdAula.getText().toString())){
                    interfaz.ResultadoDialogoAula(etIdAula.getText().toString());
                    dialog.dismiss();
                } else {
                    Toast.makeText(dialog.getContext(), "El ID no puede estar vacío", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }
}

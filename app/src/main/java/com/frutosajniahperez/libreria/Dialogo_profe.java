package com.frutosajniahperez.libreria;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Dialogo_profe {

    //Creamos la interfaz para poder implementar el código en la clases CrearColegio y ModificarColegio
    public interface ResultadoDialogoProfe{
        void ResultadoDialogoProfe(String idProfe, String idAula);
    }

    private ResultadoDialogoProfe interfaz;

    public Dialogo_profe(Context context, ResultadoDialogoProfe actividad, ArrayAdapter<String> listado){

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_profe);
        dialog.setCanceledOnTouchOutside(true);

        final EditText etIdProfe = dialog.findViewById(R.id.etIdProfe);
        etIdProfe.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        TextView btnAceptarProfe = dialog.findViewById(R.id.btnAceptarProfe);
        final Spinner spAulas = dialog.findViewById(R.id.spAulas);

        spAulas.setAdapter(listado);


        btnAceptarProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarID(etIdProfe.getText().toString())){
                    interfaz.ResultadoDialogoProfe(etIdProfe.getText().toString(), spAulas.getSelectedItem().toString());
                    dialog.dismiss();
                } else {
                    Toast.makeText(dialog.getContext(), "El ID tiene que ser de longitud 9", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();

    }

    public boolean comprobarID(String idProfe){

        return (idProfe.length() == 9);
    }
}

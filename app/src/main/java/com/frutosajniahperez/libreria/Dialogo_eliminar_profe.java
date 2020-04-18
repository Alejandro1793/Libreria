package com.frutosajniahperez.libreria;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Dialogo_eliminar_profe {

    //Creamos la interfaz para poder implementar el código en la clase ModificarColegio
    public interface ResultadoDialogoEliminarProfe {
        void ResultadoDialogoEliminarProfe(String idProfe);
    }

    private ResultadoDialogoEliminarProfe interfaz;

    public Dialogo_eliminar_profe(Context context, ResultadoDialogoEliminarProfe actividad, ArrayAdapter<String> listado){

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_eliminar_profe);
        dialog.setCanceledOnTouchOutside(true);

        TextView btnEliminarProfe = dialog.findViewById(R.id.btnEliminarProfe);
        final Spinner spProfeBorrado = dialog.findViewById(R.id.spProfeBorrado);
        spProfeBorrado.setAdapter(listado);


        btnEliminarProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoDialogoEliminarProfe(spProfeBorrado.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

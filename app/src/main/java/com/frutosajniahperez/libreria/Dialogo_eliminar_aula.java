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

public class Dialogo_eliminar_aula {
    //Creamos la interfaz para poder implementar el código en la clase ModificarColegio
    public interface ResultadoDialogoEliminarAula {
        void ResultadoDialogoEliminarAula(String idAula);
    }

    private ResultadoDialogoEliminarAula interfaz;

    public Dialogo_eliminar_aula(Context context, ResultadoDialogoEliminarAula actividad, ArrayAdapter<String> listado){

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_eliminar_aula);
        dialog.setCanceledOnTouchOutside(true);

        TextView btnEliminarAula = dialog.findViewById(R.id.btnEliminarAula);
        final Spinner spAulaBorrado = dialog.findViewById(R.id.spAulaBorrado);
        spAulaBorrado.setAdapter(listado);

        btnEliminarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoDialogoEliminarAula(spAulaBorrado.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

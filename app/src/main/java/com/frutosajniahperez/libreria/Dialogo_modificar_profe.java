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

public class Dialogo_modificar_profe {
    //Creamos la interfaz para poder implementar el código en la clases CrearColegio y ModificarColegio
    public interface ResultadoDialogoModificarProfe{
        void ResultadoDialogoModificarProfe(String idProfe, String idAula);
    }

    private ResultadoDialogoModificarProfe interfaz;

    public Dialogo_modificar_profe(Context context, ResultadoDialogoModificarProfe actividad, ArrayAdapter<String> listadoProfe, ArrayAdapter<String> listadoAula){

        interfaz = actividad;

        //Creamos el dialogo con las características necesarias
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_modificar_profe);
        dialog.setCanceledOnTouchOutside(true);

        final Spinner spModProfe = dialog.findViewById(R.id.spModProfe);
        final Spinner spModAula = dialog.findViewById(R.id.spModAula);
        TextView btnAceptarCambios = dialog.findViewById(R.id.btnAceptarCambios);

        spModProfe.setAdapter(listadoProfe);
        spModAula.setAdapter(listadoAula);


        btnAceptarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoDialogoModificarProfe(spModProfe.getSelectedItem().toString(), spModAula.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}

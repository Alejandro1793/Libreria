package com.frutosajniahperez.libreria;

import java.io.Serializable;
import java.util.HashMap;


public class Colegio implements Serializable {

    private String idColegio;
    private HashMap<String, Profesor> profesorado;
    private HashMap<String, Aula> aulas;

    public Colegio() {}

    public Colegio(String idColegio, HashMap<String, Profesor> profesorado, HashMap<String, Aula> aulas) {
        this.idColegio = idColegio;
        this.profesorado = profesorado;
        this.aulas = aulas;
    }

    public java.lang.String getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(String idColegio) {
        this.idColegio = idColegio;
    }

    public HashMap<String, Profesor> getProfesorado() {
        return profesorado;
    }

    public void setProfesorado(HashMap<String, Profesor> profesorado) {
        this.profesorado = profesorado;
    }

    public HashMap<String, Aula> getAulas() {
        return aulas;
    }

    public void setAulas(HashMap<String, Aula> aulas) {
        this.aulas = aulas;
    }
}

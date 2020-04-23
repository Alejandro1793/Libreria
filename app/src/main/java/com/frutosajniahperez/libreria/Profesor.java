package com.frutosajniahperez.libreria;

import java.io.Serializable;

public class Profesor implements Serializable {

    private String idProfesor;
    private String idAula;

    public Profesor() {
    }

    public Profesor(String idProfesor, String contraseña, String idAula) {
        this.idProfesor = idProfesor;
        this.idAula = idAula;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }
}

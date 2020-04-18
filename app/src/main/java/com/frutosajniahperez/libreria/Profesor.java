package com.frutosajniahperez.libreria;

import java.io.Serializable;

public class Profesor implements Serializable {

    private String idProfesor;
    private Aula aula;

    public Profesor() {
    }

    public Profesor(String idProfesor, String contraseña, Aula aula) {
        this.idProfesor = idProfesor;
        this.aula = aula;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
}

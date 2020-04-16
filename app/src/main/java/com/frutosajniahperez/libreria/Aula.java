package com.frutosajniahperez.libreria;

import java.io.Serializable;
import java.util.HashMap;


public class Aula implements Serializable {

    private String idAula;
    private HashMap<String, Libro> libreria;
    private HashMap<String, Alumno> listadoAlumnos;

    public Aula() {}

    public Aula(String idAula, HashMap<String, Libro> libreria, HashMap<String, Alumno> listadoAlumnos) {
        this.idAula = idAula;
        this.libreria = libreria;
        this.listadoAlumnos = listadoAlumnos;
    }

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public HashMap<String, Libro> getLibreria() {
        return libreria;
    }

    public void setLibreria(HashMap<String, Libro> libreria) {
        this.libreria = libreria;
    }

    public HashMap<String, Alumno> getListadoAlumnos() {
        return listadoAlumnos;
    }

    public void setListadoAlumnos(HashMap<String, Alumno> listadoAlumnos) {
        this.listadoAlumnos = listadoAlumnos;
    }
}
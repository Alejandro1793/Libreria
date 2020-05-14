package com.frutosajniahperez.libreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Alumno implements Serializable {

    private String idAlumno;
    private String nombre;
    private String email;
    private String idAula;
    private ArrayList<String> librosLeidos;
    private HashMap<String, Integer> librosPuntuados;

    public Alumno(){
        librosPuntuados = new HashMap<>();
    }

    public Alumno(String idAlumno, String nombre, String email, ArrayList<String> librosLeidos, String idAula) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.email = email;
        this.librosLeidos = librosLeidos;
        this.idAula = idAula;
        this.librosPuntuados = new HashMap<>();
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getLibrosLeidos() {
        return librosLeidos;
    }

    public void setLibrosLeidos(ArrayList<String> librosLeidos) {
        this.librosLeidos = librosLeidos;
    }

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public HashMap<String, Integer> getLibrosPuntuados() {
        return librosPuntuados;
    }

    public void setLibrosPuntuados(HashMap<String, Integer> librosPuntuados) {
        this.librosPuntuados = librosPuntuados;
    }
}

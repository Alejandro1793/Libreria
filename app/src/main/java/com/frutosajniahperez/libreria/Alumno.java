package com.frutosajniahperez.libreria;

import java.util.ArrayList;

public class Alumno {

    private String idAlumno;
    private String nombre;
    private String email;
    private String idAula;
    private ArrayList<String> librosLeidos;

    public Alumno(){

    }

    public Alumno(String idAlumno, String nombre, String email, ArrayList<String> librosLeidos, String idAula) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.email = email;
        this.librosLeidos = librosLeidos;
        this.idAula = idAula;
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
}

package com.frutosajniahperez.libreria;

import java.util.ArrayList;

public class Alumno {

    private String idAlumno;
    private String nombre;
    private String email;
    private Aula aula;
    private ArrayList<Libro> librosLeidos;

    public Alumno(){

    }

    public Alumno(String idAlumno, String nombre, String email, ArrayList<Libro> librosLeidos, Aula aula) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.email = email;
        this.librosLeidos = librosLeidos;
        this.aula = aula;
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

    public ArrayList<Libro> getLibrosLeidos() {
        return librosLeidos;
    }

    public void setLibrosLeidos(ArrayList<Libro> librosLeidos) {
        this.librosLeidos = librosLeidos;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
}

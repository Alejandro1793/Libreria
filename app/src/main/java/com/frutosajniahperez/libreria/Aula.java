package com.frutosajniahperez.libreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Aula implements Serializable {

    private String idAula;
    private HashMap<String, Libro> libreria;
    private ArrayList<String> listadoAlumnos;
    private HashMap<String, Prestamo> listadoPrestamos;

    public Aula() {}

    public Aula(String idAula, HashMap<String, Libro> libreria, ArrayList<String> listadoAlumnos, HashMap<String, Prestamo> listadoPrestamos) {
        this.idAula = idAula;
        this.libreria = libreria;
        this.listadoAlumnos = listadoAlumnos;
        this.listadoPrestamos = listadoPrestamos;
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

    public ArrayList<String> getListadoAlumnos() {
        return listadoAlumnos;
    }

    public void setListadoAlumnos(ArrayList<String> listadoAlumnos) {
        this.listadoAlumnos = listadoAlumnos;
    }

    public HashMap<String, Prestamo> getListadoPrestamos() {
        return listadoPrestamos;
    }

    public void setListadoPrestamos(HashMap<String, Prestamo> listadoPrestamos) {
        this.listadoPrestamos = listadoPrestamos;
    }
}
package com.frutosajniahperez.libreria;

import java.util.HashMap;

public class Libro {

    private String isbn;
    private String titulo;
    private HashMap<String, String> autores;
    private String editorial;
    private String sinopsis;
    private String año;
    private String imagen;
    private boolean prestado;
    private int valoracionTotal;
    private double valoracionMedia;
    private int numValoraciones;

    public Libro() {
        this.prestado = false;
        this.valoracionTotal = 0;
        this.valoracionMedia = 0;
        this.numValoraciones = 0;
    }

    public Libro(String isbn, String titulo, HashMap<String, String> autores, String editorial, String sinopsis, String año, String imagen) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autores = autores;
        this.editorial = editorial;
        this.sinopsis = sinopsis;
        this.año = año;
        this.imagen = imagen;
        this.prestado = false;
        this.valoracionTotal = 0;
        this.valoracionMedia = 0;
        this.numValoraciones = 0;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public HashMap<String, String> getAutores() {
        return autores;
    }

    public void setAutores(HashMap<String, String> autores) {
        this.autores = autores;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    public double getValoracion() {
       return this.valoracionMedia;
    }

    public void setValoracion(int valoracion){
        this.valoracionTotal += valoracion;
        this.numValoraciones++;
        this.valoracionMedia = (double) this.valoracionTotal / this.numValoraciones;

    }
}

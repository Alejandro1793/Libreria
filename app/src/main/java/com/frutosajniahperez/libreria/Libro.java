package com.frutosajniahperez.libreria;

import java.util.ArrayList;

public class Libro {

    private String isbn;
    private String titulo;
    private ArrayList<String> autores;
    private String editorial;
    private String sinopsis;
    private String año;
    private boolean imagen;

    public Libro() {}

    public Libro(String isbn, String titulo, ArrayList<String> autores, String editorial, String sinopsis, String año) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autores = autores;
        this.editorial = editorial;
        this.sinopsis = sinopsis;
        this.año = año;
        this.imagen = false;
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

    public ArrayList<String> getAutores() {
        return autores;
    }

    public void setAutores(ArrayList<String> autor) {
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

    public boolean hasImagen() {
        return imagen;
    }

    public void setImagen(boolean imagen) {
        this.imagen = imagen;
    }
}

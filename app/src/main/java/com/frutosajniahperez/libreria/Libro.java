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
    private int valoracion;
    private int numValoraciones;

    public Libro() {
        this.valoracion = 0;
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
        this.valoracion = 0;
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

    public int getValoracion() {
        return this.valoracion / this.numValoraciones;
    }

    public void setValoracion(int valoracion){
        this.valoracion += valoracion;
        this.numValoraciones++;
    }
}

package com.frutosajniahperez.libreria;

import java.util.Date;

public class Prestamo {

    private String alumno;
    private String libro;
    private Date fechaPrestamo;
    private Date fechaEntrega;

    public Prestamo() {
    }

    public Prestamo(String alumno, String libro, Date fechaPrestamo, Date fechaEntrega) {
        this.alumno = alumno;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaEntrega = fechaEntrega;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}

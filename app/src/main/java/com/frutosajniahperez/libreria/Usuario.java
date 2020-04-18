package com.frutosajniahperez.libreria;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;
    private String contraseña;
    private String idColegio;
    private String rol;

    public Usuario() {
    }

    public Usuario(String email, String contraseña, String idColegio, String rol) {
        this.email = email;
        this.contraseña = contraseña;
        this.idColegio = idColegio;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(String idColegio) {
        this.idColegio = idColegio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

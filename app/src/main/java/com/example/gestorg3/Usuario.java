package com.example.gestorg3.modelos;

/**
 * Clase modelo Usuario
 * Representa un usuario en la base de datos
 */
public class Usuario {

    private int id;
    private String nombreCompleto;
    private String correo;
    private String telefono;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor sin ID (para insertar nuevos registros)
    public Usuario(String nombreCompleto, String correo, String telefono) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
    }

    // Constructor completo (con ID, para registros existentes)
    public Usuario(int id, String nombreCompleto, String correo, String telefono) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}

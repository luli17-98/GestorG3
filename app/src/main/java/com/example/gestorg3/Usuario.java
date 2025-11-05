package com.example.gestorg3;

public class Usuario {
    private int id;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String contrasena;

    public Usuario(int id, String nombreCompleto, String correo, String telefono, String contrasena) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    public Usuario(String nombreCompleto, String correo, String telefono, String contrasena) {
        this(-1, nombreCompleto, correo, telefono, contrasena);
    }

    // Getters
    public int getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getContrasena() { return contrasena; }
}

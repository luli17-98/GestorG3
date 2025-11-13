package com.example.gestorg3.modelos;

public class Usuario {
    private int id;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String contrasena;

    // Constructor vacío
    public Usuario() {}

    // Constructor de INSERCIÓN (4 Strings)
    public Usuario(String nombreCompleto, String correo, String telefono, String contrasena) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    // Constructor completo (con ID y 4 Strings)
    public Usuario(int id, String nombreCompleto, String correo, String telefono, String contrasena) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; } // ⬅️ CORRECCIÓN: Ahora solo asigna el valor

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}

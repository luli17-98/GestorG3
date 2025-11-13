package com.example.gestorg3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// IMPORTAR LAS CLASES DE TU ESTRUCTURA
import com.example.gestorg3.dao.UsuarioDAO;
import com.example.gestorg3.modelos.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etEmail, etTelefono, etPassword;
    private MaterialButton btnRegistrar;
    private UsuarioDAO usuarioDAO; // 👈 CORRECCIÓN 1: Usar UsuarioDAO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioDAO = new UsuarioDAO(this); // 👈 CORRECCIÓN 2: Inicializar UsuarioDAO

        etNombre = findViewById(R.id.editTextNombre);
        etEmail = findViewById(R.id.editTextEmail);
        etTelefono = findViewById(R.id.editTextTelefono);
        etPassword = findViewById(R.id.editTextPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registrarUsuario(); }
        });

        // Configurar navegación de "Ya tengo cuenta"
        findViewById(R.id.txtIniciarSesion).setOnClickListener(v -> finish());
    }

    private void registrarUsuario() {
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String telefono = etTelefono.getText() != null ? etTelefono.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // El teléfono puede ser opcional, pero nombre, email y password son necesarios
            Toast.makeText(this, "Nombre, Email y Contraseña son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Crear el objeto Usuario con todos los datos, incluyendo la contraseña
        Usuario nuevoUsuario = new Usuario(nombre, email, telefono, password);

        // 2. Usar el método DAO para insertar
        long id = usuarioDAO.insertarUsuario(nuevoUsuario); // 👈 CORRECCIÓN 3: Usamos el método DAO

        if (id > 0) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            // Volver al Login (o ir directamente a MainActivity si quieres, pero lo más común es volver a Login)
            finish();
        } else if (id == -1) {
            // Este es el valor devuelto por 'db.insert' si falla debido a restricción UNIQUE (email)
            Toast.makeText(this, "Error: El correo ya existe", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_LONG).show();
        }
    }
}
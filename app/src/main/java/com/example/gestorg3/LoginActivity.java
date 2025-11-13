package com.example.gestorg3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Importaciones para manejo de hilos (Executor)
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.gestorg3.dao.UsuarioDAO;
// Asegúrate de importar tu clase 'Usuario' si la usas directamente, aunque en este caso solo usamos la DAO
// import com.example.gestorg3.modelos.Usuario;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private UsuarioDAO usuarioDAO;

    // Executor para ejecutar la base de datos en un hilo de fondo
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        usuarioDAO = new UsuarioDAO(this);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentarLogin();
            }
        });

        // Link al registro
        findViewById(R.id.txtRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }

    private void intentarLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingresa email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ejecutar la operación de base de datos en el hilo de fondo (evita el ANR)
        executor.execute(() -> {
            SQLiteDatabase db = usuarioDAO.getReadableDatabase();
            Cursor cursor = null;
            boolean loginExitoso = false;
            int userId = -1;
            String userNombre = null;
            String mensajeToast = "";

            try {
                // La consulta ahora busca 'contrasena', que ya existe en el nuevo DAO
                cursor = db.rawQuery(
                        "SELECT id, nombre_completo, contrasena FROM usuarios WHERE correo = ?",
                        new String[]{email}
                );

                if (cursor.moveToFirst()) {
                    // Obtenemos el índice de las columnas usando el nombre
                    String contrasenaBD = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));

                    if (password.equals(contrasenaBD)) {
                        // Login correcto
                        loginExitoso = true;
                        userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        userNombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_completo"));
                    } else {
                        mensajeToast = "Contraseña incorrecta";
                    }
                } else {
                    mensajeToast = "Usuario no encontrado";
                }
            } catch (Exception e) {
                // Captura cualquier error de DB, incluyendo la excepción de columna
                mensajeToast = "Error en el acceso a la base de datos.";
                e.printStackTrace();
            } finally {
                if (cursor != null) cursor.close();
                db.close();
            }

            // Volver al Hilo Principal (UI) para mostrar el resultado o navegar
            if (loginExitoso) {
                final int finalUserId = userId;
                final String finalUserNombre = userNombre;
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userId", finalUserId);
                    intent.putExtra("userNombre", finalUserNombre);
                    startActivity(intent);
                    finish();
                });
            } else {
                final String finalMensajeToast = mensajeToast;
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, finalMensajeToast, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
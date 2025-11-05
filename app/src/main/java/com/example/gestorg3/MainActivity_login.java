package com.example.gestorg3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentarLogin();
            }
        });

        // Link al registro si querés
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

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, nombre_completo, correo_electronico, telefono, contrasena FROM " + DatabaseHelper.TABLE_USUARIOS +
                        " WHERE correo_electronico = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            String contrasenaBD = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));

            // si en el futuro guardás con hash, compará con hash
            if (password.equals(contrasenaBD)) {
                // Login correcto -> abrir MainActivity
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_completo"));

                cursor.close();
                db.close();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userId", id);
                intent.putExtra("userNombre", nombre);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
            }
        } else {
            if (cursor != null) cursor.close();
            db.close();
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
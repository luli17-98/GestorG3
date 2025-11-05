package com.example.gestorg3;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etEmail, etTelefono, etPassword;
    private MaterialButton btnRegistrar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new DatabaseHelper(this);

        etNombre = findViewById(R.id.editTextNombre);
        etEmail = findViewById(R.id.editTextEmail);
        etTelefono = findViewById(R.id.editTextTelefono);
        etPassword = findViewById(R.id.editTextPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registrarUsuario(); }
        });
    }

    private void registrarUsuario() {
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String telefono = etTelefono.getText() != null ? etTelefono.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí podrías hashear la contraseña si querés (recomendado en producción)
        String contrasenaGuardada = password; // por ahora sin hash

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre_completo", nombre);
        values.put("correo_electronico", email);
        values.put("telefono", telefono);
        values.put("contrasena", contrasenaGuardada);

        long id = -1;
        try {
            id = db.insertOrThrow(DatabaseHelper.TABLE_USUARIOS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        if (id != -1) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            finish(); // volver a la pantalla anterior (login)
        } else {
            Toast.makeText(this, "Error: posiblemente el correo ya existe", Toast.LENGTH_LONG).show();
        }
    }
}
package com.example.gestorg3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private TextView txtSinUsuarios;
    private DatabaseHelper dbHelper;
    private ArrayList<Usuario> listaUsuarios;
    private UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate - Iniciando MainActivity");

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewUsuarios);
        txtSinUsuarios = findViewById(R.id.txtSinUsuarios);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaUsuarios = new ArrayList<>();
        adapter = new UsuarioAdapter(listaUsuarios);
        recyclerView.setAdapter(adapter);

        cargarUsuariosDesdeBD();
    }

    private void cargarUsuariosDesdeBD() {
        listaUsuarios.clear();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT id, nombre_completo, correo_electronico, telefono, contrasena FROM " +
                    DatabaseHelper.TABLE_USUARIOS, null);

            Log.d(TAG, "Cursor count: " + cursor.getCount());

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_completo"));
                    String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo_electronico"));
                    String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
                    String contrasena = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));

                    listaUsuarios.add(new Usuario(id, nombre, correo, telefono, contrasena));
                    Log.d(TAG, "Usuario cargado: " + nombre);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar usuarios: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        adapter.notifyDataSetChanged();

        Log.d(TAG, "Total usuarios cargados: " + listaUsuarios.size());

        if (listaUsuarios.isEmpty()) {
            txtSinUsuarios.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Log.d(TAG, "No hay usuarios - mostrando mensaje");
        } else {
            txtSinUsuarios.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Log.d(TAG, "Mostrando " + listaUsuarios.size() + " usuarios");
        }
    }
}
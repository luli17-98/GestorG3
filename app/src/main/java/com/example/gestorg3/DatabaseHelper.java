package com.example.gestorg3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 🔹 Nombre y versión de la base de datos
    public static final String DATABASE_NAME = "usuarios.db";
    public static final int DATABASE_VERSION = 1;

    // 🔹 Nombre de la tabla
    public static final String TABLE_USUARIOS = "usuarios";

    // 🔹 Sentencia SQL para crear la tabla
    private static final String CREATE_TABLE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_completo TEXT NOT NULL, " +
                    "correo_electronico TEXT NOT NULL UNIQUE, " +
                    "telefono TEXT NOT NULL, " +
                    "contrasena TEXT NOT NULL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // 🔹 Crear la tabla de usuarios
        db.execSQL(CREATE_TABLE_USUARIOS);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // 🔹 Si hay una actualización de versión, eliminamos y recreamos la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}

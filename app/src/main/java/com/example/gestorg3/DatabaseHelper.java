package com.example.gestorg3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "usuarios.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_USUARIOS = "usuarios";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // Creación de la tabla usuarios
        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre_completo TEXT NOT NULL, " +
                "correo_electronico TEXT NOT NULL UNIQUE, " +
                "telefono TEXT NOT NULL, " +
                "contrasena TEXT NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejo simple de upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}

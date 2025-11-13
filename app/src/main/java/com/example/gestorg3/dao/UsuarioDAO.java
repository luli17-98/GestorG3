package com.example.gestorg3.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestorg3.modelos.Usuario;

import java.util.ArrayList;

/**
 * Clase DAO para gestionar usuarios en SQLite
 */
public class UsuarioDAO extends SQLiteOpenHelper {

    // Configuración de la base de datos
    private static final String DATABASE_NAME = "gestorg3.db";
    // ¡IMPORTANTE! Se incrementa la versión de la DB para que se ejecute onUpgrade y se cree la columna 'contrasena'
    private static final int DATABASE_VERSION = 2;

    // Tabla usuarios
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre_completo";
    private static final String COLUMN_EMAIL = "correo";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_CONTRASENA = "contrasena"; // <--- NUEVA COLUMNA AGREGADA

    // SQL para crear tabla
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_CONTRASENA + " TEXT NOT NULL, " + // <--- AGREGADA AQUÍ
                    COLUMN_TELEFONO + " TEXT" +
                    ")";

    // Constructor
    public UsuarioDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Al aumentar DATABASE_VERSION, esto se ejecuta:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    // ==================== MÉTODOS CRUD ====================

    /**
     * Insertar nuevo usuario. Ahora incluye la contraseña.
     */
    public long insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE, usuario.getNombreCompleto());
        values.put(COLUMN_EMAIL, usuario.getCorreo());
        values.put(COLUMN_CONTRASENA, usuario.getContrasena()); // <--- AGREGADA AQUÍ
        values.put(COLUMN_TELEFONO, usuario.getTelefono());

        long id = db.insert(TABLE_USUARIOS, null, values);
        db.close();

        return id;
    }

    /**
     * Obtener todos los usuarios
     */
    public ArrayList<Usuario> obtenerTodosUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " ORDER BY " + COLUMN_NOMBRE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                usuario.setNombreCompleto(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                usuario.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
                usuario.setContrasena(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))); // <--- AGREGADA AQUÍ

                lista.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lista;
    }

    /**
     * Obtener usuario por ID
     */
    public Usuario obtenerUsuarioPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USUARIOS,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Usuario usuario = null;
        if (cursor != null && cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            usuario.setNombreCompleto(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            usuario.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
            usuario.setContrasena(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))); // <--- AGREGADA AQUÍ
            cursor.close();
        }

        db.close();
        return usuario;
    }

    /**
     * Actualizar usuario. Ahora incluye la contraseña.
     */
    public int actualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE, usuario.getNombreCompleto());
        values.put(COLUMN_EMAIL, usuario.getCorreo());
        values.put(COLUMN_CONTRASENA, usuario.getContrasena()); // <--- AGREGADA AQUÍ
        values.put(COLUMN_TELEFONO, usuario.getTelefono());

        int rowsAffected = db.update(TABLE_USUARIOS,
                values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(usuario.getId())});

        db.close();
        return rowsAffected;
    }

    /**
     * Eliminar usuario
     */
    public int eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(TABLE_USUARIOS,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
        return rowsDeleted;
    }

    /**
     * Buscar usuarios por nombre
     */
    public ArrayList<Usuario> buscarUsuariosPorNombre(String nombre) {
        ArrayList<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USUARIOS +
                " WHERE " + COLUMN_NOMBRE + " LIKE ?";

        Cursor cursor = db.rawQuery(query, new String[]{"%" + nombre + "%"});

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                usuario.setNombreCompleto(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                usuario.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
                usuario.setContrasena(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))); // <--- AGREGADA AQUÍ

                lista.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lista;
    }

    /**
     * Contar total de usuarios
     */
    public int contarUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USUARIOS, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }
}


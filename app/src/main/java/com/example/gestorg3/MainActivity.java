package com.example.gestorg3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

// Asegúrate que estos imports coincidan con tus carpetas reales
import com.example.gestorg3.adaptadores.UsuarioAdapter;
import com.example.gestorg3.dao.UsuarioDAO;
import com.example.gestorg3.modelos.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtSinUsuarios;
    private UsuarioDAO usuarioDAO;
    private UsuarioAdapter adapter;
    private FloatingActionButton fabCerrarSesion, fabAgregarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Enlazamos vistas
        recyclerView = findViewById(R.id.recyclerViewUsuarios);
        txtSinUsuarios = findViewById(R.id.txtSinUsuarios);
        fabCerrarSesion = findViewById(R.id.fabCerrarSesion);
        fabAgregarUsuario = findViewById(R.id.fabAgregarUsuario);

        // 🔹 Inicializamos DAO
        usuarioDAO = new UsuarioDAO(this);

        // 🔹 Obtenemos los usuarios de la base de datos
        // CORRECCIÓN: El método en tu DAO se llama "obtenerTodosUsuarios"
        ArrayList<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();

        // 🔹 Configuramos el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuarioAdapter(this, usuarios, usuarioDAO);
        recyclerView.setAdapter(adapter);

        // 🔹 Mostrar u ocultar el mensaje "sin usuarios"
        actualizarVisibilidadLista(usuarios);

        // 🔹 Acción del botón flotante (cerrar sesión)
        fabCerrarSesion.setOnClickListener(v -> finish());

        // 🔹 Acción del botón flotante (agregar usuario)
        // Asegúrate de tener creada la clase "RegistroActivity"
        fabAgregarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    // ✅ Este método se ejecuta cada vez que la pantalla vuelve a mostrarse (al volver del registro)
    @Override
    protected void onResume() {
        super.onResume();
        actualizarLista();
    }

    // 🔹 Método para refrescar la lista luego de editar/eliminar/agregar
    public void actualizarLista() {
        // CORRECCIÓN: Aquí también debemos usar el nombre correcto del método
        ArrayList<Usuario> usuariosActualizados = usuarioDAO.obtenerTodosUsuarios();

        if (adapter != null) {
            adapter.actualizarDatos(usuariosActualizados);
            actualizarVisibilidadLista(usuariosActualizados);
        }
    }

    private void actualizarVisibilidadLista(ArrayList<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            txtSinUsuarios.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtSinUsuarios.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
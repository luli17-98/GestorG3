package com.example.gestorg3.adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestorg3.R;
import com.example.gestorg3.modelos.Usuario;
import com.example.gestorg3.dao.UsuarioDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Usuario> lista;
    private UsuarioDAO usuarioDAO;

    // Constructor
    public UsuarioAdapter(Context context, ArrayList<Usuario> lista, UsuarioDAO usuarioDAO) {
        this.context = context;
        this.lista = lista;
        this.usuarioDAO = usuarioDAO;
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtEmail, txtTelefono;
        Button btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    @NonNull
    @Override
    public UsuarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.ViewHolder holder, int position) {
        // Obtenemos el objeto usando la posición que nos da el método para mostrar datos
        Usuario u = lista.get(position);

        holder.txtNombre.setText(u.getNombreCompleto());
        holder.txtEmail.setText(u.getCorreo());
        holder.txtTelefono.setText(u.getTelefono());

        // 🔹 Botón Editar - AHORA CON FUNCIONALIDAD COMPLETA
        holder.btnEditar.setOnClickListener(v -> {
            // Para el clic, verificamos la posición actual (más seguro)
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                Usuario usuarioActual = lista.get(currentPos);
                // Abrimos el diálogo de edición
                mostrarDialogoEditar(usuarioActual, currentPos);
            }
        });

        // 🔹 Botón Eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            // IMPORTANTE: Usamos getAdapterPosition() para saber la posición real al momento del clic
            int currentPosition = holder.getAdapterPosition();

            // Validación para evitar cierres inesperados (crashes)
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Usuario usuarioAEliminar = lista.get(currentPosition);

            // 1. Eliminar de la base de datos
            int filasEliminadas = usuarioDAO.eliminarUsuario(usuarioAEliminar.getId());

            if (filasEliminadas > 0) {
                Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                // 2. Eliminar de la lista visual
                lista.remove(currentPosition);
                // 3. Notificar al adaptador para que actualice la vista
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, lista.size());
            } else {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Método para actualizar la lista desde MainActivity
    public void actualizarDatos(ArrayList<Usuario> nuevaLista) {
        this.lista.clear();
        this.lista.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    // 🔹 MÉTODO PARA MOSTRAR EL DIÁLOGO DE EDICIÓN
    private void mostrarDialogoEditar(Usuario usuario, int position) {
        // Crear el diálogo
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_editar_usuario);

        // Configurar el tamaño del diálogo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Referencias a los campos del diálogo
        TextInputEditText etNombre = dialog.findViewById(R.id.editTextNombreDialog);
        TextInputEditText etEmail = dialog.findViewById(R.id.editTextEmailDialog);
        TextInputEditText etTelefono = dialog.findViewById(R.id.editTextTelefonoDialog);
        TextInputEditText etPassword = dialog.findViewById(R.id.editTextPasswordDialog);
        MaterialButton btnGuardar = dialog.findViewById(R.id.btnGuardar);
        MaterialButton btnCancelar = dialog.findViewById(R.id.btnCancelar);

        // Rellenar los campos con los datos actuales
        etNombre.setText(usuario.getNombreCompleto());
        etEmail.setText(usuario.getCorreo());
        etTelefono.setText(usuario.getTelefono());
        // La contraseña se deja vacía por seguridad

        // Botón Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        // Botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String telefono = etTelefono.getText() != null ? etTelefono.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            // Validación básica
            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email)) {
                Toast.makeText(context, "Nombre y Email son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear objeto usuario actualizado
            // Si no se ingresó contraseña, mantener la anterior
            Usuario usuarioActualizado = new Usuario(
                    usuario.getId(),
                    nombre,
                    email,
                    telefono,
                    TextUtils.isEmpty(password) ? usuario.getContrasena() : password
            );

            // Actualizar en la base de datos
            int filasActualizadas = usuarioDAO.actualizarUsuario(usuarioActualizado);

            if (filasActualizadas > 0) {
                Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();

                // Actualizar la lista visual
                lista.set(position, usuarioActualizado);
                notifyItemChanged(position);

                dialog.dismiss();
            } else {
                Toast.makeText(context, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}

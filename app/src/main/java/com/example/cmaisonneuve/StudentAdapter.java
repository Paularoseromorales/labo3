package com.example.cmaisonneuve;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<String> studentsList;
    private DatabaseHelper dbHelper; // Añadir DatabaseHelper

    public StudentAdapter(List<String> studentsList, DatabaseHelper dbHelper) {
        this.studentsList = studentsList;
        this.dbHelper = dbHelper; // Inicializar DatabaseHelper
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        String studentName = studentsList.get(position);
        holder.studentNameTextView.setText(studentName);

        // Configura el botón para eliminar el estudiante
        holder.btnRemoveStudent.setOnClickListener(v -> {
            // Eliminar el estudiante de la base de datos
            boolean isDeleted = dbHelper.deleteStudentByName(studentName);
            if (isDeleted) {
                // Eliminar el estudiante de la lista y notificar al adaptador

                studentsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, studentsList.size());
                Toast.makeText(v.getContext(), "Estudiante eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Error al eliminar el estudiante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnRemoveStudent;
        TextView studentNameTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.text_view_student_name);
            btnRemoveStudent = itemView.findViewById(R.id.remove_student); // Cambia el ID si es necesario
        }
    }
}

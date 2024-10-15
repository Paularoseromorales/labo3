package com.example.cmaisonneuve;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    private DatabaseHelper dbHelper;
    private int courseId;

    public StudentAdapter(List<String> studentsList, DatabaseHelper dbHelper, int courseId) {
        this.studentsList = studentsList;
        this.dbHelper = dbHelper;
        this.courseId = courseId;
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
        SharedPreferences preferences = holder.itemView.getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int currentUserId = preferences.getInt("currentUserId", -1);
        if (currentUserId == 1) {
            holder.btnRemoveStudent.setVisibility(View.VISIBLE);
        }


        holder.btnRemoveStudent.setOnClickListener(v -> {

            int userId = getUserIdByName(studentName);

            if (userId != -1) {
                boolean isDeleted = dbHelper.deleteStudentFromCourse(userId, courseId);
                if (isDeleted) {

                    studentsList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, studentsList.size());
                    Toast.makeText(v.getContext(), "Estudiante eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Error al eliminar el estudiante", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(v.getContext(), "No se encontró el ID del estudiante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    // Méthode pour obtenir l'ID utilisateur en fonction du nom de l'étudiant
    private int getUserIdByName(String studentName) {

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT " + DatabaseHelper.COLUMN_USER_ID + " FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.COLUMN_USER_FULL_NAME + " = ?", new String[]{studentName});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));
            cursor.close();
            return userId;
        }
        if (cursor != null) cursor.close();
        return -1;
    }
    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnRemoveStudent;
        TextView studentNameTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.text_view_student_name);
            btnRemoveStudent = itemView.findViewById(R.id.remove_student);  // Cambia el ID si es necesario
        }
    }
}

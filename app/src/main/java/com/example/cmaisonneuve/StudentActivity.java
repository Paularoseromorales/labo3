package com.example.cmaisonneuve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmaisonneuve.db.DatabaseHelper;

public class StudentActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView studentNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // Verificar que el courseId se pase correctamente
        Intent intent = getIntent();
        int currentUserId = intent.getIntExtra("current_user_id", -1);  // Recibe la ID del usuario
        int courseId = intent.getIntExtra("course_id", -1);
        Toast.makeText(this, "ID del usuario: " + currentUserId + ", ID del curso: " + courseId, Toast.LENGTH_LONG).show();


        // Inicializar la base de datos y los TextViews
        databaseHelper = new DatabaseHelper(this);
        studentNameTextView = findViewById(R.id.student_name);

        // Cargar los estudiantes inscritos en el curso desde la base de datos
        if (courseId != -1) {
            loadStudentsForCourse(courseId);
        } else {
            Log.e("StudentActivity", "Error: courseId no válido");
            studentNameTextView.setText("No se pudo cargar estudiantes.");
        }
    }

    private void loadStudentsForCourse(int courseId) {
        Cursor cursor = databaseHelper.getUsersForCourse(courseId);  // Obtener estudiantes inscritos

        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder studentsList = new StringBuilder();
            do {
                // Obtener el nombre del estudiante
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULL_NAME));
                studentsList.append(studentName).append("\n"); // Añadir el nombre a la lista
            } while (cursor.moveToNext());

            // Mostrar la lista de nombres en el TextView
            studentNameTextView.setText(studentsList.toString());
            cursor.close();
        } else {
            studentNameTextView.setText("No se encontraron estudiantes");
            Log.d("StudentActivity", "No se encontraron estudiantes para el curso con ID: " + courseId);
        }
    }
}

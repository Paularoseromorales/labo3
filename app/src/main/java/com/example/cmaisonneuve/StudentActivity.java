package com.example.cmaisonneuve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmaisonneuve.R;
import com.example.cmaisonneuve.StudentAdapter;
import com.example.cmaisonneuve.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewStudents;
    private StudentAdapter studentAdapter;
    private List<String> studentsList = new ArrayList<>();  // Podrías cambiar esta lista para almacenar objetos Student con más información

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        int currentUserId = intent.getIntExtra("current_user_id", -1);  // Recibe la ID del usuario
        int courseId = intent.getIntExtra("course_id", -1);

        if (currentUserId == -1 || courseId == -1) {
            Toast.makeText(this, "Datos inválidos", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "ID del usuario: " + currentUserId + ", ID del curso: " + courseId, Toast.LENGTH_LONG).show();

        // Inicializar la base de datos
        databaseHelper = new DatabaseHelper(this);

        // Cargar los estudiantes inscritos en el curso desde la base de datos
        loadStudentsForCourse(courseId);

        // Inicializar el RecyclerView
        recyclerViewStudents = findViewById(R.id.recycler_view_students);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el StudentAdapter y configurarlo con el RecyclerView
        studentAdapter = new StudentAdapter(studentsList, databaseHelper, courseId);
        recyclerViewStudents.setAdapter(studentAdapter);
    }

    private void loadStudentsForCourse(int courseId) {
        Cursor cursor = databaseHelper.getUsersForCourse(courseId);  // Obtener estudiantes inscritos

        if (cursor != null && cursor.moveToFirst()) {
            studentsList.clear();  // Limpiar la lista antes de agregar nuevos datos
            do {
                // Obtener el nombre del estudiante (posiblemente necesitas más datos, como el userId)
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULL_NAME));
                studentsList.add(studentName);  // Añadir el nombre a la lista
            } while (cursor.moveToNext());

            // Notificar al adaptador que los datos han cambiado, solo si el adaptador ya fue inicializado
            if (studentAdapter != null) {
                studentAdapter.notifyDataSetChanged();
            }

            cursor.close();
        } else {
            Log.d("StudentActivity", "Aucun étudiant trouvé pour le cours avec ID : " + courseId);
            Toast.makeText(this, "Aucun étudiant trouvé", Toast.LENGTH_SHORT).show();
        }
    }
}

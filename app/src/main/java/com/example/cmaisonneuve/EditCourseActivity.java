package com.example.cmaisonneuve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cmaisonneuve.db.DatabaseHelper;

public class EditCourseActivity extends AppCompatActivity {

    private EditText courseNameText;
    private EditText sigleCourseText;
    private EditText teacherName;
    private EditText sessionText;
    private Button updateCourseButton;
    private DatabaseHelper db;
    private int idCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        courseNameText = findViewById(R.id.nomcourstext);
        sigleCourseText = findViewById(R.id.siglecourstext);
        teacherName = findViewById(R.id.enseignanttext);
        sessionText = findViewById(R.id.sessioncours);
        updateCourseButton = findViewById(R.id.updateCourse);
        db = new DatabaseHelper(this);
        //Recevoir les donnees a partir de l intent
        Intent intent = getIntent();
        idCourse = intent.getIntExtra("course_id", -1);
        String courseName = intent.getStringExtra("course_name");
        String courseSigle = intent.getStringExtra("course_sigle");
        String courseTeacher = intent.getStringExtra("course_teacher");
        String courseSession = intent.getStringExtra("course_session");

        // Afficher les donnees dans les champs de texte
        courseNameText.setText(courseName);
        sigleCourseText.setText(courseSigle);
        teacherName.setText(courseTeacher);
        sessionText.setText(courseSession);

        updateCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedCourseName = courseNameText.getText().toString();
                String updatedCourseSigle =sigleCourseText.getText().toString();
                String updatedCourseTeacher = teacherName.getText().toString();
                String updatedCourseSession = sessionText.getText().toString();
                if(updatedCourseName.isEmpty() || updatedCourseSigle.isEmpty() || updatedCourseTeacher.isEmpty() || updatedCourseSession.isEmpty() ){
                    Toast.makeText(EditCourseActivity.this, "Remplisser tous les champs",Toast.LENGTH_LONG).show();
                } else {
                    boolean isUpdated = db.updateCourse(new CourseItem(idCourse, updatedCourseName, updatedCourseSigle, updatedCourseTeacher, updatedCourseSession));
                    if(isUpdated){
                        Toast.makeText(EditCourseActivity.this, "Mise a jour success",Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        Intent intent = new Intent(EditCourseActivity.this, MainActivity.class);
                        startActivity(intent);
                        //finish();
                    } else {
                        Toast.makeText(EditCourseActivity.this, "Erreur lors de la mise a jour",Toast.LENGTH_LONG).show();

                    }

                }
            }

        });


    }
}
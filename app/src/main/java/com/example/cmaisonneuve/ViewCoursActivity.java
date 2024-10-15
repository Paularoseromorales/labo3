package com.example.cmaisonneuve;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ViewCoursActivity extends AppCompatActivity {

    private TextView courseNameText;
    private TextView sigleCourseText;
    private TextView teacherName;
    private TextView sessionText;
    private ImageView imageCours;
    private Button fichier;
    private Button etudiantsList;
    private Button quizButton;
    private ImageButton btntakequiz;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cours);


        courseNameText = findViewById(R.id.nomcourstext);
        sigleCourseText = findViewById(R.id.siglecourstext);
        teacherName = findViewById(R.id.enseignanttext);
        sessionText = findViewById(R.id.sessioncours);
        imageCours = findViewById(R.id.imageCours);
        fichier = findViewById(R.id.fileName);
        quizButton = findViewById(R.id.quizButton);
        btntakequiz = findViewById(R.id.btntakequiz);
        etudiantsList = findViewById(R.id.list_students);


        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        int courseId = intent.getIntExtra("course_id", -1);
        String courseName = intent.getStringExtra("course_name");
        String courseSigle = intent.getStringExtra("course_sigle");
        String courseTeacher = intent.getStringExtra("course_teacher");
        String courseSession = intent.getStringExtra("course_session");
        int currentUserId = intent.getIntExtra("current_user_id", -1);  // Recibe la ID del usuario


        courseNameText.setText(courseName);
        sigleCourseText.setText(courseSigle);
        teacherName.setText(courseTeacher);
        sessionText.setText(courseSession);

        loadCourseImage(courseId);

        fichier.setOnClickListener(view -> loadCourseFile(courseId)); // Carga y descarga el archivo


        if (currentUserId == 1) {
            quizButton.setVisibility(View.VISIBLE);
            etudiantsList.setVisibility(View.VISIBLE);
        }

        quizButton.setOnClickListener(v -> {
            Intent quizIntent = new Intent(ViewCoursActivity.this, CreateQuizActivity.class);
            quizIntent.putExtra("course_id", courseId);
            startActivity(quizIntent);
        });

        btntakequiz.setOnClickListener(v -> {
            Intent takeQuizIntent = new Intent(ViewCoursActivity.this, TakeQuizActivity.class);
            takeQuizIntent.putExtra("course_id", courseId);
            startActivity(takeQuizIntent);  // Iniciar la actividad aquí
        });


        etudiantsList.setOnClickListener(v -> {
            Intent studentIntent = new Intent(ViewCoursActivity.this, StudentActivity.class);
            studentIntent.putExtra("current_user_id", currentUserId);
            studentIntent.putExtra("course_id", courseId);
            startActivity(studentIntent);
        });
    }

    private void loadCourseImage(int courseId) {
        byte[] imageBytes = dbHelper.getCourseImage(courseId);
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageCours.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Image non disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCourseFile(int courseId) {
        byte[] fileData = dbHelper.getCourseFile(courseId);
        if (fileData != null) {
            Log.d("File Size", "Taille du fichier: " + fileData.length + " bytes");
            downloadFile(fileData, "document.pdf");
        } else {
            Toast.makeText(this, "Fichier non disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(byte[] fileData, String fileName) {
        try {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                if (uri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        outputStream.write(fileData);
                        outputStream.close();
                        Toast.makeText(this, "Téléchargement réussi: " + fileName, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Erreur lors de la création du fichier.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                File file = new File(downloadDir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(fileData);
                fos.close();
                Toast.makeText(this, "Téléchargement réussi: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("File Download", "Error al descargar el archivo: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du téléchargement du fichier.", Toast.LENGTH_SHORT).show();
        }
    }
}

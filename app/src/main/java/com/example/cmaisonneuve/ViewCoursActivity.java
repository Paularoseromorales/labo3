package com.example.cmaisonneuve;

import android.content.ContentValues;
import android.content.Intent;
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

        // Inicializa los TextViews, ImageView y Button
        courseNameText = findViewById(R.id.nomcourstext);
        sigleCourseText = findViewById(R.id.siglecourstext);
        teacherName = findViewById(R.id.enseignanttext);
        sessionText = findViewById(R.id.sessioncours);
        imageCours = findViewById(R.id.imageCours);
        fichier = findViewById(R.id.fileName);
        quizButton = findViewById(R.id.quizButton);
        btntakequiz = findViewById(R.id.btntakequiz);
        etudiantsList = findViewById(R.id.list_students);


        // Inicializa el helper de la base de datos
        dbHelper = new DatabaseHelper(this);

        // Recibe los datos del intent
        Intent intent = getIntent();
        int courseId = intent.getIntExtra("course_id", -1);
        String courseName = intent.getStringExtra("course_name");
        String courseSigle = intent.getStringExtra("course_sigle");
        String courseTeacher = intent.getStringExtra("course_teacher");
        String courseSession = intent.getStringExtra("course_session");
        int currentUserId = intent.getIntExtra("current_user_id", -1);  // Recibe la ID del usuario

        // Muestra los datos en los TextViews
        courseNameText.setText(courseName);
        sigleCourseText.setText(courseSigle);
        teacherName.setText(courseTeacher);
        sessionText.setText(courseSession);

        // Carga la imagen del curso desde la base de datos
        loadCourseImage(courseId);

        // Configura la descarga del archivo
        fichier.setOnClickListener(view -> {
            loadCourseFile(courseId); // Carga y descarga el archivo
        });


        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCoursActivity.this, CreateQuizActivity.class);
                intent.putExtra("course_id", courseId);
                startActivity(intent);
            }
        });


        btntakequiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCoursActivity.this, TakeQuizActivity.class);
                intent.putExtra("course_id", courseId); // Pasa el ID del curso para cargar el quiz
                startActivity(intent); // Lanza la actividad para tomar el quiz
            }
        });

        etudiantsList.setOnClickListener(v -> {
            Intent studentIntent = new Intent(ViewCoursActivity.this, StudentActivity.class);
            studentIntent.putExtra("current_user_id", currentUserId);  // Pasar la ID del usuario actual
            studentIntent.putExtra("course_id", courseId);  // Pasar la ID del curso actual
            startActivity(studentIntent);
        });

    }



    private void loadCourseImage(int courseId) {
        byte[] imageBytes = dbHelper.getCourseImage(courseId); // Recupera la imagen desde la base de datos
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageCours.setImageBitmap(bitmap); // Muestra la imagen en el ImageView
        } else {
            Toast.makeText(this, "Image non disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCourseFile(int courseId) {
        byte[] fileData = dbHelper.getCourseFile(courseId); // Recupera el archivo desde la base de datos
        if (fileData != null) {
            Log.d("File Size", "Taille du fichier: " + fileData.length + " bytes");
            downloadFile(fileData, "document.pdf"); // Descarga el archivo como "document.pdf"
        } else {
            Toast.makeText(this, "Fichier non disponible", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recibir el quiz creado por el usuario
            String question1 = data.getStringExtra("question1");
            String option1_1 = data.getStringExtra("option1_1");
            String option1_2 = data.getStringExtra("option1_2");
            String option1_3 = data.getStringExtra("option1_3");
            int correctAnswer1 = data.getIntExtra("correctAnswer1", -1);

            // Similar para las otras preguntas

            // Mostrar los datos o guardarlos en la base de datos
            Toast.makeText(this, "Quiz creado con éxito", Toast.LENGTH_SHORT).show();
        }
    }


    private void downloadFile(byte[] fileData, String fileName) {
        try {
            // Verifica la versión de Android
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // Para Android 10 (API 29) y superior, usar MediaStore para guardar en Downloads
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
                // Para Android 9 (API 28) y versiones anteriores, usar almacenamiento tradicional
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
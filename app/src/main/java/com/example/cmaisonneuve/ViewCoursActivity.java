package com.example.cmaisonneuve;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;

public class ViewCoursActivity extends AppCompatActivity {

    private TextView courseNameText;
    private TextView sigleCourseText;
    private TextView teacherName;
    private TextView sessionText;
    private ImageView imageCours;
    private Button fichier;
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



        // Inicializa el helper de la base de datos
        dbHelper = new DatabaseHelper(this);

        // Recibe los datos del intent
        Intent intent = getIntent();
        int courseId = intent.getIntExtra("course_id", -1);
        String courseName = intent.getStringExtra("course_name");
        String courseSigle = intent.getStringExtra("course_sigle");
        String courseTeacher = intent.getStringExtra("course_teacher");
        String courseSession = intent.getStringExtra("course_session");

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
    }

    private void loadCourseImage(int courseId) {
        byte[] imageBytes = dbHelper.getCourseImage(courseId); // Recupera la imagen desde la base de datos
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageCours.setImageBitmap(bitmap); // Muestra la imagen en el ImageView
        } else {
            Toast.makeText(this, "Imagen no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCourseFile(int courseId) {
        byte[] fileData = dbHelper.getCourseFile(courseId); // Recupera el archivo desde la base de datos
        if (fileData != null) {
            Log.d("File Size", "Tamaño del archivo: " + fileData.length + " bytes");
            downloadFile(fileData, "document.pdf"); // Descarga el archivo como "document.pdf"
        } else {
            Toast.makeText(this, "Archivo no disponible", Toast.LENGTH_SHORT).show();
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
            // Obtiene la carpeta "Downloads" en el almacenamiento externo
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Verifica si el directorio Downloads existe, si no, lo crea
            if (!downloadDir.exists()) {
                if (!downloadDir.mkdirs()) {
                    Log.e("File Download", "No se pudo acceder a la carpeta Downloads.");
                    Toast.makeText(this, "Erreur lors de l'accès au dossier des téléchargements.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Crea el archivo en el directorio Downloads
            File file = new File(downloadDir, fileName);

            // Escribe los datos en el archivo
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileData);
            fos.close();

            // Verifica si el archivo existe y genera el log
            if (file.exists()) {
                Log.d("File Download", "Archivo guardado en: " + file.getAbsolutePath());
                Toast.makeText(this, "Téléchargement réussi: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

                // Refresca la carpeta de descargas para que el archivo sea visible
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                scanIntent.setData(uri);
                sendBroadcast(scanIntent);

                // Abre automáticamente el archivo PDF después de la descarga
                Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                openFileIntent.setDataAndType(uri, "application/pdf");
                openFileIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Verifica si hay una aplicación disponible para abrir el archivo
                if (openFileIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(openFileIntent);
                } else {
                    // Si no hay aplicaciones para abrir PDF, muestra un mensaje
                    Toast.makeText(this, "No hay aplicaciones disponibles para abrir este archivo.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d("File Download", "No se pudo guardar el archivo.");
                Toast.makeText(this, "Erreur lors de la sauvegarde du fichier.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("File Download", "Error al descargar el archivo: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du téléchargement du fichier.", Toast.LENGTH_SHORT).show();
        }
    }











}
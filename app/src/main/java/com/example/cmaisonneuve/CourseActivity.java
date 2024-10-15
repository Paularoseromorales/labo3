package com.example.cmaisonneuve;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CourseActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Constante para seleccionar imágenes
    private static final int PICK_FILE_REQUEST = 2; // Constante para seleccionar archivos

    private TextView courseNameText;
    private TextView sigleCourseText;
    private TextView teacherName;
    private TextView sessionText;
    private Button addCourseButton;
    private Button selectImageButton;
    private Button selectFileButton;
    private TextView imageNameTextView;
    private TextView fileNameTextView;
    private DatabaseHelper db;

    private byte[] imageData;
    private byte[] fileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        courseNameText = findViewById(R.id.nomcourstext);
        sigleCourseText = findViewById(R.id.siglecourstext);
        teacherName = findViewById(R.id.enseignanttext);
        sessionText = findViewById(R.id.sessioncours);
        addCourseButton = findViewById(R.id.addcourse);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectFileButton = findViewById(R.id.selectFileButton);
        imageNameTextView = findViewById(R.id.imageNameTextView);
        fileNameTextView = findViewById(R.id.fileNameTextView);
        db = new DatabaseHelper(this);


        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = courseNameText.getText().toString();
                String sigle = sigleCourseText.getText().toString();
                String teacher = teacherName.getText().toString();
                String session = sessionText.getText().toString();

                if (name.isEmpty() || sigle.isEmpty() || teacher.isEmpty() || session.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "SVP remplissez tous les champs", Toast.LENGTH_LONG).show();
                } else {

                    boolean success = db.insertCourses(new CourseItem(name, sigle, teacher, session), imageData, fileData);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Cours ajouté avec succès", Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                        fragmentTransaction.replace(R.id.fragment_container, new ListCoursFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur lors de l'insertion du cours...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }


    // Méthode pour ouvrir le sélecteur d'image
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
    }


    // Méthode pour ouvrir le sélecteur de fichiers
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Archivo"), PICK_FILE_REQUEST);
    }

    // Gérer le résultat des sélections
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                imageData = getBytesFromUri(imageUri);

                String imageName = imageUri.getLastPathSegment();
                imageNameTextView.setText(imageName != null ? imageName : "Imagen no disponible");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            try {
                fileData = getBytesFromUri(fileUri);

                String fileName = fileUri.getLastPathSegment();
                fileNameTextView.setText(fileName != null ? fileName : "Fichier non disponible");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour convertir un Uri en octet[]
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
//package com.example.cmaisonneuve;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.cmaisonneuve.db.DatabaseHelper;
//import android.content.Intent;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.cmaisonneuve.db.DatabaseHelper;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class CourseActivity extends AppCompatActivity {
//    private static final int PICK_IMAGE_REQUEST = 1; // Constante para seleccionar imágenes
//    private static final int PICK_FILE_REQUEST = 2; // Constante para seleccionar archivos
//
//
//    private EditText courseNameText;
//    private EditText sigleCourseText;
//    private EditText teacherName;
//    private EditText sessionText;
//    private Button addCourseButton;
//    private Button selectImageButton; // Botón para seleccionar imagen
//    private Button selectFileButton; // Botón para seleccionar archivo
//    private TextView imageNameTextView; // TextView para mostrar el nombre de la imagen
//    private TextView fileNameTextView; // TextView para mostrar el nombre del archivo
//    private DatabaseHelper db;
//
//    private byte[] imageData; // Datos de la imagen
//    private byte[] fileData; // Datos del archivo
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_course);
//
//        courseNameText = findViewById(R.id.nomcourstext);
//        sigleCourseText = findViewById(R.id.siglecourstext);
//        teacherName = findViewById(R.id.enseignanttext);
//        sessionText = findViewById(R.id.sessioncours);
//        addCourseButton = findViewById(R.id.addcourse);
//        selectImageButton = findViewById(R.id.selectImageButton); // Botón para seleccionar la imagen
//        selectFileButton = findViewById(R.id.selectFileButton); // Botón para seleccionar el archivo
//        imageNameTextView = findViewById(R.id.imageNameTextView); // TextView para mostrar el nombre de la imagen
//        fileNameTextView = findViewById(R.id.fileNameTextView); // TextView para mostrar el nombre del archivo
//        db = new DatabaseHelper(this);
//
//        // Botón para seleccionar imagen
//        selectImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openImageChooser();
//            }
//        });
//
//        // Botón para seleccionar archivo
//        selectFileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFileChooser();
//            }
//        });
//
//        // Botón para agregar curso
//        addCourseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Recolectar datos del formulario
//                String name = courseNameText.getText().toString();
//                String sigle = sigleCourseText.getText().toString();
//                String teacher = teacherName.getText().toString();
//                String session = sessionText.getText().toString();
//
//                if (name.isEmpty() || sigle.isEmpty() || teacher.isEmpty() || session.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "SVP remplissez tous les champs", Toast.LENGTH_LONG).show();
//                } else {
//                    // Llama a insertCourses pasando los datos de imagen y archivo
//                    boolean success = db.insertCourses(new CourseItem(name, sigle, teacher, session), imageData, fileData);
//                    if (success) {
//                        Toast.makeText(getApplicationContext(), "Cours ajouté avec succès", Toast.LENGTH_LONG).show();
//                        finish(); // Termina la actividad si la inserción fue exitosa
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Erreur lors de l'insertion du cours...", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//    }
//
//        // Verifier si l'activite est restaure (par exemple, apres une rotation
//        // pour eviter d ajouter le fragment a nouveau.
////        if (savedInstanceState == null) {
////            // Ajouter le fragment inscription
////            InscriptionFragment inscriptionFragment = new InscriptionFragment();
////
////            // Demarrer un transcation pour ajouter le fragment
////            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////
////            // Ajouter le frangment dans le container (FrameLayout)
////            transaction.replace(R.id.fragment_container, inscriptionFragment);
////
////            //valider la transaction
////            transaction.commit();
////        }
//
//
//        // Método para abrir el selector de imágenes
//    }
//    // Método para abrir el selector de imágenes
//    private void openImageChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
//    }
//
//    // Método para abrir el selector de archivos
//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("*/*"); // Permite seleccionar cualquier tipo de archivo
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Seleccionar Archivo"), PICK_FILE_REQUEST);
//    }
//
//    // Manejar el resultado de las selecciones
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            try {
//                imageData = getBytesFromUri(imageUri);
//                // Mostrar el nombre del archivo seleccionado
//                String imageName = imageUri.getLastPathSegment(); // Obtiene el nombre del archivo
//                imageNameTextView.setText(imageName != null ? imageName : "Imagen no disponible"); // Actualiza el TextView con el nombre de la imagen
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri fileUri = data.getData();
//            try {
//                fileData = getBytesFromUri(fileUri);
//                // Mostrar el nombre del archivo seleccionado
//                String fileName = fileUri.getLastPathSegment(); // Obtiene el nombre del archivo
//                fileNameTextView.setText(fileName != null ? fileName : "Archivo no disponible"); // Actualiza el TextView con el nombre del archivo
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Método para convertir un Uri a byte[]
//    private byte[] getBytesFromUri(Uri uri) throws IOException {
//        InputStream inputStream = getContentResolver().openInputStream(uri);
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//        return byteBuffer.toByteArray(); // Devuelve el contenido como byte[]
//    }
//
//}

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

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CourseActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Constante para seleccionar imágenes
    private static final int PICK_FILE_REQUEST = 2; // Constante para seleccionar archivos

    private EditText courseNameText;
    private EditText sigleCourseText;
    private EditText teacherName;
    private EditText sessionText;
    private Button addCourseButton;
    private Button selectImageButton; // Botón para seleccionar imagen
    private Button selectFileButton; // Botón para seleccionar archivo
    private TextView imageNameTextView; // TextView para mostrar el nombre de la imagen
    private TextView fileNameTextView; // TextView para mostrar el nombre del archivo
    private DatabaseHelper db;

    private byte[] imageData; // Datos de la imagen
    private byte[] fileData; // Datos del archivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // Inicializar vistas
        courseNameText = findViewById(R.id.nomcourstext);
        sigleCourseText = findViewById(R.id.siglecourstext);
        teacherName = findViewById(R.id.enseignanttext);
        sessionText = findViewById(R.id.sessioncours);
        addCourseButton = findViewById(R.id.addcourse);
        selectImageButton = findViewById(R.id.selectImageButton); // Botón para seleccionar la imagen
        selectFileButton = findViewById(R.id.selectFileButton); // Botón para seleccionar el archivo
        imageNameTextView = findViewById(R.id.imageNameTextView); // TextView para mostrar el nombre de la imagen
        fileNameTextView = findViewById(R.id.fileNameTextView); // TextView para mostrar el nombre del archivo
        db = new DatabaseHelper(this);

        // Botón para seleccionar imagen
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Botón para seleccionar archivo
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        // Botón para agregar curso
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recolectar datos del formulario
                String name = courseNameText.getText().toString();
                String sigle = sigleCourseText.getText().toString();
                String teacher = teacherName.getText().toString();
                String session = sessionText.getText().toString();

                if (name.isEmpty() || sigle.isEmpty() || teacher.isEmpty() || session.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "SVP remplissez tous les champs", Toast.LENGTH_LONG).show();
                } else {
                    // Llama a insertCourses pasando los datos de imagen y archivo
                    boolean success = db.insertCourses(new CourseItem(name, sigle, teacher, session), imageData, fileData);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Cours ajouté avec succès", Toast.LENGTH_LONG).show();
                        finish(); // Termina la actividad si la inserción fue exitosa
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur lors de l'insertion du cours...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    // Método para abrir el selector de imágenes
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
    }

    // Método para abrir el selector de archivos
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*"); // Permite seleccionar cualquier tipo de archivo
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Archivo"), PICK_FILE_REQUEST);
    }

    // Manejar el resultado de las selecciones
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                imageData = getBytesFromUri(imageUri);
                // Mostrar el nombre del archivo seleccionado
                String imageName = imageUri.getLastPathSegment(); // Obtiene el nombre del archivo
                imageNameTextView.setText(imageName != null ? imageName : "Imagen no disponible"); // Actualiza el TextView con el nombre de la imagen
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            try {
                fileData = getBytesFromUri(fileUri);
                // Mostrar el nombre del archivo seleccionado
                String fileName = fileUri.getLastPathSegment(); // Obtiene el nombre del archivo
                fileNameTextView.setText(fileName != null ? fileName : "Archivo no disponible"); // Actualiza el TextView con el nombre del archivo
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para convertir un Uri a byte[]
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray(); // Devuelve el contenido como byte[]
    }
}
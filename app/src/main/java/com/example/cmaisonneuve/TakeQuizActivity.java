package com.example.cmaisonneuve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmaisonneuve.db.DatabaseHelper;

public class TakeQuizActivity extends AppCompatActivity {

    private TextView question1Text, question2Text, question3Text;
    private RadioGroup question1Group, question2Group, question3Group;
    private Button submitQuizButton;
    private int correctAnswer1, correctAnswer2, correctAnswer3;
    private int courseId; // ID del curso al que pertenece el quiz
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        // Inicializa el DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Inicializa los TextViews y RadioGroups para las preguntas y respuestas
        question1Text = findViewById(R.id.question1Text);
        question1Group = findViewById(R.id.question1Group);
        question2Text = findViewById(R.id.question2Text);
        question2Group = findViewById(R.id.question2Group);
        question3Text = findViewById(R.id.question3Text);
        question3Group = findViewById(R.id.question3Group);
        submitQuizButton = findViewById(R.id.submitQuizButton);

        // Recupera el ID del curso desde el Intent
        courseId = getIntent().getIntExtra("course_id", -1);

        // Carga el quiz asociado con este curso
        loadQuiz(courseId);

        // Configura el botón de enviar para calcular el puntaje
        submitQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = calculateScore(); // Calcula el puntaje del quiz
                Toast.makeText(TakeQuizActivity.this, "Tu puntaje es: " + score + "/3", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Método para cargar el quiz desde la base de datos basado en el ID del curso
    private void loadQuiz(int courseId) {
        Cursor cursor = dbHelper.getQuizByCourseId(courseId);
        if (cursor != null && cursor.moveToFirst()) {
            // Cargar la pregunta 1
            question1Text.setText(cursor.getString(cursor.getColumnIndexOrThrow("question1")));
            correctAnswer1 = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer_q1"));

            // Cargar las opciones de la pregunta 1
            ((RadioButton) findViewById(R.id.option1Q1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option1_q1")));
            ((RadioButton) findViewById(R.id.option2Q1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option2_q1")));
            ((RadioButton) findViewById(R.id.option3Q1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option3_q1")));

            // Cargar la pregunta 2
            question2Text.setText(cursor.getString(cursor.getColumnIndexOrThrow("question2")));
            correctAnswer2 = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer_q2"));

            // Cargar las opciones de la pregunta 2
            ((RadioButton) findViewById(R.id.option1Q2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option1_q2")));
            ((RadioButton) findViewById(R.id.option2Q2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option2_q2")));
            ((RadioButton) findViewById(R.id.option3Q2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option3_q2")));

            // Cargar la pregunta 3
            question3Text.setText(cursor.getString(cursor.getColumnIndexOrThrow("question3")));
            correctAnswer3 = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer_q3"));

            // Cargar las opciones de la pregunta 3
            ((RadioButton) findViewById(R.id.option1Q3)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option1_q3")));
            ((RadioButton) findViewById(R.id.option2Q3)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option2_q3")));
            ((RadioButton) findViewById(R.id.option3Q3)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option3_q3")));
        }
    }

    // Método para calcular el puntaje del quiz basado en las respuestas seleccionadas
    private int calculateScore() {
        int score = 0;

        // Verificar la respuesta de la pregunta 1
        int selectedAnswer1Index = getSelectedAnswerIndex(question1Group);
        if (selectedAnswer1Index == correctAnswer1) {
            score++;
        }

        // Verificar la respuesta de la pregunta 2
        int selectedAnswer2Index = getSelectedAnswerIndex(question2Group);
        if (selectedAnswer2Index == correctAnswer2) {
            score++;
        }

        // Verificar la respuesta de la pregunta 3
        int selectedAnswer3Index = getSelectedAnswerIndex(question3Group);
        if (selectedAnswer3Index == correctAnswer3) {
            score++;
        }

        return score;
    }

    // Método para obtener el índice de la opción seleccionada
    private int getSelectedAnswerIndex(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return radioGroup.indexOfChild(selectedRadioButton) + 1; // Devuelve el índice (1, 2, 3)
        }
        return -1; // Devuelve -1 si no se seleccionó ninguna respuesta
    }

}

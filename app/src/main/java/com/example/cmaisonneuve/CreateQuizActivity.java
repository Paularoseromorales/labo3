package com.example.cmaisonneuve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmaisonneuve.db.DatabaseHelper;

public class CreateQuizActivity extends AppCompatActivity {

    private EditText question1Text, question2Text, question3Text;
    private EditText option1Q1, option2Q1, option3Q1;
    private EditText option1Q2, option2Q2, option3Q2;
    private EditText option1Q3, option2Q3, option3Q3;
    private RadioGroup correctAnswerQ1, correctAnswerQ2, correctAnswerQ3;
    private Button saveQuizButton;
    private int courseId;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        dbHelper = new DatabaseHelper(this);

        courseId = getIntent().getIntExtra("course_id", -1);
        if (courseId == -1) {
            Toast.makeText(this, "Erreur Impossible d'obtenir l'ID du cours", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        question1Text = findViewById(R.id.question1Text);
        option1Q1 = findViewById(R.id.option1Q1);
        option2Q1 = findViewById(R.id.option2Q1);
        option3Q1 = findViewById(R.id.option3Q1);
        correctAnswerQ1 = findViewById(R.id.correctAnswerQ1);

        question2Text = findViewById(R.id.question2Text);
        option1Q2 = findViewById(R.id.option1Q2);
        option2Q2 = findViewById(R.id.option2Q2);
        option3Q2 = findViewById(R.id.option3Q2);
        correctAnswerQ2 = findViewById(R.id.correctAnswerQ2);

        question3Text = findViewById(R.id.question3Text);
        option1Q3 = findViewById(R.id.option1Q3);
        option2Q3 = findViewById(R.id.option2Q3);
        option3Q3 = findViewById(R.id.option3Q3);
        correctAnswerQ3 = findViewById(R.id.correctAnswerQ3);

        saveQuizButton = findViewById(R.id.saveQuizButton);


        saveQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuiz();
            }
        });
    }

    private void saveQuiz() {

        String question1 = question1Text.getText().toString();
        String option1_1 = option1Q1.getText().toString();
        String option1_2 = option2Q1.getText().toString();
        String option1_3 = option3Q1.getText().toString();
        int correctAnswer1 = getSelectedAnswer(correctAnswerQ1);

        String question2 = question2Text.getText().toString();
        String option2_1 = option1Q2.getText().toString();
        String option2_2 = option2Q2.getText().toString();
        String option2_3 = option3Q2.getText().toString();
        int correctAnswer2 = getSelectedAnswer(correctAnswerQ2);

        String question3 = question3Text.getText().toString();
        String option3_1 = option1Q3.getText().toString();
        String option3_2 = option2Q3.getText().toString();
        String option3_3 = option3Q3.getText().toString();
        int correctAnswer3 = getSelectedAnswer(correctAnswerQ3);


        if (question1.isEmpty() || question2.isEmpty() || question3.isEmpty()) {
            Toast.makeText(this, "Veuillez répondre à toutes les questions", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean isInserted = dbHelper.insertQuiz(courseId,
                question1, option1_1, option1_2, option1_3, correctAnswer1,
                question2, option2_1, option2_2, option2_3, correctAnswer2,
                question3, option3_1, option3_2, option3_3, correctAnswer3);

        if (isInserted) {
            Toast.makeText(this, "Quiz enregistré avec succès", Toast.LENGTH_SHORT).show();
            finish(); // Termina la actividad y regresa a la anterior
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement du quiz", Toast.LENGTH_SHORT).show();
        }
    }
    private int getSelectedAnswerIndex(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return radioGroup.indexOfChild(selectedRadioButton) + 1;
        }
        return -1;
    }
    // Méthode pour obtenir la réponse correcte sélectionnée d'un RadioGroup
    private int getSelectedAnswer(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return radioGroup.indexOfChild(selectedRadioButton) + 1; // Devuelve el índice de la opción seleccionada (1, 2, o 3)
        }
        return -1;
    }
}

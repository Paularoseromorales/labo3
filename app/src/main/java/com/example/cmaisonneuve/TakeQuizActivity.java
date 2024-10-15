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
    private int courseId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);


        dbHelper = new DatabaseHelper(this);

        question1Text = findViewById(R.id.question1Text);
        question1Group = findViewById(R.id.question1Group);
        question2Text = findViewById(R.id.question2Text);
        question2Group = findViewById(R.id.question2Group);
        question3Text = findViewById(R.id.question3Text);
        question3Group = findViewById(R.id.question3Group);
        submitQuizButton = findViewById(R.id.submitQuizButton);

        courseId = getIntent().getIntExtra("course_id", -1);

        loadQuiz(courseId);

        submitQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = calculateScore();
                Toast.makeText(TakeQuizActivity.this, "Votre score est : " + score + "/3", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Méthode pour charger le quiz depuis la base de données en fonction de l'ID du cours
    private void loadQuiz(int courseId) {
        Cursor cursor = dbHelper.getQuizByCourseId(courseId);
        if (cursor != null && cursor.moveToFirst()) {

            question1Text.setText(cursor.getString(cursor.getColumnIndexOrThrow("question1")));
            correctAnswer1 = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer_q1"));

            ((RadioButton) findViewById(R.id.option1Q1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option1_q1")));
            ((RadioButton) findViewById(R.id.option2Q1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option2_q1")));
            ((RadioButton) findViewById(R.id.option3Q1)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option3_q1")));


            question2Text.setText(cursor.getString(cursor.getColumnIndexOrThrow("question2")));
            correctAnswer2 = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer_q2"));


            ((RadioButton) findViewById(R.id.option1Q2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option1_q2")));
            ((RadioButton) findViewById(R.id.option2Q2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option2_q2")));
            ((RadioButton) findViewById(R.id.option3Q2)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option3_q2")));


            question3Text.setText(cursor.getString(cursor.getColumnIndexOrThrow("question3")));
            correctAnswer3 = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer_q3"));


            ((RadioButton) findViewById(R.id.option1Q3)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option1_q3")));
            ((RadioButton) findViewById(R.id.option2Q3)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option2_q3")));
            ((RadioButton) findViewById(R.id.option3Q3)).setText(cursor.getString(cursor.getColumnIndexOrThrow("option3_q3")));
        }
    }

    // Méthode pour calculer le score du quiz en fonction des réponses sélectionnées
    private int calculateScore() {
        int score = 0;

        int selectedAnswer1Index = getSelectedAnswerIndex(question1Group);
        if (selectedAnswer1Index == correctAnswer1) {
            score++;
        }

        int selectedAnswer2Index = getSelectedAnswerIndex(question2Group);
        if (selectedAnswer2Index == correctAnswer2) {
            score++;
        }

        int selectedAnswer3Index = getSelectedAnswerIndex(question3Group);
        if (selectedAnswer3Index == correctAnswer3) {
            score++;
        }

        return score;
    }

    // Méthode pour obtenir l'index de l'option sélectionnée
    private int getSelectedAnswerIndex(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return radioGroup.indexOfChild(selectedRadioButton) + 1; // Devuelve el índice (1, 2, 3)
        }
        return -1;
    }

}

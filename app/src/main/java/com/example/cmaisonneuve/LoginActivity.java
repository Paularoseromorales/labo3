package com.example.cmaisonneuve;



import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.cmaisonneuve.db.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button connectionButton;
    private TextView registerText;
    private TextView forgotPasswordText;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameTextID);
        passwordEditText = findViewById(R.id.passwordTextID);
        connectionButton = findViewById(R.id.connexionBoutonID);
        registerText = findViewById(R.id.registerText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

         db = new DatabaseHelper(this);

        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);

                    User loggedInUser = db.checkUser(user); // Recuperar el objeto User si es válido

                    if (loggedInUser != null) {
                        // Mostrar un Toast con el ID y nombre completo del usuario
                        Toast.makeText(getApplicationContext(), "Bienvenue, " + loggedInUser.getFullname() + "!", Toast.LENGTH_LONG).show();

                        // Guardar el ID del usuario en SharedPreferences
                        // LoginActivity.java
                        // En el LoginActivity.java
                        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("currentUserId", loggedInUser.getId());  // Guardar el userId en SharedPreferences correctamente
                        editor.apply();



                        // Navegar a la actividad principal
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user", loggedInUser);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Utilisateur introuvable!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
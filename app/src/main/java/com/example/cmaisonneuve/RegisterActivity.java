package com.example.cmaisonneuve;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText fullnameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       fullnameEditText = findViewById(R.id.fullnameTextID);
        usernameEditText = findViewById(R.id.usernameTextID);
        passwordEditText = findViewById(R.id.passwordTextID);
        registerButton = findViewById(R.id.registerButtonID);

          db = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = fullnameEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(fullname.isEmpty() || username.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User();
                    user.setFullname(fullname);
                    user.setUsername(username);
                    user.setPassword(password);
                    boolean success = db.insertUser(user);
                    if(success) {
                        Toast.makeText(getApplicationContext(), "Utilisateur cree avec sucess..", Toast.LENGTH_LONG).show();
                        finish();
                        // naviguer vers la vue login
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Impossible d'enregister le compte", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }
}
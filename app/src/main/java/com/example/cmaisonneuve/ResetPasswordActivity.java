package com.example.cmaisonneuve;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResetPasswordActivity extends AppCompatActivity {
    
    private EditText emailEditText;
    private Button resetPasswordButton;
    // a ajouter les deux autres textview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_reset_password);
         // referencer les elements graphiques
        emailEditText = findViewById(R.id.emailTextID);
        resetPasswordButton = findViewById(R.id.resetPasswordID);
        // mettre le bouton a l ecoute
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                if (email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Veuillez entrer votre adresse e-mail", Toast.LENGTH_LONG).show();
                } else {
                    //Afficher la boite de dialogue d envoie couriel..
                    showSendEmailDialog(email);
                }
            }
        });
         
    }

    private void showSendEmailDialog(String email) {
        AlertDialog.Builder emailDialogBuilder = new AlertDialog.Builder(this);
        emailDialogBuilder.setTitle("Reinitialisation mot passe")
                          .setMessage("Etes vous sur de vouloir reinitialiser votre mot passe?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Un lien de reinitialisation a ete envoye a " + email, Toast.LENGTH_LONG).show();}
                })
                .setNegativeButton("Non", null);

               AlertDialog dialog = emailDialogBuilder.create();
               dialog.show();
    }
}
package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bdd.Bdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nutrijoy.R;

public class PageResetPassword extends AppCompatActivity {
    Button envoi,retour;
    EditText email;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);
        envoi = findViewById(R.id.btnEnvoi);
        retour = findViewById(R.id.btnRetour);
        email=findViewById(R.id.editTextEmail);
        message=findViewById(R.id.textview_message);

        envoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpEmail=email.getText().toString().trim();
                if(!tmpEmail.isEmpty()) {
                    Bdd.envoiResetEmail(tmpEmail, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Gérer la réussite

                                Toast.makeText(getApplicationContext(),"Un lien réinitialisation vous sera envoyé.",Toast.LENGTH_LONG).show();

                            } else {
                                //Gérer l'échec
                                email.setError("Echec de la demande de réinitialisation.");
                                Toast.makeText(getApplicationContext(),"Echec de la demande de réinitialisation.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    email.setError("Email invalide.");
                }
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAccueilActivity();
            }
        });

    }

    private void goToAccueilActivity(){
        finish();
    }
}
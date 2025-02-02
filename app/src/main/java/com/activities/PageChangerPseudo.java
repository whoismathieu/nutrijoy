package com.activities;

import android.annotation.SuppressLint;
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

import org.bson.Document;

import io.realm.mongodb.App;

public class PageChangerPseudo extends AppCompatActivity {
    Button confirmer,retour;
    EditText pseudo;
    TextView message,ancienPseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_username);
        confirmer = findViewById(R.id.btnConfirmerPseudo);
        retour = findViewById(R.id.btnRetour);
        pseudo=findViewById(R.id.editTextPseudo);
        ancienPseudo=findViewById(R.id.textview_ancienPseudo);
        message=findViewById(R.id.textview_message);

        refreshUsername();

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpPseudo=pseudo.getText().toString();
                if(!tmpPseudo.isEmpty()) {
                    Bdd.modifierUsername(pseudo.getText().toString(), new App.Callback<Document>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResult(App.Result<Document> result) {
                            if (result.isSuccess()) {
                                Toast.makeText(getApplicationContext(),"Modification réussie.",Toast.LENGTH_SHORT).show();
                                refreshUsername();
                            } else {
                                Toast.makeText(getApplicationContext(),"Modification échouée.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    pseudo.setError("Pseudonyme invalide.");
                }
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retour à la page précédente.
                finish();
            }
        });

    }

    private void refreshUsername(){
        Bdd.recupererUsername(new App.Callback<String>() {
            @Override
            public void onResult(App.Result<String> result) {
                if(result.isSuccess()){
                    ancienPseudo.setText("Pseudo actuel : "+result.get());
                }else{
                    ancienPseudo.setText("APseudo actuel : Erreur lors de la récupération du pseudo.");
                }
            }
        });
    }
}
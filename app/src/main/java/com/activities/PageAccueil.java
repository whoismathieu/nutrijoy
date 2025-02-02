package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bdd.Bdd;
import com.nutrijoy.R;

public class PageAccueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bdd.checkIfSavedUser(new Bdd.ConnexionCallback(){
            @Override
            public void onConnexionSuccess() {
                //Le initUser va toujours trouver un user car un user
                //est toujours créé lors du build de l'app. (voir Bdd.init())
                //Le token récupéré est null
                goToMainActivity();
            }
            @Override
            public void onConnexionFailed(Exception e) {
                creerContenu();
            }
        });


    }

    private void creerContenu(){
        setContentView(R.layout.activity_page_accueil);
        Button connexionbutton = findViewById(R.id.connexionbutton);
        Button createaccountbutton = findViewById(R.id.creationbutton);

        connexionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToConnexionActivity();
            }
        });

        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateActivity();
            }
        });
    }

    private void goToMainActivity(){
        Intent intent=new Intent(PageAccueil.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToConnexionActivity(){
        Intent intent = new Intent(this, Connexion.class);
        startActivity(intent);
        finish();
    }

    private void goToCreateActivity(){
        Intent intent = new Intent(this, CreationCompte.class);
        startActivity(intent);
        finish();
    }
}
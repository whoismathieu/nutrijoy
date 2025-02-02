package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bdd.Bdd;
import com.nutrijoy.R;
import com.outils.PasswordService;

import org.bson.Document;

import io.realm.mongodb.App;

public class CreationCompte extends AppCompatActivity {

    EditText mUsername,mEmail,mPassword,mConfirmPwd;
    Button mBackbutton,mNextbutton;
    ProgressBar roueChargementCreationCompte;
    TextView mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);

        mUsername = findViewById(R.id.usernameEdit);
        mEmail = findViewById(R.id.editText_email);
        mPassword = findViewById(R.id.passwordEdit);
        mConfirmPwd = findViewById(R.id.edit_confirmpassword);
        mNextbutton = findViewById(R.id.suivant2);
        mBackbutton = findViewById(R.id.backbutton2);
        roueChargementCreationCompte=findViewById(R.id.roueChargementCreationCompte);
        roueChargementCreationCompte.setVisibility(View.INVISIBLE);
        mMessage = findViewById(R.id.textview_erreurcreation);


        mBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAccueilActivitybis();
            }
        });

        mNextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roueChargementCreationCompte.setVisibility(View.VISIBLE);
                final String username = mUsername.getText().toString().trim();
                final String email = mEmail.getText().toString().trim(); //.trim() enlève les espaces blancs avant et après la chaine
                String password = mPassword.getText().toString();
                String confirmpwd = mConfirmPwd.getText().toString();


                if(verifInfos(username,email,password,confirmpwd)){

                    Bdd.creerCompteFirebase(email, PasswordService.hashPassword(password), new Bdd.ConnexionCallback() {
                        @Override
                        public void onConnexionSuccess() {
                            Log.v("CREATION","Création du compte réussie.");


                            Bdd.connexionJwt( new Bdd.ConnexionCallback() {
                                @Override
                                public void onConnexionSuccess() {
                                    Bdd.modifierUsername(username, new App.Callback<Document>() {
                                        @Override
                                        public void onResult(App.Result<Document> result) {
                                            if (result.isSuccess()) {
                                                Log.v("Creation", "Initialisation username réussi.");

                                            }else{
                                                Log.v("Creation", "Initialisation username échoué.");
                                            }
                                        }
                                    });
                                    roueChargementCreationCompte.setVisibility(View.GONE);
                                    goToMainActivity();
                                }

                                @Override
                                public void onConnexionFailed(Exception e) {
                                    roueChargementCreationCompte.setVisibility(View.GONE);
                                    mMessage.setText("Erreur, un compte existe peut-être déjà : "+e.getMessage());
                                }
                            });

                        }

                        @Override
                        public void onConnexionFailed(Exception e) {
                            mMessage.setText("Erreur, un compte existe peut-être déjà. : "+e.getMessage());
                        }


                    });
                }
                else{
                    Log.v("COMPTE","Erreur création compte.");
                }
            }
        });
    }

    private boolean verifInfos(final String username,final String email,String password,String confirmpwd){
        boolean resultat=true;

        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("E-mail invalide.");
            resultat=false;
        }

        if(TextUtils.isEmpty(username)){
            mUsername.setError("Username requis");
            resultat=false;
        }

        if(TextUtils.isEmpty(password)){
            mPassword.setError("Mot de passe requis");
            resultat=false;
        }
        if(password.length()<6){
            mPassword.setError("Le mot de passe doit contenir au minimum 6 caractères");
            resultat=false;
        }
        if(!password.equals(confirmpwd)){
            mConfirmPwd.setError("Les mots de passe ne correspondent pas");
            resultat=false;
        }



        return resultat;
    }

    private void goToMainActivity(){
        startActivity(new Intent(CreationCompte.this,MainActivity.class));
        finish();
    }

    private void goToAccueilActivitybis(){
        Intent intent = new Intent(this, PageAccueil.class);
        startActivity(intent);
        finish();
    }
}
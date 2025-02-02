package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bdd.Bdd;
import com.google.android.material.textfield.TextInputEditText;
import com.nutrijoy.R;
import com.outils.PasswordService;

public class Connexion extends AppCompatActivity {

    private boolean passwordVisible;

    private Button backbutton,valider;
    private EditText barreEmail;
    private EditText barrePassword;
    private TextView message,mdpOublie;

    private ProgressBar roueChargementConnexion;

    private int estVisible;
    int visibiliteBase;
    private ImageView btnVisibity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connexion);

        backbutton = findViewById(R.id.backbutton);
        valider = findViewById(R.id.nextbutton);
        barreEmail = findViewById(R.id.editTextTextEmailAddress);
        barrePassword = findViewById(R.id.editTextPassword);
        estVisible=barrePassword.getInputType();
        visibiliteBase=estVisible;
        message = findViewById(R.id.textview_erreurconnexion);
        mdpOublie=findViewById(R.id.forgetpwd);
        roueChargementConnexion=findViewById(R.id.roueChargementConnexion);
        roueChargementConnexion.setVisibility(View.INVISIBLE);
        message.setVisibility(View.GONE);
        btnVisibity=findViewById(R.id.btnVisibility);
        btnVisibity.setSelected(false);


        //set clicks listeners
        setClicks();

        //End OnCreate
    }

    private void setClicks(){
/*
        barrePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getRawX()>= barrePassword.getRight()-barrePassword.getCompoundDrawables()[Right].getBounds().width()){
                    int selection = barrePassword.getSelectionEnd();
                    if(passwordVisible){
                        barrePassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visible24,0);
                        barrePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    }else{
                        barrePassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.invisible24,0);
                        barrePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    barrePassword.setSelection(selection);
                    return true;
                }
                return false;
            }
        });

        */
        btnVisibity.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                btnVisibity.setSelected(!btnVisibity.isSelected());
                Log.v("TESTVISIBLE", estVisible+"");
                if(estVisible==InputType.TYPE_CLASS_TEXT) {
                    barrePassword.setInputType(visibiliteBase);
                    estVisible = visibiliteBase;
                }else{
                    barrePassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    estVisible=InputType.TYPE_CLASS_TEXT;
                }



            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAccueilActivity();
            }
        });

        mdpOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMdpOublie();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roueChargementConnexion.setVisibility(View.VISIBLE);
                Log.v("LOGIN","PASSE :"+barreEmail.getText().toString().trim()+barrePassword.getText().toString());
                String tmpEmail=barreEmail.getText().toString().trim();
                String tmpPassword=PasswordService.hashPassword(barrePassword.getText().toString());
                if(!(tmpEmail.isEmpty() || tmpPassword.isEmpty()) ) {
                    Bdd.connexionFirebase(tmpEmail, tmpPassword, new Bdd.ConnexionCallback() {
                        @Override
                        public void onConnexionSuccess() {

                            //Suppression d'un potentiel ancien token et ajout de celui
                            //du compte connecté.


                            Bdd.connexionJwt(new Bdd.ConnexionCallback() {
                                @Override
                                public void onConnexionSuccess() {


                                    Log.v("LOGIN", "Login réussi.");

                                    Toast.makeText(getApplicationContext(),"Connexion réussie.",Toast.LENGTH_LONG).show();
                                    roueChargementConnexion.setVisibility(View.GONE);
                                    goToMainActivity();
                                }

                                @Override
                                public void onConnexionFailed(Exception e) {
                                    roueChargementConnexion.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Email ou mot de passe invalide.",Toast.LENGTH_LONG).show();
                                }
                            });


                        }

                        @Override
                        public void onConnexionFailed(Exception e) {
                            roueChargementConnexion.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Connexion échouée.",Toast.LENGTH_LONG).show();
                            Log.v("LOGIN", "Login échoué.");
                        }


                    });
                }else{
                    if(TextUtils.isEmpty(tmpEmail) || !Patterns.EMAIL_ADDRESS.matcher(tmpEmail).matches()){
                        barreEmail.setError("E-mail invalide.");
                    }

                    if(TextUtils.isEmpty(tmpPassword)){
                        barrePassword.setError("Mot de passe requis");
                    }
                    roueChargementConnexion.setVisibility(View.GONE);
                }
            }


        });
    }

    private void goToMdpOublie(){
        Intent intent = new Intent(Connexion.this, PageResetPassword.class);
        startActivity(intent);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(Connexion.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToAccueilActivity(){
        Intent intent = new Intent(Connexion.this, PageAccueil.class);
        startActivity(intent);
        finish();
    }
}
package com.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bdd.Bdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nutrijoy.R;
import com.outils.PasswordService;

public class ConfirmResetPassword extends AppCompatActivity {

    private Button backbutton,valider;
    private EditText barrePassword1,barrePassword2;
    private TextView message;

    private ProgressBar roueChargementConnexion;

    private int estVisible1,estVisible2;
    private int visibiliteBase1,visibiliteBase2;
    private ImageView btnVisibility1,btnVisibility2;

    //Pour manipuler les infos du lien de reset
    private Uri lien;
    String code;
    Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_reset_password);

        backbutton = findViewById(R.id.backbutton);
        valider = findViewById(R.id.nextbutton);

        barrePassword1 = findViewById(R.id.editTextMdp1);
        barrePassword2 = findViewById(R.id.editTextMdp2);
        estVisible1=barrePassword1.getInputType();
        estVisible2=barrePassword2.getInputType();
        visibiliteBase1=estVisible1;
        visibiliteBase2=estVisible2;

        message = findViewById(R.id.textview_erreurconnexion);
        roueChargementConnexion=findViewById(R.id.roueChargementConnexion);
        roueChargementConnexion.setVisibility(View.INVISIBLE);
        message.setVisibility(View.GONE);

        btnVisibility1=findViewById(R.id.btnVisibility1);
        btnVisibility2=findViewById(R.id.btnVisibility2);
        btnVisibility1.setSelected(false);
        btnVisibility2.setSelected(false);


        //set clicks listeners
        setClicks();

        //Pour lien
        pourCode();

        //End OnCreate
    }

    private void pourCode(){
        intent1=getIntent();
        lien=intent1.getData();
        try {
            code = lien.getQueryParameter("oobCode");
            Log.v("PourCode","code réussi: "+code);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Erreur lien de reset.",Toast.LENGTH_LONG).show();
            Log.v("PourCode","code échoué : "+e.getMessage());
        }

    }

    private void setClicks(){

        btnVisibility1.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                btnVisibility1.setSelected(!btnVisibility1.isSelected());
                Log.v("TESTVISIBLE", estVisible1+"");
                if(estVisible1== InputType.TYPE_CLASS_TEXT) {
                    barrePassword1.setInputType(visibiliteBase1);
                    estVisible1 = visibiliteBase1;
                }else{
                    barrePassword1.setInputType(InputType.TYPE_CLASS_TEXT);
                    estVisible1=InputType.TYPE_CLASS_TEXT;
                }
                
            }
        });
        btnVisibility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVisibility2.setSelected(!btnVisibility2.isSelected());
                Log.v("TESTVISIBLE", estVisible2+"");
                if(estVisible2== InputType.TYPE_CLASS_TEXT) {
                    barrePassword2.setInputType(visibiliteBase2);
                    estVisible2 = visibiliteBase2;
                }else{
                    barrePassword2.setInputType(InputType.TYPE_CLASS_TEXT);
                    estVisible2=InputType.TYPE_CLASS_TEXT;
                }

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAccueilActivity();
            }
        });



        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roueChargementConnexion.setVisibility(View.VISIBLE);
                String tmpMdp1=barrePassword1.getText().toString();
                String tmpMdp2= barrePassword2.getText().toString();
                if(!(tmpMdp1.isEmpty() || tmpMdp2.isEmpty()) && tmpMdp1.equals(tmpMdp2) && tmpMdp2.length() >= 6) {
                    Bdd.resetPassword(code,PasswordService.hashPassword(tmpMdp1), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Réinitialisation réussie.",Toast.LENGTH_LONG).show();
                                roueChargementConnexion.setVisibility(View.GONE);
                                goToConnexion();
                            }else{
                                roueChargementConnexion.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Réinitialisation échouée\nLien sûrement expiré",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    if(TextUtils.isEmpty(tmpMdp1)){
                        barrePassword1.setError("Mot de passe requis");
                    }
                    if(TextUtils.isEmpty(tmpMdp2)){
                        barrePassword2.setError("Mot de passe requis");
                    }else if(!tmpMdp1.equals(tmpMdp2)){
                        barrePassword1.setError("Les mots de passe ne correspondent pas.");
                        barrePassword2.setError("Les mots de passe ne correspondent pas.");
                    } else if (tmpMdp1.length()<6) {
                        barrePassword1.setError("Le mot de passe doit contenir au moins 6 caractères.");
                        barrePassword2.setError("Le mot de passe doit contenir au moins 6 caractères.");
                    }


                    roueChargementConnexion.setVisibility(View.GONE);
                }
            }


        });
    }



    private void goToConnexion(){
        Intent intent = new Intent(ConfirmResetPassword.this, Connexion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToAccueilActivity(){
        Intent intent = new Intent(ConfirmResetPassword.this, PageAccueil.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

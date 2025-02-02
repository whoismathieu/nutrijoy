package com.activities;

import android.app.Application;
import android.util.Log;

import com.bdd.Bdd;
import com.google.firebase.FirebaseApp;
import com.network.RetrofitClient;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;


// On utilise cette classe pour préciser que la base de donnée doit être initialisé dès le lancement de l'application

public class MyApplication extends Application {

    public static boolean connexion=false;
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialise Realm
        Realm.init(this);

        //Initialise l'app mongodb (A ne faire qu'une fois)
        Bdd.init();
        RetrofitClient.getService();

        FirebaseApp.initializeApp(getApplicationContext());

    }
}

package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.activities.fragments.pagesPrincipales.FavorisFragment;
import com.activities.fragments.pagesPrincipales.ParametreFragment;
import com.activities.fragments.pagesPrincipales.RechercheFragment;
import com.activities.fragments.pagesPrincipales.StatistiqueFragment;
import com.nutrijoy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.outils.FromParam;


public class MainActivity extends AppCompatActivity {

 //Initialisation du menu (pour plus tard interagir avec)
    BottomNavigationView  bottomNavigationView;

    SharedPreferences preferences;

    @Override
    public void onResume(){
        super.onResume();
        if (FromParam.fromParam){
            moveToFragment(new ParametreFragment());
            FromParam.fromParam=false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //connecte le menu present graphiquement à la variable
        bottomNavigationView=findViewById(R.id.barreMenu);
        // Place à l'ouverture de l'app le Fragment Recherche
        moveToFragment(new RechercheFragment());

        // Méthode décrivant la suite d'instruction à faire lorsqu'on interagit avec le menu
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           // Chaque fois qu'un item est touché dans le menu, on change de fragment grâce à la méthode moveToFragment()
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_boutonRecherche) {
                    moveToFragment(new RechercheFragment());
                } else if (item.getItemId() == R.id.menu_boutonFavoris) {
                    moveToFragment(new FavorisFragment());
                } else if (item.getItemId() == R.id.menu_boutonParametre) {
                    moveToFragment(new ParametreFragment());
                } else if (item.getItemId() == R.id.menu_boutonStatistique) {
                    moveToFragment(new StatistiqueFragment());
                }

                return true;
            }
        });
        preferences = getSharedPreferences("ThemePreferences", MODE_PRIVATE);
        boolean nightMode = preferences.getBoolean("nightMode", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


    }
    // Definition de la methode moveToFragment
    public void moveToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.containerView, fragment);
        fragmentTransaction.commit();
    }

    public void rafraichir(int actif){
        AppCompatDelegate.setDefaultNightMode(actif);
        bottomNavigationView.setSelectedItemId(R.id.menu_boutonRecherche);
    }

    public BottomNavigationView getBottomNavigationView(){
        return bottomNavigationView;
    }

}








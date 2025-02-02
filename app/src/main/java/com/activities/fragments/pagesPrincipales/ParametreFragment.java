package com.activities.fragments.pagesPrincipales;


import static io.realm.Realm.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.activities.MainActivity;
import com.activities.PageAccueil;
import com.activities.PageChangerPseudo;
import com.activities.PageResetPassword;
import com.bdd.Bdd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;
import com.outils.FromParam;

import java.util.Random;

import io.realm.mongodb.App;

// Pas encore fait (Template)

public class ParametreFragment extends Fragment {
    private Button btnChangeUsername,btnChangeMdp,btnDeconnexion,btnViderHistorique,btnViderConsomme;

    private Switch switchMode;

    private boolean nightMode;

    SharedPreferences sharedPreferences;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public ParametreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_parametre, container, false);

        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonParametre).setChecked(true);

        btnDeconnexion=view.findViewById(R.id.btnDeconnexion);
        btnViderHistorique=view.findViewById(R.id.btnViderHistorique);
        btnViderConsomme=view.findViewById(R.id.btnViderConsomme);
        btnChangeMdp=view.findViewById(R.id.btnChangeMdp);
        btnChangeUsername=view.findViewById(R.id.btnChangeUsername);
        switchMode=view.findViewById(R.id.switchMode);
        preferences = getActivity().getSharedPreferences("ThemePreferences", Context.MODE_PRIVATE);
        switchMode.setChecked(preferences.getBoolean("nightMode", false));
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("nightMode", isChecked);
            editor.apply();

            // Apply the night mode immediately
            int actif=isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            //AppCompatDelegate.setDefaultNightMode();

            // Recreate activity for the theme change to take effect
            //requireActivity().recreate();
            FromParam.fromParam=true;
            ((MainActivity)requireActivity()).rafraichir(actif);
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decoIntoPageAccueil();
            }
        });

        btnViderHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bdd.suppAllHistorique(new Bdd.SuppListeCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),"Historique supprimé.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(),"Suppression échouée.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnViderConsomme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bdd.suppAllConsomme(new Bdd.SuppListeCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),"Consommés supprimés.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(),"Suppression échouée.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnChangeMdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPageResetPassword();
            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPageChangerPseudo();
            }
        });
    }

    private void goToPageChangerPseudo(){
        Intent intent=new Intent(getActivity(), PageChangerPseudo.class);
        startActivity(intent);
    }
    private void goToPageResetPassword(){
        Intent intent=new Intent(getActivity(), PageResetPassword.class);
        startActivity(intent);
    }
    private void decoIntoPageAccueil(){
        Bdd.deconnexion();
        Intent intent=new Intent(requireActivity(),PageAccueil.class);
        startActivity(intent);
        getActivity().finish();
    }
}
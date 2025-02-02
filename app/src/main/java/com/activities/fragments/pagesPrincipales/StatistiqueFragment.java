package com.activities.fragments.pagesPrincipales;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.activities.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;
import com.activities.fragments.periodeStatistique.JourFragment;
import com.activities.fragments.periodeStatistique.MoisFragment;
import com.activities.fragments.periodeStatistique.SemaineFragment;

import java.util.ArrayList;
// Pas encore fait (Template)


public class StatistiqueFragment extends Fragment {

    public StatistiqueFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_statistique, container, false);


        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonStatistique).setChecked(true);


        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = spinner.getItemAtPosition(position).toString();
                // Fragment Manager pour passer de jour, mois, semaine
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (item.equals("Jour")) {

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerStatistique, JourFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                }

                if (item.equals("Semaine")) {

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerStatistique, SemaineFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                }

                if (item.equals("Mois")) {

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerStatistique, MoisFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Définition des choix dans le menu déroulant
        ArrayList<String> choix = new ArrayList<>();
        choix.add("Jour");
        choix.add("Semaine");
        choix.add("Mois");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, choix);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);


        return view;
    }
}

package com.activities.fragments.pagesPrincipales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.activities.MainActivity;
import com.bdd.Bdd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;
import com.activities.adapers.FavorisRecycleViewAdapter;
import com.activities.adapers.interfaces.RecyclerViewInterface;

import com.activities.fragments.ManageFragment;
import com.activities.fragments.pagesResultat.ResultatScanFragment;
import com.model.produitScanne.ProduitReponse;


import java.util.ArrayList;

// Pas encore fait (Template)

public class FavorisFragment extends Fragment implements RecyclerViewInterface {

    private boolean estFavoris,estConsomme;
    RecyclerView rViewFavoris;
    ResultatScanFragment resultatFragment;

    ArrayList<ProduitReponse> favoris;

    private ProgressBar  roueChargment;



    public FavorisFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoris = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoris, container, false);

        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonFavoris).setChecked(true);

        rViewFavoris= view.findViewById(R.id.recylerview_favoris);
        roueChargment=view.findViewById(R.id.roueChargementFavoris);
        roueChargment.setVisibility(View.VISIBLE);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        affichage();
    }

    public void affichage() {
        if (Bdd.collection != null) {
            Log.d("TEST","on passe par l'affichage");


            FavorisFragment recyclerParent = this;

            Bdd.recupererFavoris(new Bdd.ListeCallback() {
                @Override
                public void onSuccess(ArrayList<ProduitReponse> liste) {
                    roueChargment.setVisibility(View.GONE);
                    Log.d("TEST","onsSucess affichage");

                    favoris= liste;

                    FavorisRecycleViewAdapter adapter = new FavorisRecycleViewAdapter(getContext(),
                            favoris, (RecyclerViewInterface) recyclerParent);
                    rViewFavoris.setAdapter(adapter);
                    rViewFavoris.setLayoutManager(new LinearLayoutManager(getContext()));
                    Log.v("TEST", "Historique récupéré correctement page favoris.\n");
                }

                @Override
                public void onFailure(Exception e) {
                    roueChargment.setVisibility(View.GONE);
                    Log.v("USER", "Historique non récupérer page favoris.");
                }
            });
        } else {
            Log.v("ERR Récupération", "Affichage impossible, la collection semble null");
        }
    }

    // onListeClick = quels actions faire lors d'un click d'un item du recycleView
    public void onListeClick(int index,String codeProduit) {
        moveToResultat(codeProduit);

    }
    // Check si un produit est dans la liste des favoris et se déplace dans le fragment resultatFragment avec l'information en plus
    public void moveToResultat(String codeProduit){
        Bdd.checkProduitCode(codeProduit,new Bdd.FavorisCallback(){
            @Override
            public void onConnexionFavorisSuccess() {
                Log.v("TEST","Check code reussi");
            }
            @Override
            public void onConnexionFavorisFailed(Exception e) {
                Log.v("TEST","Favoris ERR"+e);
                Log.v("Err", String.valueOf(e));
            }
            public void onConnexionFavorisSuccess(boolean favoris) {
                Log.v("TEST","Le code est bien dans Historique");
                estFavoris=favoris;

                resultatFragment = new ResultatScanFragment(codeProduit,estFavoris,true);
                ManageFragment.moveToFragment(requireActivity(),resultatFragment,R.id.containerView);
            }

        });
    }


}
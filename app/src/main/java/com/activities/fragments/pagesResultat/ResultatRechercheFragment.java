package com.activities.fragments.pagesResultat;

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
import android.widget.TextView;
import android.widget.Toast;

import com.activities.MainActivity;
import com.bdd.Bdd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;
import com.activities.adapers.RechercheRecycleViewAdapter;
import com.activities.adapers.interfaces.RecyclerViewInterface;
import com.activities.fragments.ManageFragment;
import com.model.produitRecherche.RechercheReponse;
import com.model.produitRecherche.Rproduit;
import com.network.OpenFoodFactsService;
import com.network.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultatRechercheFragment extends Fragment implements RecyclerViewInterface {
    private Boolean estFavoris,estConsomme;
    private TextView recap;
    private ResultatScanFragment resultatFragment;
    private String nomProduit;
    private RecyclerView rViewRecherche;
    private ArrayList <Rproduit> produits;

    private ProgressBar roueChargement;

    public ResultatRechercheFragment(String nomProduit){
        this.nomProduit=nomProduit;
        Log.v("USER",nomProduit);
    }

    public ResultatRechercheFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        produits=new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_resultat_recherche, container, false);

        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonRecherche).setChecked(true);

        rViewRecherche=view.findViewById(R.id.recyclerViewRecherche);
        roueChargement=view.findViewById(R.id.barChargment);
        roueChargement.setVisibility(View.VISIBLE);
        recap=view.findViewById(R.id.recapitulatif);
        recap.setText(nomProduit);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lanceApi();

    }

    public void lanceApi(){
        OpenFoodFactsService service = RetrofitClient.getService();
        String fields = "product_name,code,image_url";
        Call<RechercheReponse> call = service.searchProduct(nomProduit,1,"process",1,fields);
        ResultatRechercheFragment recyclerParent = this;

        call.enqueue(new Callback<RechercheReponse>() {
            @Override
            public void onResponse(@NonNull Call<RechercheReponse> call, @NonNull Response<RechercheReponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RechercheReponse rechercheReponse = response.body();
                    if (rechercheReponse.getProduits() != null) {
                        // flemme d'expliquer c'est assez clair nn ?
                        produits=rechercheReponse.getProduits();
                        Log.d("SEARCH", "YEssss ");
                        Log.d("SEARCH", produits.toString());

                        Log.d("SEARCH",rechercheReponse.toString());
                        RechercheRecycleViewAdapter adapter = new RechercheRecycleViewAdapter(getContext(),
                                produits, (RecyclerViewInterface) recyclerParent);
                        rViewRecherche.setAdapter(adapter);
                        rViewRecherche.setLayoutManager(new LinearLayoutManager(getContext()));


                    } else {
                        Toast.makeText(getContext(), "les produits sont vide dans la liste", Toast.LENGTH_SHORT).show();
                        Log.d("SEARCH", "les produits sont vide dans la liste ");
                    }

                }
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "la reponse n'est pas successful", Toast.LENGTH_SHORT).show();
                }if(response.body()==null){
                    Toast.makeText(getContext(), "La reponse a un body null", Toast.LENGTH_SHORT).show();
                }
                roueChargement.setVisibility(View.INVISIBLE);

            }


            @Override
            public void onFailure(@NonNull Call<RechercheReponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                roueChargement.setVisibility(View.INVISIBLE);
            }
        });

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
                Log.v("TEST","ResultatRechercheFragment ERR"+e);
            }
            @Override
            public void onConnexionFavorisSuccess(boolean favoris) {
                Log.v("TEST","Le code est bien dans Historique");
                estFavoris=favoris;
                resultatFragment = new ResultatScanFragment(codeProduit,estFavoris,true);
                ManageFragment.moveToFragment(requireActivity(),resultatFragment,R.id.containerView);
            }

        });
    }
}
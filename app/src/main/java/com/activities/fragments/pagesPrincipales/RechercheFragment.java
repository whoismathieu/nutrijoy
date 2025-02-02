


package com.activities.fragments.pagesPrincipales;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.activities.MainActivity;
import com.bdd.Bdd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;

// Import pour le scan

import com.activities.adapers.HistoriqueRecyclerViewAdapter;
import com.activities.adapers.interfaces.RecyclerViewInterface;
import com.activities.fragments.ManageFragment;
import com.activities.fragments.pagesResultat.ResultatRechercheFragment;
import com.activities.fragments.pagesResultat.ResultatScanFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

// Import pour l'appel de Open Food fact

import com.model.produitScanne.ProduitReponse;

import java.util.ArrayList;

import io.realm.mongodb.sync.Progress;


public class RechercheFragment extends Fragment implements RecyclerViewInterface {
    private Fragment resultatFragment;
    private Button scanner;
    // Déclaration de l'objet servant à récupérer les données du scan
    private ActivityResultLauncher<Intent> mStartForResult;

    private SearchView searchView;
    private ArrayList<ProduitReponse> historique;
    private RecyclerView rViewHistorique;
    private boolean estFavoris,scanne;

    private String codeBarre;
    private ProgressBar roueChargement;

    public RechercheFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Instanciation d'une liste produit historique (elle nous servira plus tard à emmagasiner les produits de notre bdd
        historique=new ArrayList<>();


        // Instanciation du ActivityResultLauncher

        mStartForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                    if (scanResult.getContents() == null) {
                        Toast.makeText(getActivity(), "Scan annulé", Toast.LENGTH_LONG).show();
                    } else {

                        // instanciation du fragment "ResultatScanFragment qui va contenir le resultat de l'appel de OFF en passant codebarre en parametre

                        codeBarre=scanResult.getContents();
                        scanne=true;
                        moveToResultat(codeBarre);
                    }
                });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recherche, container, false);

        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonRecherche).setChecked(true);

        // instanciation des différents composants par l'id
        scanner = view.findViewById(R.id.boutonScan);
        rViewHistorique=view.findViewById(R.id.recylerview_historique);
        searchView=view.findViewById(R.id.searchView);
        roueChargement=view.findViewById(R.id.roueChargementRecherche);
        roueChargement.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prépare et lance le scan
                scanbarreCode();

            }
        });
        affichage();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ManageFragment.moveToFragment(requireActivity(),new ResultatRechercheFragment(query),R.id.containerView);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // implementation du scan via la bibliotheque zxing( un peu modifié pour être compatible avec un fragment)
    public void scanbarreCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(requireActivity());
        // Cette ligne en dessous permet d'integrer le scanner sous une page qui est un fragment
        intentIntegrator.forSupportFragment(RechercheFragment.this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setPrompt("Scannez un code-barre");
        mStartForResult.launch(intentIntegrator.createScanIntent());

    }

    public void affichage(){
        if (Bdd.collection != null) {

            //Test appel historique seulement

            RechercheFragment recyclerParent = this;

            Bdd.recupererHistorique( new Bdd.ListeCallback() {
                @Override
                public void onSuccess(ArrayList<ProduitReponse> liste) {
                    roueChargement.setVisibility(View.GONE);

                    historique=liste;

                    HistoriqueRecyclerViewAdapter adapter=new HistoriqueRecyclerViewAdapter(getContext(),
                            historique,recyclerParent);
                    rViewHistorique.setAdapter(adapter);
                    rViewHistorique.setLayoutManager(new LinearLayoutManager(getContext()));
                    Log.v("USER", "Historique récupéré correctement.\n");
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(requireContext(),"Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    Log.v("USER", "Historique récupération utilisateur.");
                }
            });
        }
        else {
            Log.v("ERR Récupération", "Affichage impossible, la collection semble null");
        }

    }

    // onListeClick = quels actions faire lors d'un click d'un item du recycleView
    @Override
    public void onListeClick(int index,String codeProduit) {
        moveToResultat(codeProduit);
        scanne=false;

    }

    // Check si un produit est dans la liste des favoris et se déplace dans le fragment resultatFragment avec l'information en plus

    public void moveToResultat(String codeProduit){
        Bdd.checkProduitCode(codeProduit,new Bdd.FavorisCallback(){
            @Override
            public void onConnexionFavorisSuccess() {
                Log.v("TEST","Check code reussi");
            }
            @Override
            public void onConnexionFavorisSuccess(boolean favoris) {
                Log.v("TEST","Le code est bien dans Historique");
                estFavoris=favoris;

                resultatFragment = new ResultatScanFragment(codeProduit,estFavoris,scanne);
                ManageFragment.moveToFragment(requireActivity(),resultatFragment,R.id.containerView);
            }
            @Override
            public void onConnexionFavorisFailed(Exception e) {
                Log.v("ERR",""+e);
                Log.v("TEST","RECHERCHE ERR"+e);

            }
        });
    }



    // méthode permettant de se déplacer d'un fragment à un autre



}


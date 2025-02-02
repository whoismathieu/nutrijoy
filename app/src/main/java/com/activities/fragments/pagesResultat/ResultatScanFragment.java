package com.activities.fragments.pagesResultat;

import static android.graphics.Color.rgb;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activities.MainActivity;
import com.bdd.Bdd;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;
import com.activities.fragments.ManageFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.model.produitScanne.ProduitReponse;
import com.network.OpenFoodFactsService;
import com.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResultatScanFragment extends Fragment {

    //Déclaration des zones de textView qui afficheronts des caracteristiqus du produit
    private Fragment comparerFragment;
    TextView nomProduit;
    private ActivityResultLauncher<Intent> mStartForResult;

    ArrayList<PieEntry> nutriment =new ArrayList<>();

    // Déclaration des variables string (on peut faire sans mais rend le code plus digeste)
    String url, codeBarre1,codeBarre2,gradeText, nomProduitText,energieText, lienNutriscore;

  //  Déclaration de la zone d'image du produit

    private Button btnComparer;
    private ImageView img,imageNutriscore;
    private PieChart pieChart;
    Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);


    private double fibre, glucide, sucre, sel, matiereGrasse, proteine, lipide;
    private ImageButton btnFavoris,btnConsomme;

    private Boolean estFavoris,scanne=false;
    private ProduitReponse productResponse;

    private ProgressBar roueChargement;

    //Pour suppression dans consommé
    private Date timestamp;


    // Cration du constructeur ayant en parametre le resultat du scan (codebarre)
    public ResultatScanFragment(String scanResult,boolean estFavoris,boolean scanne){

        this.codeBarre1=scanResult;
        this.estFavoris=estFavoris;
        this.scanne=scanne;

    }
    public ResultatScanFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                    if (scanResult.getContents() == null) {
                        Toast.makeText(getActivity(), "Scan annulé", Toast.LENGTH_LONG).show();
                    } else {

                        // instanciation du fragment "ResultatScanFragment qui va contenir le resultat de l'appel de OFF en passant codebarre en parametre

                        codeBarre2=scanResult.getContents();
                        moveToComparer(productResponse,codeBarre2);
                    }
                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_resultat_scan, container, false);

        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonRecherche).setChecked(true);

        // Placement dans les bones zones xml des différents variables

        nomProduit=view.findViewById(R.id.nomProduit);
        img=view.findViewById(R.id.imageView);
        imageNutriscore=view.findViewById(R.id.imageNutriscore);
        btnComparer=view.findViewById(R.id.boutonComparer);
        btnConsomme=view.findViewById(R.id.btnConsomme);
        btnConsomme.setSelected(false);
        btnFavoris=view.findViewById(R.id.btnFavoris);
        btnFavoris.setSelected(estFavoris);
        roueChargement=view.findViewById(R.id.roueChargementProduit);
        roueChargement.setVisibility(View.VISIBLE);
        pieChart = view.findViewById(R.id.PieChart);

        timestamp=new Date();

        // lancement de la requete d'api

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFavoris.setOnClickListener(v -> {
            v.setSelected(!v.isSelected()); // Change l'état de sélection à chaque clic
            // Sauvegarde de l'état du bouton
            sauverEtatFavoris(v.isSelected());
        });
        btnConsomme.setOnClickListener(v -> {
            v.setSelected(!v.isSelected()); // Change l'état de sélection à chaque clic
            // Sauvegarde de l'état du bouton

            sauverEtatConsomme(v.isSelected());
        });
        lanceApi();


        btnComparer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanbarreCode();
            }
        });

    }



    private void sauverEtatFavoris(boolean isSelected) {
        if(isSelected){
             requireActivity().runOnUiThread(() -> Bdd.ajoutFavoris(productResponse.toDocumentandTimeStamp()));
            //getActivity().runOnUiThread(() -> Bdd.ajoutHistorique(Bdd.queryFilter,productResponse.toDocument()));
        }else{
             requireActivity().runOnUiThread(() -> Bdd.suppFavoris(productResponse.toDocument()));
            //getActivity().runOnUiThread(() -> Bdd.suppHistorique(Bdd.queryFilter,productResponse.toDocument()));
            Log.v("favoris","on devrait supprimer un element de la base de donnée ");
        }
    }
    private void sauverEtatConsomme(boolean isSelected) {
        if(isSelected){
            requireActivity().runOnUiThread(() -> Bdd.ajoutConsommes(productResponse.toDocument().append("timestamp",timestamp)));
            Log.v("favoris","ajout" +productResponse.toDocument().toString());

            //getActivity().runOnUiThread(() -> Bdd.ajoutHistorique(Bdd.queryFilter,productResponse.toDocument()));
        }else{
            requireActivity().runOnUiThread(() -> Bdd.suppConsommes(productResponse.toDocument().append("timestamp",timestamp)));
            //getActivity().runOnUiThread(() -> Bdd.suppHistorique(Bdd.queryFilter,productResponse.toDocument()));
            Log.v("favoris","on devrait supprimer un element de la base de donnée ");
            Log.v("favoris","retire"+productResponse.toDocument().toString());


        }
    }

    // Definition de la methode lancedApi
    public void lanceApi(){
        nutriment.clear();

        OpenFoodFactsService service = RetrofitClient.getService();

        String fields = "product_name,nutriments,image_url,nutriscore";

        Call<ProduitReponse> call = service.getProductByCodeBarre(codeBarre1, fields);

        call.enqueue(new Callback<ProduitReponse>() {
            @Override
            public void onResponse(@NonNull Call<ProduitReponse> call, @NonNull Response<ProduitReponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productResponse = response.body();
                    if (productResponse.getProduct() != null) {
                        Log.v("TESTAPI",productResponse.getProduct().getNomProduit());
                        // Accès aux données

                        // flemme d'expliquer c'est assez clair nn ?

                        url = productResponse.getProduct().getImageUrl();
                        Glide.with(requireContext()).load(url).into(img);

                        nomProduitText = productResponse.getProduct().getNomProduit();
                        energieText=String.valueOf((int )productResponse.getProduct().getNutriments().getEnergie());
                        nomProduit.setText(nomProduitText);
                        gradeText=productResponse.getProduct().getNutriScore().getAnnee().getGrade();
                        int lienIdNutriscore = getNutriscoreImage(gradeText);
                        Glide.with(requireContext()).load(lienIdNutriscore).into(imageNutriscore);



                        //Récupération des apports nutritionnelles

                        fibre = productResponse.getProduct().getNutriments().getFibre();
                        glucide = productResponse.getProduct().getNutriments().getGlucide();
                        proteine = productResponse.getProduct().getNutriments().getProteine();
                        sel = productResponse.getProduct().getNutriments().getSel();
                        lipide = productResponse.getProduct().getNutriments().getAcideGrasSature();
                        matiereGrasse = productResponse.getProduct().getNutriments().getMatiereGrasse();

                        sucre = productResponse.getProduct().getNutriments().getSucre();


                        //Création du diagramme en camembert pour la page produit si valeur different de 0

                        if ((int) matiereGrasse !=0){
                            nutriment.add(new PieEntry((float)matiereGrasse, "Gras"));
                        }
                        if ((int) glucide != 0) {
                            nutriment.add(new PieEntry((float) glucide, "Glucide"));
                        }
                        if ((int) sucre !=0) {
                            nutriment.add(new PieEntry((float) sucre, "Sucre"));
                        }
                        if ((int) lipide !=0){
                            nutriment.add(new PieEntry((float)lipide, "Lipide"));
                        }

                        if ((int) fibre !=0){
                            nutriment.add(new PieEntry((float) fibre, "Fibre"));
                        }
                        if ((int) proteine !=0) {
                            nutriment.add(new PieEntry((float) proteine, "Protéine"));
                        }

                        if (sel >=1) {
                            nutriment.add(new PieEntry((float) sel, "Sel"));
                        }



                        int[] customColors = new int[]{
                                rgb(230, 57, 70),  // Rouge vif
                                rgb(244, 162, 97),  // Orange sable
                                rgb(233, 196, 106) ,  // Jaune doré
                                rgb(29, 53, 87),  // Bleu marine
                                rgb(42, 157, 143),  // Vert d'eau

                                rgb(168, 218, 220),  // Bleu clair
                                rgb(69, 123, 157),  // Bleu moyen
                                rgb(241, 250, 238),  // Blanc cassé
                        };


                        PieDataSet pieDataSet = new PieDataSet(nutriment, "");
                        pieDataSet.setColors(customColors);
                        //pieDataSet.setDrawValues(false);  // Ne pas afficher les valeurs sur les segments

                        pieDataSet.setValueTextSize(15f);
                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setDrawEntryLabels(false);
                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.animateY(1000);

                        roueChargement.setVisibility(View.INVISIBLE);
                        pieChart.setVisibility(View.VISIBLE);

                        pieChart.invalidate();


                        pieChart.setTransparentCircleRadius(5);
                        pieChart.setDrawCenterText(true); // Activeee l'affichage du texte au centre
                        pieChart.setExtraOffsets(5, 10, 5, 5); // Ajoute des offsets autour du graphique pour donner plus d'espace

                        pieChart.setCenterText(energieText+"\nkcal");
                        pieChart.setHoleRadius(40f);
                        pieChart.setCenterTextColor(rgb(24, 36, 44)); // Définit la couleur du texte
                        pieChart.setCenterTextSize(18f); // Définit la taille du texte
                        pieChart.setCenterTextTypeface(typeface); // Définit la police du texte

                        Legend legend = pieChart.getLegend();
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setTextColor(getResources().getColor(R.color.grisClair2));
                        legend.setFormToTextSpace(1f);
                        legend.setXEntrySpace(10f);
                        legend.setTextSize(12f);
                        legend.setDrawInside(false);
                        legend.setEnabled(true);




                        Log.v("Produit",productResponse.toDocument().toString());

                        if(scanne) {
                            Bdd.ajoutHistorique(productResponse.toDocumentandTimeStamp());
                        }
                        Log.v("USER",productResponse.toDocument().toString());


                    } else {
                        Toast.makeText(getContext(), "Produit non trouvé dans la réponse", Toast.LENGTH_SHORT).show();
                    }



                }else{
                    Toast.makeText(getContext(), "Une erreur est survenue.\nVérifiez votre connexion internet.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProduitReponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void scanbarreCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(requireActivity());
        // Cette ligne en dessous permet d'integrer le scanner sous une page qui est un fragment
        intentIntegrator.forSupportFragment(ResultatScanFragment.this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setPrompt("Scannez un code-barre");
        mStartForResult.launch(intentIntegrator.createScanIntent());

    }

    public void moveToComparer(ProduitReponse p1,String barreCode2){
        comparerFragment=new ResultatComparerFragment(p1,barreCode2);
        ManageFragment.moveToFragment(requireActivity(),comparerFragment,R.id.containerView);

    }





    private int getNutriscoreImage(String gradeText) {
        switch (gradeText) {
            case "a":
                return R.drawable.nutriscore_a;
            case "b":
                return R.drawable.nutriscore_b;
            case "c":
                return R.drawable.nutriscore_c;
            case "d":
                return R.drawable.nutriscore_d;
            case "e":
                return R.drawable.nutriscore_e;
            default:
                return R.drawable.ic_launcher_foreground;
        }
    }


}


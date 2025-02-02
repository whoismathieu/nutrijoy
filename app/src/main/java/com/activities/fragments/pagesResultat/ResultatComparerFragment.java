package com.activities.fragments.pagesResultat;

import static android.graphics.Color.rgb;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.activities.MainActivity;
import com.bdd.Bdd;
import com.bumptech.glide.Glide;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nutrijoy.R;
import com.model.produitScanne.ProduitReponse;
import com.network.OpenFoodFactsService;
import com.network.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResultatComparerFragment extends Fragment {
    private String url,url2,nomProduitText,descriptionText,nomProduitText2,descriptionText2;
    private ImageView img1,img2;
    private double fibre, glucide, proteine, sel, lipide, matiereGrasse, sucre,
            fibre2, glucide2, proteine2, sel2, lipide2, matiereGrasse2, sucre2;
    private String codeBarre2;
    private ProduitReponse produitReponse1,produitReponse2;
    private TextView nomProduit1,nomProduit2;

    private BarChart barChart;


    /* On récupère via le constructeur le premier produit avec toutes ses informations
     et le code du second produit scanné (qui va servir pour l'appel de l'Api du second produit)
     */
    public ResultatComparerFragment(ProduitReponse p1, String codeBarre2){
        this.produitReponse1=p1;
        this.codeBarre2=codeBarre2;
    }

    public ResultatComparerFragment(){}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_comparer, container, false);

        BottomNavigationView navView=((MainActivity)getActivity()).getBottomNavigationView();
        navView.getMenu().findItem(R.id.menu_boutonRecherche).setChecked(true);

        img1=view.findViewById(R.id.imageView2);
        img2=view.findViewById(R.id.imageView3);
        nomProduit1=view.findViewById(R.id.nompProduitComparer1);
        nomProduit2=view.findViewById(R.id.nomProduitComparer2);
        barChart=view.findViewById(R.id.barcharComparer);

        // On lance l'api pour récupérer les informations du second produit
        lanceApi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    public void lanceApi(){
        OpenFoodFactsService service = RetrofitClient.getService();

        String fields = "product_name,nutriments,image_url,nutriscore";

        Call<ProduitReponse> call = service.getProductByCodeBarre(codeBarre2, fields);

        call.enqueue(new Callback<ProduitReponse>() {
            @Override
            public void onResponse(@NonNull Call<ProduitReponse> call, @NonNull Response<ProduitReponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produitReponse2 = response.body();
                    if (produitReponse2.getProduct() != null) {
                        // Accès aux données

                        /**
                         * Récupération des informations du second produit
                         *
                         (il est inutile récupérer ici les informations du produit1, il faudra les récupérer en dehors de lanceApi
                         car on a deja les informations du 1er produit contrairement au deuxieme )

                         */
                        url2 = produitReponse2.getProduct().getImageUrl();
                        Glide.with(requireContext()).load(url2).into(img2);

                        nomProduitText2 = produitReponse2.getProduct().getNomProduit();
                        nomProduit2.setText(nomProduitText2);
                        nomProduit2.setSingleLine(false);
                        descriptionText2 = produitReponse2.getProduct().toString();

                        fibre2 = produitReponse2.getProduct().getNutriments().getFibre();
                        glucide2 = produitReponse2.getProduct().getNutriments().getGlucide();
                        proteine2 = produitReponse2.getProduct().getNutriments().getProteine();
                        sel2 = produitReponse2.getProduct().getNutriments().getSel();
                        lipide2 = produitReponse2.getProduct().getNutriments().getAcideGrasSature();
                        matiereGrasse2 = produitReponse2.getProduct().getNutriments().getMatiereGrasse();
                        sucre2 = produitReponse2.getProduct().getNutriments().getSucre();

                        Bdd.ajoutHistorique(produitReponse2.toDocumentandTimeStamp());
                        Diagramme();

                    } else {
                        Toast.makeText(getContext(), "Produit non trouvé dans la réponse", Toast.LENGTH_SHORT).show();
                    }
                }
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "la reponse n'est pas successful", Toast.LENGTH_SHORT).show();
                }if(response.body()==null){
                    Toast.makeText(getContext(), "La reponse a un body null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProduitReponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }
    public void Diagramme(){
        url = produitReponse1.getProduct().getImageUrl();
        Glide.with(requireContext()).load(url).into(img1);
        nomProduitText = produitReponse1.getProduct().getNomProduit();
        nomProduit1.setText(nomProduitText);
        nomProduit1.setSingleLine(false);

        descriptionText = produitReponse1.getProduct().toString();

        fibre = produitReponse1.getProduct().getNutriments().getFibre();
        glucide = produitReponse1.getProduct().getNutriments().getGlucide();
        proteine = produitReponse1.getProduct().getNutriments().getProteine();
        sel = produitReponse1.getProduct().getNutriments().getSel();
        lipide = produitReponse1.getProduct().getNutriments().getAcideGrasSature();
        matiereGrasse = produitReponse1.getProduct().getNutriments().getMatiereGrasse();
        sucre = produitReponse1.getProduct().getNutriments().getSucre();

        // Création des listes qui vont contenir les info des produits
        ArrayList<BarEntry> produit1 = new ArrayList<>();
        ArrayList<BarEntry> produit2 = new ArrayList<>();

        // Ajout des infos produit 1
        produit1.add(new BarEntry(0f, (float)sucre));
        produit1.add(new BarEntry(1f, (float)lipide));
        produit1.add(new BarEntry(2f, (float)matiereGrasse));
        produit1.add(new BarEntry(3f, (float)glucide));
        produit1.add(new BarEntry(4f, (float)proteine));
        produit1.add(new BarEntry(5f, (float)sel));
        produit1.add(new BarEntry(6f, (float)fibre));

        // Ajout des infos produits 2
        produit2.add(new BarEntry(0f, (float)sucre2));
        produit2.add(new BarEntry(1f, (float)lipide2));
        produit2.add(new BarEntry(2f, (float)matiereGrasse2));
        produit2.add(new BarEntry(3f, (float)glucide2));
        produit2.add(new BarEntry(4f, (float)proteine2));
        produit2.add(new BarEntry(5f, (float)sel2));
        produit2.add(new BarEntry(6f, (float)fibre2));

        // Création d'une liste qui contients les labels des nutriments
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Sucre");
        labels.add("Lipide");
        labels.add("Matière grasse");
        labels.add("Glucide");
        labels.add("Protéine");
        labels.add("Sel");
        labels.add("FIbre");


        BarDataSet set1 = new BarDataSet(produit1,nomProduitText);
        set1.setColor(rgb(24, 36, 44));
        BarDataSet set2 = new BarDataSet(produit2,nomProduitText2);
        set1.setValueTextColor(Color.parseColor("#ff424242"));
        set2.setValueTextColor(Color.parseColor("#ff424242"));

        set2.setColor(rgb(12, 172, 116));

        float groupSpace = 0.4f; // Space between groups
        float barSpace = 0.06f; // Space between individual bars within a group
        float barWidth = 0.14f; // Width of each bar

        BarData barData = new BarData(set1, set2);

        barChart.setData(barData);

        int dataCount =  labels.size(); // Update with the actual size of your data list

        barData.setBarWidth(barWidth);
        barChart.setDragEnabled(true);
        barData.groupBars(0, groupSpace, barSpace);

        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.animateY(1000);

        barChart.setNoDataTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
        barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        // Set chart padding
        barChart.setExtraTopOffset(10f);
        barChart.setExtraBottomOffset(10f);
        barChart.setExtraLeftOffset(20f);
        barChart.setExtraRightOffset(10f);

        // Customize x-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // Display one label per data point
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(barData.getGroupWidth(groupSpace, barSpace) * labels.size());
        xAxis.setTextColor(getResources().getColor(R.color.grisClair2));


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(getResources().getColor(R.color.grisClair2));


        barChart.resetZoom();
        barChart.fitScreen();
        barChart.moveViewToX(0);
        barChart.invalidate();

        Legend legend= barChart.getLegend();
        legend.setTextColor(Color.parseColor("#ff424242"));
    }
}
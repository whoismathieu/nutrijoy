package com.activities.fragments.periodeStatistique;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.rgb;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdd.Bdd;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.model.produitScanne.Nutriments;
import com.nutrijoy.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class JourFragment extends Fragment {
    private TextView kcalTextView;

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
    public JourFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jour, container, false);
        kcalTextView=view.findViewById(R.id.textViewKcal);

        BarChart barChart;

        barChart = view.findViewById(R.id.barChartJour);

        ArrayList<BarEntry> calories = new ArrayList<>();

        Bdd.recupererStatsJour(new Bdd.StatsCallback() {
            @Override
            public void onSuccess(Nutriments stats) {


                calories.add(new BarEntry(1f, (float) stats.getMatiereGrasse()));
                calories.add(new BarEntry(2f, (float) stats.getGlucide()));
                calories.add(new BarEntry(3f, (float) stats.getSucre()));
                calories.add(new BarEntry(4f, (float) stats.getAcideGrasSature()));
                calories.add(new BarEntry(5f, (float) stats.getFibre()));
                calories.add(new BarEntry(6f, (float) stats.getProteine()));
                calories.add(new BarEntry(7f, (float) stats.getSel()));

                // Liste des labels axe des X
                final ArrayList<String> labels = new ArrayList<>();
                labels.add("");
                labels.add("Gras");
                labels.add("Glucide");
                labels.add("Sucre");
                labels.add("Lipide");
                labels.add("Fibre");
                labels.add("Protéine");
                labels.add("Sel");


                BarDataSet barDataSet = new BarDataSet(calories, "");
                barDataSet.setColors(customColors);//Couleur changeable
                barDataSet.setValueTextColor(Color.parseColor("#e0e0e0")); // Couleur plus foncée pour les labels des valeurs
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);
                float barWidth = 0.5f;


                barChart.setData(barData);
                barData.setBarWidth(barWidth);
                barChart.setDragEnabled(true);

                barChart.setDrawGridBackground(false);
                barChart.getDescription().setEnabled(false);
                barChart.getDescription().setText("Apport nutritionnel en Grammes");
                barChart.getDescription().setTextSize(16f);
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getAxisRight().setEnabled(false);
                barChart.setDoubleTapToZoomEnabled(false);
                barChart.animateY(400);

                barChart.setNoDataTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

                // Set chart padding
                barChart.setExtraTopOffset(10f);
                barChart.setExtraBottomOffset(10f);
                barChart.setExtraLeftOffset(20f);
                barChart.setExtraRightOffset(10f);


                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularityEnabled(true);
                xAxis.setCenterAxisLabels(false);
                xAxis.setTextColor(BLACK);
                xAxis.setTextSize(12f);
                xAxis.setGranularity(1f);
                xAxis.setAxisMinimum(0f);
                xAxis.setTextColor(getResources().getColor(R.color.grisClair2));


                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setAxisMinimum(0f);
                leftAxis.setDrawGridLines(true);
                leftAxis.setTextColor(getResources().getColor(R.color.grisClair2));


                //Description de la legende (Non actif actuellement mais possibilité de rajouté
                Legend legend = barChart.getLegend();/*
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setFormToTextSpace(1f);
                legend.setXEntrySpace(10f);
                legend.setTextSize(12f); // Vous pouvez ajuster la taille du texte ici
                legend.setDrawInside(false);*/
                legend.setEnabled(false); // Légende désactivé

                barChart.resetZoom();
                barChart.fitScreen();
                barChart.moveViewToX(0);
                barChart.invalidate();
                kcalTextView.setText("Sur cette journée, tu as consommé : "+stats.getEnergie()+" kcal");



            }
            @Override
            public void onFailure(Exception e) {

            }
        });

        return view;
    }
}
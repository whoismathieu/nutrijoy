package com.activities.fragments.periodeStatistique;

import static android.graphics.Color.rgb;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdd.Bdd;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.nutrijoy.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MoisFragment extends Fragment {
    int[] customColorsSemaineMois;

    public MoisFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customColorsSemaineMois = new int[30];
        for(int i = 0; i < 30; i++) {
            customColorsSemaineMois[i] = rgb(12, 172, 116);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mois, container, false);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String result = format.format(date);

        BarChart barChart;

        barChart = view.findViewById(R.id.barChartMois);

        ArrayList<BarEntry> calories = new ArrayList<>();

        Bdd.recupererStatsMois(new Bdd.StatsMoisSemaineCallback() {
            @Override
            public void onSuccess(ArrayList<Double> stats) {
                int y =  0;
                for(Double energie : stats){
                    calories.add(new BarEntry( (float) y,stats.get(y).floatValue()));
                    y++;
                }

                //int numDate=Integer.parseInt(result);
                //Liste comprenant les jours du mois
                final ArrayList<String> nombres = new ArrayList<>(30);
                for(int i=30;i>9;i--){
                    nombres.add("-"+i+"j");
                }
                for(int i=9;i>0;i--){
                    nombres.add("-"+i+"j");
                }


                // Liste des labels axe des X (L'ordre d'ajout dépends du jour actuel)
                ArrayList<String> labels = new ArrayList<>();
                /*labels.add(""); A rajouter si mauvais alignement des labels


                for(int i=numDate;i<30-numDate;i++){
                    labels.add(0,nombres.get(i));
                }
                for (int j = 0; j<numDate; j++){
                    labels.add(0,nombres.get(j));
                }*/


                BarDataSet barDataSet = new BarDataSet(calories, "");
                barDataSet.setColors(customColorsSemaineMois); //Couleur changeable
                barDataSet.setValueTextColor(Color.parseColor("#e0e0e0"));
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);

                float barWidth = 0.5f;

                barChart.setData(barData);
                barData.setBarWidth(barWidth);
                barChart.setDragEnabled(true);

                //Censé limiter la hauteur à la valeur max.
                barChart.getAxisRight().setAxisMaximum(Collections.max(stats).floatValue());

                barChart.setDrawGridBackground(false);
                barChart.getDescription().setEnabled(false);
                barChart.getDescription().setText("Somme des calories consommé durant la semaine");
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
                barChart.setExtraLeftOffset(05f);
                barChart.setExtraRightOffset(10f);


                // Customize x-axis labels
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(nombres));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularityEnabled(true);
                xAxis.setCenterAxisLabels(false);
                xAxis.setTextColor(getResources().getColor(R.color.grisClair2));
                xAxis.setTextSize(8f);
                xAxis.setGranularity(1f); // Display one label per data point
                xAxis.setAxisMinimum(0f);


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


            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        return view;
    }
}
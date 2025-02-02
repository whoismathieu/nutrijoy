package com.model.produitRecherche;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RechercheReponse {
    private int count;

    private int page;

    private int page_count;

    private int page_size;


    @SerializedName("products")
    private ArrayList<Rproduit> produits;


    public ArrayList<Rproduit> getProduits() {
        return produits;
    }


    @Override
    public String toString() {
        return "RechercheReponse{" +
                "count=" + count +
                ", page=" + page +
                ", page_count=" + page_count +
                ", page_size=" + page_size +
                ", produits=" + produits +
                '}';
    }
    public int getPage_count() {
        return page_count;
    }


    public int getCount() {
        return count;
    }
}

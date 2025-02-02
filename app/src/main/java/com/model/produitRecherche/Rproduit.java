package com.model.produitRecherche;

import com.google.gson.annotations.SerializedName;
public class Rproduit {

    private String code;
    @SerializedName("product_name")
    private String nomProduit;

    @SerializedName("image_url")
    private String imageUrl;

    public String getNomProduit() {
        return nomProduit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCode() {
        return code;
    }
    @Override
    public String toString() {
        return "Rproduit{" +
                "code='" + code + '\'' +
                ", nomProduit='" + nomProduit + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

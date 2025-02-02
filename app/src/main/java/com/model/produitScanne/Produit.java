package com.model.produitScanne;

import com.google.gson.annotations.SerializedName;

import org.bson.Document;

public class Produit {
    @SerializedName("product_name")
    private String nomProduit;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("nutriments")
    private Nutriments nutriments;

    @SerializedName("nutriscore")
    private Nutriscore nutriscore;

    public Produit(Document prodDoc){
        nomProduit=prodDoc.getString("product_name");
        imageUrl=prodDoc.getString("image_url");
        nutriments=new Nutriments((Document) prodDoc.get("nutriments"));
        nutriscore=new Nutriscore((Document) prodDoc.get("nutriscore"));
    }


    public Document toDocument(){
        return new Document("product_name",nomProduit).append("image_url",imageUrl).append("nutriments",nutriments.toDocument()).append("nutriscore",nutriscore.toDocument());
    }
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString(){
        return "Pour 100g voici ses valeurs nutritionels :\n"+
                nutriments.toString();

    }
    public String getNomProduit() {
        return nomProduit;
    }

    public Nutriments getNutriments() {
        return nutriments;
    }

    public Nutriscore getNutriScore(){return nutriscore;}

}

package com.model.produitScanne;

import com.google.gson.annotations.SerializedName;

import org.bson.Document;

public class Nutriments {
    @SerializedName("energy-kcal_100g")
    private double energie;
    @SerializedName("fat_100g")
    private double matiereGrasse;
    @SerializedName("saturated-fat_100g")
    private double acideGrasSature;
    @SerializedName("carbohydrates_100g")
    private double glucide;
    @SerializedName("sugars_100g")
    private double sucre;
    @SerializedName("fiber_100g")
    private double fibre;
    @SerializedName("proteins_100g")
    private double proteine;
    @SerializedName("salt_100g")
    private double sel;

    public Nutriments(Document nutriDoc){

        sucre = getDoubleValue(nutriDoc.get("sucre"));
        sel = getDoubleValue(nutriDoc.get("sel"));
        energie = getDoubleValue(nutriDoc.get("energie"));
        glucide = getDoubleValue(nutriDoc.get("glucide"));
        proteine = getDoubleValue(nutriDoc.get("proteine"));
        fibre = getDoubleValue(nutriDoc.get("fibre"));
        matiereGrasse = getDoubleValue(nutriDoc.get("matiereGrasse"));
        acideGrasSature = getDoubleValue(nutriDoc.get("acideGrasSature"));
    }
    private double getDoubleValue(Object valeur) {
        if (valeur instanceof Integer) {
            return ((Integer) valeur).doubleValue();
        } else if (valeur instanceof Double) {
            return (Double) valeur;
        } else {

            throw new IllegalArgumentException("La valeur n'est pas un nombre");
        }
    }

    public Double maximum(){
        double[] liste=new double[]{sucre,sel,energie,glucide,proteine,fibre,matiereGrasse,acideGrasSature};
        double maxi = liste[0];
        for (int i = 1; i < liste.length; i++) {
            if (liste[i] > maxi) {
                maxi = liste[i];
            }
        }
        return maxi;
    }
    public Document toDocument(){
        return new Document("sucre",sucre)
                .append("energie",energie)
                .append("sel",sel)
                .append("energie",energie)
                .append("glucide",glucide)
                .append("proteine",proteine)
                .append("fibre",fibre)
                .append("matiereGrasse",matiereGrasse)
                .append("acideGrasSature",acideGrasSature);

    }

    @Override
    public String toString(){

        return "Energie : "+energie+"kcal\n"+
                "Matières grasses : "+matiereGrasse+" g\n"+
                "dont acides gras saturés : "+acideGrasSature+"g\n"+
                "Glucides : "+glucide+" g\n"+
                "dont sucres : "+sucre+" g\n"+
                "Fibres : "+fibre+" g\n"+
                "Protéines : "+proteine+" g\n"+
                "Sel : "+sel;
    }

    public double getEnergie() {
        return energie;
    }

    public double getMatiereGrasse() {
        return matiereGrasse;
    }

    public double getAcideGrasSature() {
        return acideGrasSature;
    }

    public double getGlucide() {
        return glucide;
    }

    public double getSucre() {
        return sucre;
    }

    public double getFibre() {
        return fibre;
    }

    public double getProteine() {
        return proteine;
    }

    public double getSel() {
        return sel;
    }

}

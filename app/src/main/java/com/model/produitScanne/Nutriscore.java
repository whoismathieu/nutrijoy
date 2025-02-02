package com.model.produitScanne;

import com.google.gson.annotations.SerializedName;

import org.bson.Document;

public class Nutriscore {

    @SerializedName("2023")
    private Annee annee;

    public Annee getAnnee() {
        return annee;
    }

    public Nutriscore(Document nutridoc){
    annee=new Annee((Document) nutridoc.get("2023"));
}
public Document toDocument(){
    return new Document("2023",annee.toDocument());
}

}

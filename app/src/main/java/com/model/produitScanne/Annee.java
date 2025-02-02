package com.model.produitScanne;

import org.bson.Document;

public class Annee {
    private String grade;
    private int score;

    public String getGrade() {
        return grade;
    }
    public int getScore(){
        return score;
    }
    public Annee(Document nutriDoc){

        score = getIntegerValue(nutriDoc.get("score"));
        grade=nutriDoc.getString("grade");

    }


    private int getIntegerValue(Object valeur) {
        if (valeur instanceof Double) {
            return ((Double) valeur).intValue();
        } else if (valeur instanceof Integer) {
            return (Integer) valeur;
        } else {

            throw new IllegalArgumentException("La valeur n'est pas un nombre");
        }
    }

    public Document toDocument() {
        return new Document("score", score)
                .append("grade", grade);

    }
}

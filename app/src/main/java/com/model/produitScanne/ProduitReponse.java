package com.model.produitScanne;

import org.bson.Document;

import java.util.Date;

public class ProduitReponse {
    private String code;
    private Produit product;
    private Date timestamp;

    public ProduitReponse(Document prodRepDoc){
        code=prodRepDoc.getString("code");
        product=new Produit(prodRepDoc.get("product",Document.class));
        timestamp=prodRepDoc.get("timestamp",Date.class);
    }
    public Document toDocument(){
        return new Document("code",code)
                .append("product",product.toDocument());
                //Nouveau timestamp car nouveau push

    }

    public Document toDocumentandTimeStamp(){
        return toDocument().append("timestamp",new Date());
    }
    @Override
    public String toString(){
        return "code : "+code+"\n"+
                product.toString()+"\n";
    }
    public String getCode() {
        return code;
    }

    public Produit getProduct() {
        return product;
    }

    public Date getTimestamp(){return timestamp;}


}


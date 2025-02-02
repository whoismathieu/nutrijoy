package com.model;


import androidx.annotation.NonNull;

import com.model.produitScanne.ProduitReponse;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;


public class Utilisateur {
    private String id;
    private String username;
    private String email;
    private String password;



    private ArrayList <ProduitReponse> historique;
    private ArrayList <ProduitReponse> favoris;
    private ArrayList <ProduitReponse> consommes;



    public Utilisateur(Document userDoc){
        username=userDoc.getString("username");
        email=userDoc.getString("email");
        password=userDoc.getString("password");//On sauvegarde ?? -> Je pense que oui
        //car potentiel changement de MDP.
        historique=new ArrayList<ProduitReponse>();
        favoris=new ArrayList<ProduitReponse>();
        consommes=new ArrayList<ProduitReponse>();


        List<Document> tmp=userDoc.get("historique",List.class);

        for(Document doc : tmp){
            historique.add(new ProduitReponse(doc));
        }

        tmp=userDoc.get("favoris",List.class);

        for(Document doc : tmp){
            favoris.add(new ProduitReponse(doc));
        }

        tmp=userDoc.get("consommes",List.class);

        for(Document doc : tmp){
            consommes.add(new ProduitReponse(doc));
        }
        /* faire la meme chose pour produitFavoris et produitConsomme*/

    }

    //TODO: Crer une fonction insérant un Produit danns l'historique.

    /*Methode viable uniquement pour des test, il ne faut pas ingjecter tout les produits (de l'objet historique) dans l'historique(bdd), il ya aura trop de doublons + perte de temps  */
    private ArrayList<Document> historiqueToDoc(){
        ArrayList<Document> doc=new ArrayList<>();
        for (ProduitReponse prod : historique){
            doc.add(prod.toDocument());
        }
        return doc;
    }


    // Transforme les données de l'utilsateur en un gros Document
    public Document toDocument(){
        Document d=new Document("username",username)
                .append("password",password)
                .append("historique",historiqueToDoc())
                .append("produitFavoris",favoris)
                .append("produitConsomme",consommes);
        return d;
    }


    @NonNull
    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder("username : " + username + "\n").append("email : ").append(email).append("\n").append("password : ").append(password).append("\n");
        for (ProduitReponse produitReponse : historique){
            sb.append(produitReponse.toString());
        }
        return sb.toString();
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<ProduitReponse> getHistorique() {
        return historique;
    }

    public void setHistorique(ArrayList<ProduitReponse> historique) {
        this.historique = historique;
    }

    public ArrayList<ProduitReponse> getProduitConsomme() {
        return consommes;
    }

    public void setProduitConsomme(ArrayList<ProduitReponse> produitConsomme) {
        this.consommes = produitConsomme;
    }

    public ArrayList<ProduitReponse> getProduitFavoris() {
        return favoris;
    }

    public void setProduitFavoris(ArrayList<ProduitReponse> produitFavoris) {
        this.favoris = produitFavoris;
    }

}

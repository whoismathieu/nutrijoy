package com.bdd;

import static io.realm.Realm.getApplicationContext;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.model.produitScanne.Nutriments;
import com.model.produitScanne.ProduitReponse;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class Bdd {

    public static App app;
    public static User user;
    public static String appid = "nutrijoy-iflfr";
    public static MongoClient mongoClient;
    public static MongoDatabase database;
    public static MongoCollection collection;

    private static final String HISTORIQUE="historique";
    private static final String FAVORIS="favoris";
    private static final String CONSOMMES="consommes";
    public static FirebaseAuth firebaseApp;




    // Interface qui gère le succès ou l'erreur de la connexion
    public interface ConnexionCallback {

        void onConnexionSuccess();
        void onConnexionFailed(Exception e);

    }
    public App test(){
        return this.app;
    }
    public interface FavorisCallback {

        void onConnexionFavorisSuccess();
        void onConnexionFavorisFailed(Exception e);

        void onConnexionFavorisSuccess(boolean estfavoris);
    }
    public interface ListeCallback {
        void onSuccess(ArrayList<ProduitReponse> liste);
        void onFailure(Exception e);
    }

    public static void init(){
        app = new App(new AppConfiguration.Builder(appid).build());

        firebaseApp = FirebaseAuth.getInstance();

    }

    private static void initUser(){
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        database = mongoClient.getDatabase("NutrijoyDB");
        collection = database.getCollection("users");
    }



    public static void checkIfSavedUser(ConnexionCallback callback){

        if (firebaseApp.getCurrentUser()!=null){
            firebaseApp.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.v("CONNEXION","Connexion + token succès.");
                            app.loginAsync(Credentials.jwt(idToken),result -> {
                                if(result.isSuccess()){
                                    Log.v("CONNEXION","Connexion depuis accueil réussie");
                                    initUser();
                                    callback.onConnexionSuccess();
                                }else{
                                    callback.onConnexionFailed(result.getError());
                                }
                            });
                        } else {
                            Log.v("CONNEXION","Connexion + token échec.");
                            callback.onConnexionFailed(task.getException());
                        }
                    }
                });

        }else{
            callback.onConnexionFailed(new NullPointerException("Pas d'utilisateur connecté (null)."));
        }
    }

    public static void connexionJwt(final ConnexionCallback callback){
        if (firebaseApp.getCurrentUser()!=null){
            firebaseApp.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.v("CONNEXION","Connexion + token succès.");
                            app.loginAsync(Credentials.jwt(idToken),result -> {
                                if (result.isSuccess()){
                                    Bdd.initUser();
                                    callback.onConnexionSuccess();
                                }else {
                                    callback.onConnexionFailed(result.getError());
                                }
                            });
                        } else {
                            Log.v("CONNEXION","Connexion + token échec.");
                            callback.onConnexionFailed(task.getException());
                        }
                    }
                });

        }else{
            callback.onConnexionFailed(new NullPointerException("Pas d'utilisateur connecté (null)."));
        }

    }


    public static void connexionFirebase(String email, String password,ConnexionCallback callback){
        firebaseApp.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.v("CONNEXION","Connexion compte firebase : Succes.");
                    callback.onConnexionSuccess();
                }else{
                    Log.v("CONNEXION","Connexion compte firebase : échec -> "+task.getException());
                    callback.onConnexionFailed(task.getException());
                }
            }
        });
    }


    public static void creerCompteFirebase(String email, String password,ConnexionCallback callback){
        // Initialize Firebase Auth
        firebaseApp.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("CONNEXION", "Firebase createUserWithEmail:success");
                            callback.onConnexionSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("CONNEXION", "Firebase createUserWithEmail:failure", task.getException());
                            callback.onConnexionFailed(task.getException());
                        }
                    }
                });
    }

    public static void recupererListe(Document projection,String typeListe,ListeCallback callback){

        App.Callback<Document> callback2=new App.Callback<Document>() {

            @Override
            public void onResult(App.Result<Document> result) {
                if (result.isSuccess()){
                    Document d=result.get();
                    ArrayList<ProduitReponse> liste=new ArrayList<>();
                    ArrayList<Document> tmp=d.get(typeListe,ArrayList.class);

                    for (Document doc : tmp){
                        ProduitReponse prodRep=new ProduitReponse(doc);
                        liste.add(0,prodRep);
                    }

                    callback.onSuccess(liste);
                    Log.v("USER", typeListe+" : récupération liste réussie.\n");
                }else{
                    callback.onFailure(new NullPointerException("Utilisateur null"));
                    Log.v("USER", typeListe+" : récupération échouée.");
                }
            }
        };
        collection.find(new Document("userid", user.getId())).projection(projection).first().getAsync(callback2);
    }

    public static void recupererHistorique(ListeCallback callback){
        recupererListe(new Document(HISTORIQUE, 1),HISTORIQUE, callback);
    }

    public static void recupererFavoris(ListeCallback callback){
        recupererListe(new Document(FAVORIS, 1),FAVORIS, callback);
    }

    public static void recupererConsommes(ListeCallback callback){
        recupererListe(new Document(CONSOMMES, 1),CONSOMMES, callback);
    }


    // Supprime toutes les occurences du produit émis en paramètre, dans la l'historique de l'utilisateur

    public static void ajoutListe(String typeListe,Document produit){
        Document action=new Document("$push",new Document(typeListe,produit));
        collection.updateOne(new Document("userid",user.getId()),action).getAsync(result -> {
            if (result.isSuccess()){
                Log.v("DONNEE", "Donnée insérée correctement.\n");
            }else{
                Log.v("DONNEE", "Donnée mal insérée.");
            }
        });


    }

    public static void ajoutHistorique(Document produit){
        ajoutListe("historique",produit);
    }
    public static void ajoutFavoris(Document produit){
        ajoutListe("favoris",produit);
    }
    public static void ajoutConsommes(Document produit){
        ajoutListe("consommes",produit);
    }


    public static void suppListe(String typeListe,Document produit){
        Document action=new Document("$pull",new Document(typeListe,produit));
        collection.updateOne(new Document("userid",user.getId()),action).getAsync(result -> {
            if (result.isSuccess()){
                Log.v("DONNEE", "Donnée supprimé correctement.\n");
            }else{
                Log.v("DONNEE", "Donnée mal supprimé.");
            }
        });
    }

    public static void suppHistorique(Document produit){
        suppListe("historique",produit);
    }
    public static void suppFavoris(Document produit){
        suppListe("favoris",produit);
    }
    public static void suppConsommes(Document produit){
        suppListe("consommes",produit);
    }


    public interface SuppListeCallback{
        void onSuccess();
        void onFailure(Exception e);
    }
    public static void suppAllHistorique(SuppListeCallback callback){
        user.getFunctions().callFunctionAsync("viderHistorique", new ArrayList<>(), Document.class, new App.Callback<Document>() {
            @Override
            public void onResult(App.Result<Document> result) {
                if(result.isSuccess()){
                    Log.v("DONNEE","Succès : viderHistorique effectué.");
                    callback.onSuccess();
                }else{
                    Log.e("DONNEE","Erreur : viderHistorique échoué.");
                    callback.onFailure(result.getError());
                }
            }
        });
    }
    public static void suppAllConsomme(SuppListeCallback callback){
        user.getFunctions().callFunctionAsync("viderConsommes", new ArrayList<>(), Document.class, new App.Callback<Document>() {
            @Override
            public void onResult(App.Result<Document> result) {
                if(result.isSuccess()){
                    Log.v("DONNEE","Succès : viderConsommes effectué.");
                    callback.onSuccess();
                }else{
                    Log.e("DONNEE","Erreur : viderConsommes échoué.");
                    callback.onFailure(result.getError());
                }
            }
        });
    }
    public static void suppAllFavoris(SuppListeCallback callback){
        user.getFunctions().callFunctionAsync("viderFavoris", new ArrayList<>(), Document.class, new App.Callback<Document>() {
            @Override
            public void onResult(App.Result<Document> result) {
                if(result.isSuccess()){
                    Log.v("DONNEE","Succès : viderFavoris effectué.");
                    callback.onSuccess();
                }else{
                    Log.e("DONNEE","Erreur : viderFavoris échoué.");
                    callback.onFailure(result.getError());
                }
            }
        });
    }

    /**
     * Méthode static servant à vérifier si un code-barre est bien dans la liste de favoris d'un utilisateur
     * @param codeBarre le code barre
     * @param callback le callback va représenter la réponse de la méthode
     */
    public static void checkProduitCode(String codeBarre, FavorisCallback callback) {
        // C'est le filtre pour laa requête
        Document query = new Document("userid", user.getId())
                .append("favoris", new Document("$elemMatch", new Document("code", codeBarre)));

        // On va utiliser une  projection pour augmenter la rapidité de la requete
        collection.find(query)
                .projection(new Document("favoris.code", 1))
                .first()
                .getAsync(result -> {
                    if (result.isSuccess()) {
                        Document document = (Document) result.get();
                        boolean estFavoris = false;

                        if (document != null && document.getList("favoris", Document.class) != null && !document.getList("favoris", Document.class).isEmpty()) {
                            estFavoris = true;
                            Log.v("CHECK_CODE", "Le code produit existe dans les favoris.");
                        } else {
                            Log.v("CHECK_CODE", "Le code produit n'existe pas dans les favoris");
                        }

                        if (callback != null) callback.onConnexionFavorisSuccess(estFavoris);
                    } else {
                        Log.e("CHECK_CODE", "Erreur lors de la recherche dans la base de données", result.getError());
                        if (callback != null) callback.onConnexionFavorisFailed(new Exception("Erreur de base de données: " + result.getError().toString()));
                    }
                });
    }

    public interface StatsCallback{
        void onSuccess(Nutriments stats);
        void onFailure(Exception e);
    }

    public interface StatsMoisSemaineCallback{
        void onSuccess(ArrayList<Double> stats);
        void onFailure(Exception e);
    }

    public static void recupererStatsJour(StatsCallback callback){
        ArrayList<Long> offset=new ArrayList<>();
        offset.add(recupOffset());
        user.getFunctions().callFunctionAsync("getStatsJour", offset, Document.class, new App.Callback<Document>() {
            @Override
            public void onResult(App.Result<Document> result) {
                if(result.isSuccess()){
                    Document stats=result.get();
                    Nutriments nutristats=new Nutriments(stats);
                    Log.v("DONNEE","Succès : getStatsJour effectué. :");
                    callback.onSuccess(nutristats);
                }else{
                    Log.v("DONNEE","Erreur : getStatsJour échoué.");
                    callback.onFailure(result.getError());
                }
            }
        });
    }


    private static long recupOffset(){
        TimeZone timeZone = TimeZone.getDefault();

        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance(timeZone);

        // Get the current timezone offset in milliseconds
        return timeZone.getOffset(calendar.getTimeInMillis());
    }
    public static void recupererStatsSemaine(StatsMoisSemaineCallback callback){
        ArrayList<Long> offset =new ArrayList<Long>();
        offset.add(recupOffset());
        user.getFunctions().callFunctionAsync("getTabEnergieSemaine", offset , ArrayList.class, new App.Callback<ArrayList>() {
            @Override
            public void onResult(App.Result<ArrayList> result) {
                if(result.isSuccess()){
                    ArrayList<Double> stats=result.get();
                    Log.v("DONNEE","Succès : getTabEnergieSemaine effectué.");
                    callback.onSuccess(stats);
                }else{
                    Log.e("DONNEE","Erreur : getTabEnergieSemaine échoué.");
                    callback.onFailure(result.getError());
                }
            }
        });
    }

    public static void recupererStatsMois(StatsMoisSemaineCallback callback){
        ArrayList<Long> offset =new ArrayList<Long>();
        offset.add(recupOffset());
        user.getFunctions().callFunctionAsync("getTabEnergieMois", offset, ArrayList.class, new App.Callback<ArrayList>() {
            @Override
            public void onResult(App.Result<ArrayList> result) {
                if(result.isSuccess()){
                    ArrayList<Double> stats=result.get();
                    Log.v("DONNEE","Succès : getTabEnergieMois effectué.");
                    callback.onSuccess(stats);
                }else{
                    Log.e("DONNEE","Erreur : getTabEnergieMois échoué.");
                    callback.onFailure(result.getError());
                }
            }
        });
    }

    public static void recupererUsername(App.Callback<String> callback){
        user.getFunctions().callFunctionAsync("getUsername", new ArrayList<>(), String.class,callback);
    }
    public static void modifierUsername(String newUsername,App.Callback<Document> callback){
        ArrayList<String> tmpUsername=new ArrayList<String>();
        tmpUsername.add(newUsername);
        user.getFunctions().callFunctionAsync("setUsername", tmpUsername, Document.class, callback);
    }

    public static void envoiResetEmail(String email,OnCompleteListener<Void> listener){
        firebaseApp.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }
    public static void resetPassword(String code,String newPassword,OnCompleteListener<Void> callback){
        firebaseApp.confirmPasswordReset(code,newPassword).addOnCompleteListener(callback);
    }

    public static void deconnexion(){
        app.currentUser().removeAsync(result1 -> {
            if(result1.isSuccess()){
                Log.v("Deconnexion","app.currentUser().remove() réussi. : ");
            }else{
                Log.v("Deconnexion","app.currentUser().remove() échoué. : "+result1.getError().toString());
            }
        });
        app.currentUser().logOutAsync(result -> {
            if(result.isSuccess()){
                Log.v("Deconnexion","Deconnexion de l'user réussie.");
            }else{
                Log.v("Deconnexion","Deconnexion échouée.");
            }
        });
        FirebaseAuth.getInstance().signOut();
        user = null;
        mongoClient = null;
        database = null;
        collection = null;
    }

}

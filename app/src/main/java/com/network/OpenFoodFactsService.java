package com.network;

import com.model.produitScanne.ProduitReponse;
import com.model.produitRecherche.RechercheReponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface OpenFoodFactsService {
    @GET("api/v2/product/{codeBarre}")
    Call<ProduitReponse> getProductByCodeBarre(
            @Path("codeBarre") String codeBarre,
            @Query("fields") String fields
    );

    @GET("cgi/search.pl")
    Call<RechercheReponse> searchProduct(
            @Query("search_terms") String searchTerms,
            @Query("search_simple") int searchSimple,
            @Query("action") String action,
            @Query("json") int json,
            @Query("fields") String fields
    );
}

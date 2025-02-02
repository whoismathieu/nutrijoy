package com.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://world.openfoodfacts.org/";
    // Instance unique non initialisée
    private static OpenFoodFactsService service = null;

    // Méthode publique statique pour obtenir l'instance
    public static synchronized OpenFoodFactsService getService() {
        // Initialisation paresseuse de l'instance
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(OpenFoodFactsService.class);
        }
        return service;
    }
}

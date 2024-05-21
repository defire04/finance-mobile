package com.example.finance_mobile.domain;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KeycloakApiClient {
    private static Retrofit retrofit = null;

    private static final String baseKeycloakUrl = "http://192.168.4.5:9999";
//    private static final String baseKeycloakUrl = "http://176.36.34.106:9999";

    public static Retrofit getClient() {
        if (retrofit == null) {


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseKeycloakUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

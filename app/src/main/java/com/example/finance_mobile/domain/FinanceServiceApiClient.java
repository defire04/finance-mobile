package com.example.finance_mobile.domain;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FinanceServiceApiClient {
    private static Retrofit retrofit = null;

//    private static final String baseFinanceServiceUrl = "http://176.36.34.106:8080";
    private static final String baseFinanceServiceUrl = "http://192.168.4.5:8080";

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();
            retrofit = new Retrofit.Builder()

                    .baseUrl(baseFinanceServiceUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()

                    .baseUrl(baseFinanceServiceUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

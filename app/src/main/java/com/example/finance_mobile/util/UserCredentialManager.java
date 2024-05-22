package com.example.finance_mobile.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.finance_mobile.data.api.finance.auth.AuthService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.auth.SignInUserDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.CategoryDTO;
import com.example.finance_mobile.data.dto.finance.balance.currency.Currency;
import com.example.finance_mobile.data.dto.keycloak.token.TokenResponseDTO;
import com.example.finance_mobile.domain.FinanceServiceApiClient;
import com.google.gson.Gson;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserCredentialManager {
    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    public UserCredentialManager(Context context) {

        sharedPreferences = context.getSharedPreferences("auth", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void save(TokenResponseDTO tokenResponseDTO) {
        Retrofit retrofit = FinanceServiceApiClient.getClient();
        AuthService authService = retrofit.create(AuthService.class);
        editor.putString("access_token", "Bearer " + tokenResponseDTO.getAccessToken());
        editor.apply();



        authService.signIn("Bearer "+ tokenResponseDTO.getAccessToken()).enqueue(new Callback<ResponseModelSingle<SignInUserDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<SignInUserDTO>> call, @NonNull Response<ResponseModelSingle<SignInUserDTO>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<SignInUserDTO>> call, @NonNull Throwable t) {

            }
        });
    }

    public void saveCurrency(Currency currency){
        String currencyStr =  new Gson().toJson(currency);;

       editor.putString("currency", currencyStr);
       editor.apply();
    }

    public Currency getCurrency() {

        return  new Gson().fromJson(sharedPreferences.getString("currency", new Gson().toJson(Currency.USD)), Currency.class);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("access_token", "");
    }

}

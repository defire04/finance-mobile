package com.example.finance_mobile.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.finance_mobile.data.dto.keycloak.token.TokenResponseDTO;

import java.util.Optional;

public class UserCredentialManager {
    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    public UserCredentialManager(Context context) {

        sharedPreferences = context.getSharedPreferences("auth", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void save(TokenResponseDTO tokenResponseDTO) {
        editor.putString("access_token", "Bearer " + tokenResponseDTO.getAccessToken());
        editor.apply();
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    public String getToken() {

        return sharedPreferences.getString("access_token", "");
    }
}

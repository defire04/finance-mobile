package com.example.finance_mobile.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finance_mobile.MainActivity;
import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.keycloak.token.TokenResponseDTO;
import com.example.finance_mobile.data.api.keycloak.KeycloakService;
import com.example.finance_mobile.ui.register.RegisterActivity;
import com.example.finance_mobile.util.UserCredentialManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPassword);
        Button loginButton = findViewById(R.id.btnLogIn);
        TextView dontHaveAccount = findViewById(R.id.tvDontHaveAccount);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            performLogin(username, password);
        });

        dontHaveAccount.setOnClickListener(v -> navigateToRegisterActivity());
    }



    private void performLogin(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.4.5:9999")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KeycloakService service = retrofit.create(KeycloakService.class);

        Call<TokenResponseDTO> call = service.login("password", "finance-service", "LuEItTbBAyuSBfqhBK74UfdQDwzTHj05", username, password);
        call.enqueue(new Callback<TokenResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponseDTO> call, @NonNull Response<TokenResponseDTO> response) {
                if (response.isSuccessful()) {
                    TokenResponseDTO tokenResponse = response.body();

                    if (tokenResponse != null) {
                        saveTokens(tokenResponse);
                        navigateToMainActivity();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponseDTO> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTokens(TokenResponseDTO tokenResponseDTO) {
        new UserCredentialManager(this).save(tokenResponseDTO);
    }

    private void navigateToMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
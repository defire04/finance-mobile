package com.example.finance_mobile.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.api.finance.auth.AuthService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.auth.RegisterDTO;
import com.example.finance_mobile.domain.FinanceServiceApiClient;
import com.example.finance_mobile.ui.auth.LoginActivity;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editUsername = findViewById(R.id.edit_username);
        EditText editPassword = findViewById(R.id.edit_password);
        EditText editEmail = findViewById(R.id.edit_email);
        EditText editFirstName = findViewById(R.id.edit_first_name);
        EditText editLastName = findViewById(R.id.edit_last_name);
        Button buttonSignUp = findViewById(R.id.button_signup);
        TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        buttonSignUp.setOnClickListener(view -> {
            RegisterDTO registerData = new RegisterDTO();
            registerData.setUsername(editUsername.getText().toString());
            registerData.setPassword(editPassword.getText().toString());
            registerData.setEmail(editEmail.getText().toString());
            registerData.setFirstName(editFirstName.getText().toString());
            registerData.setLastName(editLastName.getText().toString());

            register(registerData);
        });

        alreadyHaveAccount.setOnClickListener(view -> navigateToLoginActivity());
    }


    private void register(RegisterDTO registerDTO) {
        Retrofit retrofit = FinanceServiceApiClient.getClient();
        AuthService authService = retrofit.create(AuthService.class);

        Call<ResponseModelSingle<RegisterDTO>> call = authService.register(registerDTO);
        call.enqueue(new Callback<ResponseModelSingle<RegisterDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<RegisterDTO>> call, @NonNull Response<ResponseModelSingle<RegisterDTO>> response) {
                if (response.isSuccessful()) {


                    ResponseModelSingle<RegisterDTO> responseModelSingle = response.body();

                    if (responseModelSingle != null && responseModelSingle.getData() != null) {
                        Toast.makeText(RegisterActivity.this, "You have successfully registered. Now you can log in.", Toast.LENGTH_LONG).show();
                        navigateToLoginActivity();
                    } else {

                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Gson gson = new Gson();
                    ResponseModelSingle<?> responseModel = null;
                    try {
                        assert response.errorBody() != null;
                        responseModel = gson.fromJson(response.errorBody().string(), ResponseModelSingle.class);
                        System.out.println(responseModel.getErrorMsg());
                        System.out.println(response.code());
                        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSS");
                        Toast.makeText(RegisterActivity.this, responseModel.getErrorMsg(), Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<RegisterDTO>> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
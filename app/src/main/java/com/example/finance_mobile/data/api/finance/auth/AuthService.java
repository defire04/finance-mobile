package com.example.finance_mobile.data.api.finance.auth;

import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.auth.RegisterDTO;
import com.example.finance_mobile.data.dto.finance.auth.SignInUserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/auth/v1/register")
    Call<ResponseModelSingle<RegisterDTO>> register(@Body RegisterDTO registerDTO);

    @GET("/auth/v1/sign-in")
    Call<ResponseModelSingle<SignInUserDTO>> signIn(@Header("Authorization") String token);
}
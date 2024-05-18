package com.example.finance_mobile.data.api.keycloak;

import com.example.finance_mobile.data.dto.keycloak.token.TokenResponseDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface KeycloakService {

    @FormUrlEncoded
    @POST("/realms/finance-service/protocol/openid-connect/token")
    Call<TokenResponseDTO> login(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/realms/finance-service-realm/protocol/openid-connect/token")
    Call<TokenResponseDTO> refreshToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("refresh_token") String refreshToken
    );
}
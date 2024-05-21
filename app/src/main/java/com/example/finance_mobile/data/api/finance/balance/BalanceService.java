package com.example.finance_mobile.data.api.finance.balance;

import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.balance.balance.BalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.RegisterBalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BalanceService {

    @POST("/balances")
    Call<ResponseModelSingle<BalanceDTO>> createBalance(
            @Header("Authorization") String token,
            @Body RegisterBalanceDTO balanceDTO
    );

    @GET("/balances")
    Call<ResponseModelSingle<BalanceDTO>> getBalance(
            @Header("Authorization") String token
    );

    @POST("/balances/transactions")
    Call<ResponseModelSingle<BalanceDTO>> createTransaction(
            @Header("Authorization") String token,
            @Body BalanceTransactionDTO balanceTransactionDTO
    );

    @GET("/balances/transactions")
    Call<ResponseModelSingle<List<BalanceTransactionDTO>>> getTransactions(
            @Header("Authorization") String token,
            @Query("from") Long from,
            @Query("till") Long till
    );

//    @POST("/balances/transfer/from-piggy-bank")
//    Call<ResponseModelSingle<BalanceDTO>> transferFromPiggyBank(
//            @Header("Authorization") String token,
//            @Body TransferDTO transferDTO
//    );
//
//    @POST("/balances/transfer/to-piggy-bank")
//    Call<ResponseModelSingle<BalanceDTO>>> transferToPiggyBank(
//            @Header("Authorization") String token,
//            @Body TransferDTO transferDTO
//    );
}

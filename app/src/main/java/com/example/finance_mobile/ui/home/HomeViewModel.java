package com.example.finance_mobile.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.finance_mobile.data.api.finance.balance.BalanceService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.balance.balance.BalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;
import com.example.finance_mobile.data.dto.finance.balance.currency.Currency;
import com.example.finance_mobile.domain.FinanceServiceApiClient;
import com.example.finance_mobile.util.UserCredentialManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeViewModel extends AndroidViewModel {

    private final String accessToken;

    private final BalanceService balanceService;

    private final Currency currency = Currency.USD;

    private boolean isBalanceHidden = false;

    private final MutableLiveData<BalanceDTO> balanceLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<BalanceTransactionDTO>> transactionsLiveData = new MutableLiveData<>();


    public HomeViewModel(@NonNull Application application) {
        super(application);

        Retrofit retrofit = FinanceServiceApiClient.getClient(getApplication());
        balanceService = retrofit.create(BalanceService.class);
        accessToken = new UserCredentialManager(getApplication()).getToken();
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isBalanceHidden() {
        return isBalanceHidden;
    }

    public void setBalanceHidden(boolean balanceHidden) {
        isBalanceHidden = balanceHidden;
    }

    public LiveData<BalanceDTO> getBalanceLiveData() {
        fetchBalance();
        return balanceLiveData;
    }

    public LiveData<List<BalanceTransactionDTO>> getTransactionsLiveData(Long from, Long till) {
        fetchGetTransactions(from, till);
        return transactionsLiveData;
    }

    public void fetchBalance() {

        balanceService.getBalance(accessToken).enqueue(new Callback<ResponseModelSingle<BalanceDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Response<ResponseModelSingle<BalanceDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BalanceDTO balance = response.body().getData();



                    balanceLiveData.postValue(balance);
                } else {
                    balanceLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Throwable t) {
                balanceLiveData.postValue(null);
            }
        });
    }

    public void fetchGetTransactions(Long from, Long till) {
        balanceService.getTransactions(accessToken, from, till).enqueue(new Callback<ResponseModelSingle<List<BalanceTransactionDTO>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<List<BalanceTransactionDTO>>> call, @NonNull Response<ResponseModelSingle<List<BalanceTransactionDTO>>> response) {

                System.out.println(response.code());
                System.out.println(response.toString());

                System.out.println("REEEEEEEEEEEEEEEEEEEEE");
                if (response.isSuccessful() && response.body() != null) {
                    List<BalanceTransactionDTO> transactionDTOS = response.body().getData();

                    Objects.requireNonNull(transactionDTOS).forEach(x -> System.out.println(x.getCategoryType() + " " + x.getAmount()));
                    System.out.println("dddddddddaaaaaaaaaaaa");
                    transactionsLiveData.postValue(transactionDTOS);
                } else {
                    transactionsLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<List<BalanceTransactionDTO>>> call, @NonNull Throwable t) {
                transactionsLiveData.postValue(null);

                t.printStackTrace();
            }
        });

    }
}
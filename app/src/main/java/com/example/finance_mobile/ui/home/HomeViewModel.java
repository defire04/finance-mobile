package com.example.finance_mobile.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.finance_mobile.data.api.finance.balance.BalanceService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.balance.balance.BalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.RegisterBalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;
import com.example.finance_mobile.data.dto.finance.balance.currency.Currency;
import com.example.finance_mobile.domain.FinanceServiceApiClient;
import com.example.finance_mobile.util.UserCredentialManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeViewModel extends AndroidViewModel {

    private final String accessToken;

    private final BalanceService balanceService;

    public Currency currency = Currency.USD;

    private boolean isBalanceHidden = false;

    private final MutableLiveData<BalanceDTO> balanceLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<BalanceTransactionDTO>> transactionsLiveData = new MutableLiveData<>();

    private FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


    public HomeViewModel(@NonNull Application application) {
        super(application);


        Retrofit retrofit = FinanceServiceApiClient.getClient(getApplication());
        balanceService = retrofit.create(BalanceService.class);
        accessToken = new UserCredentialManager(getApplication()).getToken();
        currency = new UserCredentialManager(getApplication()).getCurrency();


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        fetchGetTransactions(calendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
    }

    public Currency getCurrency() {
        return new UserCredentialManager(getApplication()).getCurrency();
    }

    public boolean isBalanceHidden() {
        return isBalanceHidden;
    }

    public void setBalanceHidden(boolean balanceHidden) {
        isBalanceHidden = balanceHidden;
    }

    public LiveData<BalanceDTO> getBalanceLiveData(FragmentManager fragmentManager) {
        fetchBalance(fragmentManager);
        return balanceLiveData;
    }


    public MutableLiveData<List<BalanceTransactionDTO>> getTransactionsLiveData() {
        return transactionsLiveData;
    }

    public void fetchBalance(FragmentManager fragmentManager) {

        balanceService.getBalance(accessToken).enqueue(new Callback<ResponseModelSingle<BalanceDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Response<ResponseModelSingle<BalanceDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BalanceDTO balance = response.body().getData();


                    balanceLiveData.postValue(balance);
                } else {

//                    fragmentManager

                    if (response.code() == 400) {
                        new RegisterBalanceDialog(HomeViewModel.this).show(fragmentManager, "");
                    }
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
                if (response.isSuccessful() && response.body() != null) {
                    List<BalanceTransactionDTO> transactionDTOS = response.body().getData();

                    transactionsLiveData.postValue(transactionDTOS);
                } else {
                    transactionsLiveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<List<BalanceTransactionDTO>>> call, @NonNull Throwable t) {
                transactionsLiveData.postValue(new ArrayList<>());

                t.printStackTrace();
            }
        });

    }


    public void registerBalance(Currency currency, String inputBalance, FragmentManager fragmentManager) {
        balanceService.createBalance(accessToken, new RegisterBalanceDTO(new BigDecimal(inputBalance), currency)).enqueue(new Callback<ResponseModelSingle<BalanceDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Response<ResponseModelSingle<BalanceDTO>> response) {
                fetchBalance(fragmentManager);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Throwable t) {
                fetchBalance(fragmentManager);
            }
        });
    }

    public void createTransaction(String amount, Long categoryId) {
        balanceService.createTransaction(accessToken, new BalanceTransactionDTO(new BigDecimal(amount), categoryId)).enqueue(new Callback<ResponseModelSingle<BalanceDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Response<ResponseModelSingle<BalanceDTO>> response) {

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                fetchGetTransactions(calendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
                fetchBalance(fragmentManager);

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<BalanceDTO>> call, @NonNull Throwable t) {

            }
        });
    }
}
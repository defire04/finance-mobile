package com.example.finance_mobile.ui.home.transaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.finance_mobile.data.api.finance.category.CategoryService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.balance.category.CategoryDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.example.finance_mobile.domain.FinanceServiceApiClient;
import com.example.finance_mobile.util.UserCredentialManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateTransactionViewModel extends AndroidViewModel {


    private final CategoryService categoryService;

    private String accessToken;
    private final MutableLiveData<List<CategoryDTO>> categories = new MutableLiveData<>();

    public CreateTransactionViewModel(@NonNull Application application) {
        super(application);

        Retrofit retrofit = FinanceServiceApiClient.getClient(getApplication());
        categoryService = retrofit.create(CategoryService.class);
        accessToken = new UserCredentialManager(getApplication()).getToken();
    }

    public MutableLiveData<List<CategoryDTO>> getCategories() {
        return categories;
    }

    public void downloadCategories(CategoryType categoryType) {

        System.out.println(categoryType.name());
        System.out.println("ddddddddddddddddddd");
        categoryService.getCategory(accessToken, categoryType.name()).enqueue(new Callback<ResponseModelSingle<List<CategoryDTO>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<List<CategoryDTO>>> call, @NonNull Response<ResponseModelSingle<List<CategoryDTO>>> response) {

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    categories.postValue(response.body().getData());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<List<CategoryDTO>>> call, @NonNull Throwable t) {

            }
        });
    }
}

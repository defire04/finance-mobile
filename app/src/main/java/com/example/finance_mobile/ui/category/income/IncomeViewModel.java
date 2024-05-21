package com.example.finance_mobile.ui.category.income;

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

public class IncomeViewModel extends AndroidViewModel {

    private final MutableLiveData<List<CategoryDTO>> categoriesList = new MutableLiveData<>();

    private final CategoryService categoryService;

    private final String accessToken;

    public IncomeViewModel(@NonNull Application application) {

        super(application);

        Retrofit retrofit = FinanceServiceApiClient.getClient(getApplication());
        categoryService = retrofit.create(CategoryService.class);

        accessToken = new UserCredentialManager(getApplication()).getToken();
        getCategories();
    }

    public MutableLiveData<String> getCategories() {
        MutableLiveData<String> result = new MutableLiveData<>();


        categoryService.getCategory(accessToken, CategoryType.INCOME.name()).enqueue(new Callback<ResponseModelSingle<List<CategoryDTO>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<List<CategoryDTO>>> call, @NonNull Response<ResponseModelSingle<List<CategoryDTO>>> response) {
                if (response.isSuccessful()) {

                    ResponseModelSingle<List<CategoryDTO>> data = response.body();
                    if (data != null && data.getData() != null) {
                        categoriesList.postValue(data.getData());
                        result.postValue("Success");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<List<CategoryDTO>>> call, @NonNull Throwable t) {
                result.postValue("Failure");
            }
        });

        return result;
    }


    public MutableLiveData<List<CategoryDTO>> getCategoriesList() {
        return categoriesList;
    }

    public void removeCategory(CategoryDTO category) {
        categoryService.delete(accessToken, category).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });

        getCategories();
    }
}

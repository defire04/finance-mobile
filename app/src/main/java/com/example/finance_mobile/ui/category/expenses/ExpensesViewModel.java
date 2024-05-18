package com.example.finance_mobile.ui.category.expenses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.finance_mobile.data.api.finance.category.CategoryService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.category.CategoryDTO;
import com.example.finance_mobile.data.dto.category.type.CategoryType;
import com.example.finance_mobile.domain.ApiClient;
import com.example.finance_mobile.util.UserCredentialManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExpensesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<CategoryDTO>> categoriesList = new MutableLiveData<>();

    private final CategoryService categoryService;

    private String accessToken;

    public ExpensesViewModel(@NonNull Application application) {

        super(application);

        Retrofit retrofit = ApiClient.getClient(getApplication());
        categoryService = retrofit.create(CategoryService.class);

        accessToken = new UserCredentialManager(getApplication()).getToken();
        getCategories();
    }

    public MutableLiveData<String> getCategories() {
        MutableLiveData<String> result = new MutableLiveData<>();


        categoryService.getCategory(accessToken, CategoryType.EXPENSE.name()).enqueue(new Callback<ResponseModelSingle<List<CategoryDTO>>>() {
            @Override
            public void onResponse(Call<ResponseModelSingle<List<CategoryDTO>>> call, Response<ResponseModelSingle<List<CategoryDTO>>> response) {
                if (response.isSuccessful()) {

                    ResponseModelSingle<List<CategoryDTO>> data = response.body();
                    if (data != null && data.getData() != null) {
                        categoriesList.postValue(data.getData());
                        result.postValue("Success");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModelSingle<List<CategoryDTO>>> call, Throwable t) {

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
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

        getCategories();
    }
}

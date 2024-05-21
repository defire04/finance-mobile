package com.example.finance_mobile.ui.category.add_category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.finance_mobile.data.api.finance.category.CategoryService;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.finance.balance.category.CategoryDTO;
import com.example.finance_mobile.domain.FinanceServiceApiClient;
import com.example.finance_mobile.util.UserCredentialManager;

import retrofit2.Call;
import retrofit2.Retrofit;

public class AddCategoryViewModel extends AndroidViewModel {


    private final String accessToken;

    private final CategoryService categoryService;

    public AddCategoryViewModel(@NonNull Application application) {
        super(application);

        Retrofit retrofit = FinanceServiceApiClient.getClient(getApplication());
        categoryService = retrofit.create(CategoryService.class);
        accessToken = new UserCredentialManager(getApplication()).getToken();
    }

    public Call<ResponseModelSingle<CategoryDTO>> createCategory(CategoryDTO categoryDTO) {

        return categoryService.createCategory(accessToken, categoryDTO);
    }

    public Call<ResponseModelSingle<CategoryDTO>> updateCategory(CategoryDTO categoryDTO) {
        return categoryService.updateCategory(accessToken, categoryDTO);
    }
}

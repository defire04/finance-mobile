package com.example.finance_mobile.data.api.finance.category;

import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.category.CategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CategoryService {

    @GET("/categories")
    Call<ResponseModelSingle<List<CategoryDTO>>> getCategory(@Header("Authorization") String token, @Query("category_type") String categoryType);

    @HTTP(method = "DELETE", path = "/categories", hasBody = true)
    Call<Void> delete(@Header("Authorization") String token, @Body CategoryDTO category);

    @POST("/categories")
    Call<ResponseModelSingle<CategoryDTO>> createCategory(@Header("Authorization") String token, @Body CategoryDTO categoryDTO);

    @PUT("/categories")
    Call<ResponseModelSingle<CategoryDTO>> updateCategory(@Header("Authorization") String token, @Body CategoryDTO categoryDTO);

}

package com.example.finance_mobile.data.dto.finance.balance.category;

import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.google.gson.annotations.SerializedName;


public class CategoryDTO {
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;


    @SerializedName("category_type")
    private CategoryType categoryType;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, CategoryType categoryType) {
        this.name = name;
        this.categoryType = categoryType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }



}

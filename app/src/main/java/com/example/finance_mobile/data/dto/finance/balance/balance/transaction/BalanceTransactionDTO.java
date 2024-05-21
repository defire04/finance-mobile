package com.example.finance_mobile.data.dto.finance.balance.balance.transaction;

import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


public class BalanceTransactionDTO {

    @SerializedName("id")
    private Long id;

    @SerializedName("balance_id")
    private Long balanceId;

    @SerializedName("category_id")
    private Long categoryId;

    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("transaction_date")
    private Long transactionDate;

    @SerializedName("category_type")
    private CategoryType categoryType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
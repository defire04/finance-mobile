package com.example.finance_mobile.data.dto.finance.balance;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class BalanceDTO {

    @SerializedName("id")
    private Long id;

    @SerializedName("amount")
    private BigDecimal amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

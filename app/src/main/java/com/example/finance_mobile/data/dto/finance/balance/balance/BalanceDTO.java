package com.example.finance_mobile.data.dto.finance.balance.balance;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class BalanceDTO {
    @SerializedName("id")
    private Long id;

    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("income_amount")
    private BigDecimal incomeAmount;

    @SerializedName("expense_amount")
    private BigDecimal expenseAmount;

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

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }
}

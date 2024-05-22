package com.example.finance_mobile.data.dto.finance.balance.balance;

import com.example.finance_mobile.data.dto.finance.balance.currency.Currency;

import java.math.BigDecimal;

public class RegisterBalanceDTO {

    private BigDecimal amount;

    private Currency currency;

    public RegisterBalanceDTO(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

package com.example.finance_mobile.data.dto.finance.balance.currency;

public enum Currency {

    EUR("EUR", "€"),
    UAH("UAH", "₴"),
    USD("USD", "$");

    private final String code;
    private final String symbol;

    Currency(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }
}

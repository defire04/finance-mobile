package com.example.finance_mobile.ui.home.transaction;

import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;

import java.util.List;

public class DailyTransactions {

    private String dateOfMonth;
    private String dayOfWeek;
    private String monthName;
    private String yearMonth;

    private List<BalanceTransactionDTO> incomeTransactions;
    private List<BalanceTransactionDTO> expenseTransactions;

    public List<BalanceTransactionDTO> getIncomeTransactions() {
        return incomeTransactions;
    }

    public void setIncomeTransactions(List<BalanceTransactionDTO> incomeTransactions) {
        this.incomeTransactions = incomeTransactions;
    }

    public List<BalanceTransactionDTO> getExpenseTransactions() {
        return expenseTransactions;
    }

    public void setExpenseTransactions(List<BalanceTransactionDTO> expenseTransactions) {
        this.expenseTransactions = expenseTransactions;
    }
}

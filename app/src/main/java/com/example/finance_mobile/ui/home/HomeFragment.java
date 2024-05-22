package com.example.finance_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.data.dto.finance.balance.balance.BalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.example.finance_mobile.databinding.FragmentHomeBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;

    private BalanceDTO balanceDTO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        viewModel.getBalanceLiveData().observe(getViewLifecycleOwner(), balanceDTO -> {
            if (balanceDTO != null) {
                updateBalanceUI(balanceDTO);
            }
        });


        binding.datePicker.setOnClickListener(v -> {
            showDateRangePicker();
        });

        binding.hideBalance.setOnClickListener(v -> {
            boolean isHidden = viewModel.isBalanceHidden();
            viewModel.setBalanceHidden(!isHidden);
            BalanceDTO balanceDTO = viewModel.getBalanceLiveData().getValue();
            if (balanceDTO != null) {
                updateBalanceUI(balanceDTO);
            }
        });

        bindRecycleView();

        return binding.getRoot();
    }

    private void updateBalanceUI(BalanceDTO balanceDTO) {
        if (viewModel.isBalanceHidden()) {
            int length = binding.tvBalance.getText().length();
            StringBuilder hiddenText = new StringBuilder();
            for (int i = 0; i < length; i++) {
                hiddenText.append('*');
            }
            binding.tvBalance.setText(viewModel.getCurrency().getSymbol() + hiddenText);
        } else {
            binding.tvBalance.setText(viewModel.getCurrency().getSymbol() + balanceDTO.getAmount().toString());
            binding.incomeTextView.setText(balanceDTO.getIncomeAmount().toString());
            binding.expensesTextView.setText(balanceDTO.getExpenseAmount().toString());
        }
    }

    private void showDateRangePicker() {
        // Получаем диапазон дат с помощью MaterialDatePicker
        Calendar calendar = Calendar.getInstance();
        Long start = calendar.getTimeInMillis();
        Long end = calendar.getTimeInMillis();

        MaterialDatePicker<Pair<Long, Long>> picker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Selected period")
                        .setSelection(new Pair<>(start, end))
                        .build();

        // Обработчик выбора даты
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> pair = (Pair<Long, Long>) selection;


                long startDate = pair.first;
                long endDate = pair.second;

                prepareData(startDate, endDate);
            }
        });

        picker.show(getParentFragmentManager(), picker.toString());
    }

    private void prepareData(long startDate, long endDate) {

        viewModel.fetchGetTransactions(startDate, endDate);

//        bindRecycleView();


    }

    private void bindRecycleView() {
        Map<String, DailyTransactions> dailyTransactionsMap = new HashMap<>();
        viewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), transactionsLiveData -> {

            if (transactionsLiveData != null) {


                transactionsLiveData.forEach(transaction -> {
                    Instant instant = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        instant = Instant.ofEpochMilli(transaction.getTransactionDate());
                    }
                    LocalDate transactionDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        transactionDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    }
                    String formattedDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formattedDate = transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }



                    DailyTransactions dailyTransactions = dailyTransactionsMap.get(formattedDate);
                    if (dailyTransactions == null) {
                        dailyTransactions = new DailyTransactions();
                        dailyTransactions.setIncomeTransactions(new ArrayList<>());
                        dailyTransactions.setExpenseTransactions(new ArrayList<>());
                        dailyTransactionsMap.put(formattedDate, dailyTransactions);
                    }

                    if (transaction.getCategoryType() == CategoryType.INCOME) {
                        dailyTransactions.getIncomeTransactions().add(transaction);
                    } else if (transaction.getCategoryType() == CategoryType.EXPENSE) {
                        dailyTransactions.getExpenseTransactions().add(transaction);
                    }

                });


            }


            binding.rvTransactions.setAdapter(new TransactionAdapter(dailyTransactionsMap));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class DailyTransactions {

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
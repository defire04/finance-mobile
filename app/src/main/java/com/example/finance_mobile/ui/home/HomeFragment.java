package com.example.finance_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.data.dto.finance.balance.balance.BalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.example.finance_mobile.databinding.FragmentHomeBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


        binding.rvTransactions.setAdapter(new TransactionAdapter());
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

                // Форматируем дату начала
                Instant startInstant = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startInstant = Instant.ofEpochMilli(startDate);
                }
                LocalDateTime startDateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startDateTime = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
                }
                String formattedStartDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formattedStartDate = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }

                // Форматируем дату конца
                Instant endInstant = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    endInstant = Instant.ofEpochMilli(endDate);
                }
                LocalDateTime endDateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    endDateTime = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault());
                }
                String formattedEndDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formattedEndDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }

                // Выводим информацию о выбранном периоде
                System.out.println("Выбранный период: " + "SSSSSSSSSSSSSSSSSSSSSSS");
                System.out.println("Начальная дата и время: " + formattedStartDate);
                System.out.println("Конечная дата и время: " + formattedEndDate);

                // Подготовка данных
                prepareData(startDate, endDate);
            }
        });

        picker.show(getParentFragmentManager(), picker.toString());
    }

    // Метод для подготовки данных
    private void prepareData(long startDate, long endDate) {
        LiveData<List<BalanceTransactionDTO>> transactionsLiveData = viewModel.fetchGetTransactions(startDate, endDate);

        System.out.println("DDDDDDDDDDDDDDDDDDDD");
        System.out.println(transactionsLiveData.getValue() != null );
        if (transactionsLiveData.getValue() != null) {
            Map<String, DailyTransactions> dailyTransactionsMap = new HashMap<>();

            transactionsLiveData.getValue().forEach(transaction -> {
                // Получаем дату из транзакции
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

                // Получаем или создаем объект DailyTransactions для текущей даты
                DailyTransactions dailyTransactions = dailyTransactionsMap.get(formattedDate);
                if (dailyTransactions == null) {
                    dailyTransactions = new DailyTransactions();
                    dailyTransactions.setIncomeTransactions(new ArrayList<>());
                    dailyTransactions.setExpenseTransactions(new ArrayList<>());
                    dailyTransactionsMap.put(formattedDate, dailyTransactions);
                }

                // Добавляем транзакцию в соответствующий список по типу категории
                if (transaction.getCategoryType() == CategoryType.INCOME) {
                    dailyTransactions.getIncomeTransactions().add(transaction);
                } else if (transaction.getCategoryType() == CategoryType.EXPENSE) {
                    dailyTransactions.getExpenseTransactions().add(transaction);
                }
            });



            // Выводим содержимое карты
            for (Map.Entry<String, DailyTransactions> entry : dailyTransactionsMap.entrySet()) {
                String date = entry.getKey();
                DailyTransactions dailyTransactions = entry.getValue();



                System.out.println("Дата: " + date);
                System.out.println("Доходы: ");
                for (BalanceTransactionDTO incomeTransaction : dailyTransactions.getIncomeTransactions()) {
                    System.out.println(incomeTransaction.getAmount());
                    // Другие поля, которые вы хотите вывести
                }
                System.out.println("Расходы: ");
                for (BalanceTransactionDTO expenseTransaction : dailyTransactions.getExpenseTransactions()) {
                    System.out.println(expenseTransaction.getAmount());
                    // Другие поля, которые вы хотите вывести
                }
                System.out.println("--------------------------------");
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

 class DailyTransactions {

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
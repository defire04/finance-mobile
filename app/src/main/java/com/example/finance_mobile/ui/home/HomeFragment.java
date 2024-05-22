package com.example.finance_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.balance.BalanceDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.example.finance_mobile.databinding.FragmentHomeBinding;
import com.example.finance_mobile.ui.home.transaction.CreateTransactionDialog;
import com.example.finance_mobile.ui.home.transaction.DailyTransactions;
import com.example.finance_mobile.ui.home.transaction.TransactionAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;

    private boolean isFabOpen = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setFragmentManager(getParentFragmentManager());
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        viewModel.getBalanceLiveData(getParentFragmentManager()).observe(getViewLifecycleOwner(), balanceDTO -> {
            if (balanceDTO != null) {
                updateBalanceUI(balanceDTO);
            }

        });

        binding.manageAccount.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigate_to_manage_account);
        });


        binding.datePicker.setOnClickListener(v -> {
            showDateRangePicker();
        });

        binding.hideBalance.setOnClickListener(v -> {
            boolean isHidden = viewModel.isBalanceHidden();
            viewModel.setBalanceHidden(!isHidden);
            BalanceDTO balanceDTO = viewModel.getBalanceLiveData(getParentFragmentManager()).getValue();

            if (balanceDTO != null) {
                updateBalanceUI(balanceDTO);
            }
        });

        bindRecycleView();

        binding.mainFab.setOnClickListener(view -> {
            if (isFabOpen) {
                closeFabMenu();
            } else {
                openFabMenu();
            }
        });


        return binding.getRoot();
    }

    private void openFabMenu() {
        isFabOpen = true;
        binding.fabIncome.show();
        binding.fabExpenses.show();
        binding.fabIncome.animate().translationY(-40);
        binding.fabExpenses.animate().translationY(-40);
        binding.tvIncome2.animate().translationY(-40);
        binding.tvExpeness2.animate().translationY(-40);

        binding.tvIncome2.setVisibility(View.VISIBLE);
        binding.tvExpeness2.setVisibility(View.VISIBLE);

        binding.mainFab.setImageDrawable(getContext().getDrawable(R.drawable.ic_plus_wo_trim));

        binding.fabIncome.setOnClickListener(v -> {
            new CreateTransactionDialog(viewModel, CategoryType.INCOME).show(getParentFragmentManager(), "");
            closeFabMenu();
        });

        binding.fabExpenses.setOnClickListener(v -> {
            new CreateTransactionDialog(viewModel, CategoryType.EXPENSE).show(getParentFragmentManager(), "");
            closeFabMenu();
        });

        binding.rootLayout.setVisibility(View.GONE);

    }

    private void closeFabMenu() {
        isFabOpen = false;
        binding.fabIncome.hide();
        binding.fabExpenses.hide();
        binding.fabIncome.animate().translationY(0);
        binding.fabExpenses.animate().translationY(0);
        binding.tvIncome2.animate().translationY(0);
        binding.tvExpeness2.animate().translationY(0);

        binding.rootLayout.setVisibility(View.VISIBLE);
        binding.tvIncome2.setVisibility(View.GONE);
        binding.tvExpeness2.setVisibility(View.GONE);

        binding.mainFab.setImageDrawable(getContext().getDrawable(android.R.drawable.ic_input_add));
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

                String startMonthYear = getMonthYearString(startDate);
                String endMonthYear = getMonthYearString(endDate);

                if (startMonthYear.equals(endMonthYear)) {
                    binding.textView8.setText(startMonthYear);
                } else {
                    binding.textView8.setText(startMonthYear + "  -  " + endMonthYear);
                }

                prepareData(startDate, endDate);
            }
        });

        picker.show(getParentFragmentManager(), picker.toString());
    }

    private String getMonthYearString(long dateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
        return monthFormat.format(calendar.getTime());
    }


    private void prepareData(long startDate, long endDate) {

        viewModel.fetchGetTransactions(startDate, endDate);

//        bindRecycleView();


    }

    private void bindRecycleView() {

        viewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), transactionsLiveData -> {

            if (transactionsLiveData != null) {
                Map<String, DailyTransactions> dailyTransactionsMap = new HashMap<>();

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

                binding.rvTransactions.setAdapter(new TransactionAdapter(dailyTransactionsMap));
            } else {
                binding.rvTransactions.setAdapter(new TransactionAdapter(new HashMap<>()));
            }


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


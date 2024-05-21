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
import com.example.finance_mobile.databinding.FragmentHomeBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.List;
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
        Calendar calendar = Calendar.getInstance();
        Long start = calendar.getTimeInMillis();
        Long end = calendar.getTimeInMillis();


        MaterialDatePicker<Pair<Long, Long>> picker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Selected period")
                        .setSelection(new Pair<>(start, end))
                        .build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> pair = (Pair<Long, Long>) selection;

                long startDate = pair.first;
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.setTimeInMillis(startDate);
                calendarStart.set(Calendar.HOUR_OF_DAY, 0);
                calendarStart.set(Calendar.MINUTE, 0);
                calendarStart.set(Calendar.SECOND, 0);
                calendarStart.set(Calendar.MILLISECOND, 0);

                long endDate = pair.second;
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTimeInMillis(endDate);
                calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarEnd.set(Calendar.MILLISECOND, 999);
                //FromTill fromTill = new FromTill(calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis());
                //viewModel.setSelectedDateLimit(fromTill);
                //binding.btnSetDataLimit.setText(fromTill.getTillFromString());
                //binding.removeDateLimit.setVisibility(View.VISIBLE);


                LiveData<List<BalanceTransactionDTO>> transactionsLiveData = viewModel.getTransactionsLiveData(pair.first, pair.second);

                System.out.println(transactionsLiveData.getValue() != null);
//                Objects.requireNonNull(transactionsLiveData.getValue()).forEach(x -> System.out.println(x.getCategoryType() + " " + x.getAmount()));

                System.out.println("ddddddddddddddddddddddddddd");
                System.out.println(pair.first);
                System.out.println(pair.second);
            }
        });

        picker.show(getParentFragmentManager(), picker.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
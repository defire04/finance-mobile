package com.example.finance_mobile.ui.category.expenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.R;
import com.example.finance_mobile.databinding.FragmentExpensesBinding;


public class ExpensesFragment extends Fragment {


    private FragmentExpensesBinding binding;

    private ExpensesViewModel expensesViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);
        if (getArguments() != null) {

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExpensesBinding.bind(inflater.inflate(R.layout.fragment_expenses, container, false));



        expensesViewModel.getCategoriesList().observe(getViewLifecycleOwner(), categoryDTOS -> {
            if (categoryDTOS != null) {
                binding.categoryExpensesList.setAdapter(new ExpensesAdapter(categoryDTOS, expensesViewModel, getParentFragmentManager()));
            } else {
                System.out.println("НИЧЕГО НЕ НАЙДЕНО");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        expensesViewModel.getCategories();
    }
}
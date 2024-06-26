package com.example.finance_mobile.ui.category.income;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.R;
import com.example.finance_mobile.databinding.FragmentIncomeBinding;


public class IncomeFragment extends Fragment {


    private FragmentIncomeBinding binding;

    private IncomeViewModel incomeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentIncomeBinding.bind(inflater.inflate(R.layout.fragment_income, container, false));



        incomeViewModel.getCategoriesList().observe(getViewLifecycleOwner(), categoryDTOS -> {
            if (categoryDTOS != null) {
                binding.categoryIncomeList.setAdapter(new IncomeAdapter(categoryDTOS, incomeViewModel, getParentFragmentManager()));
            } else {
                System.out.println("НИЧЕГО НЕ НАЙДЕНО");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        incomeViewModel.getCategories();

        System.out.println("ddddddddddddddddd");
    }


}
package com.example.finance_mobile.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.category.type.CategoryType;
import com.example.finance_mobile.databinding.FragmentCategoriesBinding;
import com.example.finance_mobile.ui.category.add_category.AddCategoryDialog;
import com.example.finance_mobile.ui.category.expenses.ExpensesFragment;
import com.example.finance_mobile.ui.category.income.IncomeFragment;

public class CategoryFragment extends Fragment {

    private FragmentCategoriesBinding binding;
    private CategoryViewModel categoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);


        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        binding.addCategory.setOnClickListener(v -> {
            int selectedItemId = binding.bottomNavigationView.getSelectedItemId();


            if (selectedItemId == R.id.category_income) {
                new AddCategoryDialog(CategoryType.INCOME, () -> changeFragment(new IncomeFragment())).show(getParentFragmentManager(), "Create INCOME");

            } else if (selectedItemId == R.id.category_expenses) {
                new AddCategoryDialog(CategoryType.EXPENSE, () -> changeFragment(new ExpensesFragment())).show(getParentFragmentManager(), "Create EXPENSE");
            }
        });
        bindNavigation();


        return binding.getRoot();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bindNavigation() {
        changeFragment(new IncomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.category_income) {

                changeFragment(new IncomeFragment());
            } else if (menuItem.getItemId() == R.id.category_expenses) {
                changeFragment(new ExpensesFragment());
            }

            return true;
        });
    }

    private void changeFragment(Fragment fragment) {


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragmentContainerView, fragment);

        fragmentManager.popBackStack();

        transaction.commit();
    }

}

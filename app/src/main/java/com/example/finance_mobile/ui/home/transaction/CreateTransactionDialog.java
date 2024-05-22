package com.example.finance_mobile.ui.home.transaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.category.CategoryDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;
import com.example.finance_mobile.databinding.CreateTransactionDialogBinding;
import com.example.finance_mobile.ui.home.CategorySpinnerAdapter;
import com.example.finance_mobile.ui.home.HomeViewModel;

public class CreateTransactionDialog extends DialogFragment {

    private CreateTransactionDialogBinding binding;

    private final HomeViewModel homeViewModel;

    private CreateTransactionViewModel viewModel;

    private CategoryType categoryType;

    public CreateTransactionDialog(HomeViewModel homeViewModel, CategoryType categoryType) {
        this.homeViewModel = homeViewModel;
        this.categoryType = categoryType;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreateTransactionViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.fab_square_light);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = CreateTransactionDialogBinding.inflate(inflater, container, false);

        binding.cancel.setOnClickListener(v -> dismiss());

        viewModel.downloadCategories(categoryType);


        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            assert categories != null;
            CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getContext(), categories);
            binding.category.setAdapter(adapter);

        });

        binding.save.setOnClickListener(v -> {
            if (binding.balanceInput.getText().toString().isEmpty()) {
                binding.balanceInput.setError("Amount should be not empty");
                return;
            }

            CategoryDTO categoryDTO = (CategoryDTO) binding.category.getSelectedItem();

            String inputBalance = binding.balanceInput.getText().toString();

            if (categoryDTO == null) {
                Toast.makeText(getContext(), "Not selected category", Toast.LENGTH_SHORT).show();
            }

            assert categoryDTO != null;
            homeViewModel.createTransaction(inputBalance, categoryDTO.getId());

            dismiss();

        });

        return binding.getRoot();
    }
}

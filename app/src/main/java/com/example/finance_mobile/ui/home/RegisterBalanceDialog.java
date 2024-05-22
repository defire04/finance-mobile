package com.example.finance_mobile.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.currency.Currency;
import com.example.finance_mobile.databinding.RegisterBalanceDialogBinding;
import com.example.finance_mobile.util.UserCredentialManager;

public class RegisterBalanceDialog extends DialogFragment {


    private RegisterBalanceDialogBinding binding;

    private final HomeViewModel homeViewModel;

    public RegisterBalanceDialog(HomeViewModel homeViewModel) {

        this.homeViewModel = homeViewModel;
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

        binding = RegisterBalanceDialogBinding.inflate(inflater, container, false);


        EnumAdapter<Currency> adapter = new EnumAdapter<>(getContext(), Currency.values());
        binding.currency.setAdapter(adapter);

        binding.save.setOnClickListener(v -> {
            if (binding.balanceInput.getText().toString().isEmpty()) {
                binding.balanceInput.setError("Balance should be not empty");
                return;
            }

            Currency currency = (Currency) binding.currency.getSelectedItem();
            String inputBalance = binding.balanceInput.getText().toString();


            new UserCredentialManager(getContext()).saveCurrency(currency);
            homeViewModel.registerBalance(currency, inputBalance, getParentFragmentManager());
            dismiss();

        });

        return binding.getRoot();
    }
}

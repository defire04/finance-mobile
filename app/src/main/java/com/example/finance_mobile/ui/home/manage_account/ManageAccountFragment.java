package com.example.finance_mobile.ui.home.manage_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.currency.Currency;
import com.example.finance_mobile.databinding.FragmentManageAccountBinding;
import com.example.finance_mobile.ui.home.EnumAdapter;
import com.example.finance_mobile.util.UserCredentialManager;


public class ManageAccountFragment extends Fragment {


    private FragmentManageAccountBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentManageAccountBinding.bind(inflater.inflate(R.layout.fragment_manage_account, container, false));

        EnumAdapter<Currency> adapter = new EnumAdapter<>(getContext(), Currency.values());
        binding.currency.setAdapter(adapter);

        binding.currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = (Currency) parent.getSelectedItem();

                new UserCredentialManager(getContext()).saveCurrency(currency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();

    }
}
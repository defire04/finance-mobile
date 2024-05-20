package com.example.finance_mobile.ui.category.add_category;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.ResponseModelSingle;
import com.example.finance_mobile.data.dto.category.CategoryDTO;
import com.example.finance_mobile.data.dto.category.type.CategoryType;
import com.example.finance_mobile.databinding.DialogAddCategoryBinding;
import com.example.finance_mobile.ui.category.OnDialogDismissListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryDialog extends DialogFragment {


    private DialogAddCategoryBinding binding;

    private AddCategoryViewModel addCategoryViewModel;

    private final OnDialogDismissListener onDismissListener;
    private CategoryType categoryType;
    private CategoryDTO category;


    public AddCategoryDialog(CategoryType categoryType, OnDialogDismissListener onDismissListener) {
        this.categoryType = categoryType;
        this.onDismissListener = onDismissListener;
    }


    public AddCategoryDialog(CategoryDTO category, OnDialogDismissListener onDialogDismissListener) {
        this.category = category;
        this.onDismissListener = onDialogDismissListener;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addCategoryViewModel = new ViewModelProvider(this).get(AddCategoryViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = DialogAddCategoryBinding.inflate(inflater, container, false);

        if (category != null) {
            binding.editTextText.setText(category.getName());
            categoryType = category.getCategoryType();
        }
        switch (categoryType) {
            case INCOME:
                binding.imageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_income));
                break;
            case EXPENSE:
                binding.imageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_expenses));
                break;
        }

        binding.cancelButton.setOnClickListener(v -> dismiss());
        binding.saveButton.setOnClickListener(v -> {
            String categoryName = binding.editTextText.getText().toString();

            if (TextUtils.isEmpty(categoryName)) {
                binding.editTextText.setError("Category name should be not empty");
                return;
            }

            if (category != null) {
                updateCategory(categoryName);

            } else if (categoryType != null) {
                createCategory(categoryName);
            } else {
                showToast("Try again");
                closeDialog();
            }


        });

        return binding.getRoot();
    }

    private void createCategory(String categoryName) {
        addCategoryViewModel.createCategory(new CategoryDTO(categoryName, categoryType)).enqueue(new Callback<ResponseModelSingle<CategoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<CategoryDTO>> call, @NonNull Response<ResponseModelSingle<CategoryDTO>> response) {


                if (response.isSuccessful()) {

                    showToast("Success add category");
                    closeDialog();
                } else {
                    showToast("Failed to add category");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<CategoryDTO>> call, @NonNull Throwable t) {
                showToast(t.getLocalizedMessage());
                closeDialog();
            }
        });
    }

    private void updateCategory(String newCategoryName) {

        category.setName(newCategoryName);
        addCategoryViewModel.updateCategory(category).enqueue(new Callback<ResponseModelSingle<CategoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelSingle<CategoryDTO>> call, @NonNull Response<ResponseModelSingle<CategoryDTO>> response) {
                if (response.isSuccessful()) {

                    showToast("Success add category");
                    closeDialog();
                } else {
                    showToast("Failed to add category");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelSingle<CategoryDTO>> call, @NonNull Throwable t) {
                showToast(t.getLocalizedMessage());
                closeDialog();
            }
        });

    }


    private void closeDialog() {
        dismiss();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }
}

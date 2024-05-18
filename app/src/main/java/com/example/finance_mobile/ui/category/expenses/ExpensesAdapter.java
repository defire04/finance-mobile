package com.example.finance_mobile.ui.category.expenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.category.CategoryDTO;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private final List<CategoryDTO> categories;

    private ExpensesViewModel expensesViewModel;

    public ExpensesAdapter(List<CategoryDTO> categories, ExpensesViewModel expensesViewModel) {
        this.expensesViewModel = expensesViewModel;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryDTO category = categories.get(position);
        holder.categoryName.setText(category.getName());


        holder.remove.setOnClickListener(v -> {
            expensesViewModel.removeCategory(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;

        private ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            remove = itemView.findViewById(R.id.remove);
            categoryName = itemView.findViewById(R.id.item);
        }
    }
}

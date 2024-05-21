package com.example.finance_mobile.ui.category.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.category.CategoryDTO;
import com.example.finance_mobile.ui.category.add_category.AddCategoryDialog;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private final List<CategoryDTO> categories;

    private Context context;
    private ExpensesViewModel expensesViewModel;

    private FragmentManager fragmentManager;

    public ExpensesAdapter(List<CategoryDTO> categories, ExpensesViewModel expensesViewModel,   FragmentManager fragmentManager) {
        this.expensesViewModel = expensesViewModel;
        this.categories = categories;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryDTO category = categories.get(position);
        holder.categoryName.setText(category.getName());

        holder.logo.setImageDrawable(context.getDrawable(R.drawable.ic_expenses));
        holder.remove.setOnClickListener(v -> {
            expensesViewModel.removeCategory(category);
        });

        holder.itemView.setOnClickListener(v -> {
            new AddCategoryDialog(category, () -> expensesViewModel.getCategories()).show(fragmentManager, "");
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;

        private ImageView remove;

        private ImageView logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            remove = itemView.findViewById(R.id.remove);
            categoryName = itemView.findViewById(R.id.item);
            logo = itemView.findViewById(R.id.logo);
        }
    }
}

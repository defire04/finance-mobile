package com.example.finance_mobile.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;
import com.example.finance_mobile.data.dto.finance.balance.category.type.CategoryType;

import java.util.List;

public class DayTransactionAdapter extends RecyclerView.Adapter<DayTransactionAdapter.ViewHolder> {


    private Context context;
    private List<BalanceTransactionDTO> dayTransactions;

    public DayTransactionAdapter(List<BalanceTransactionDTO> dayTransactions) {
        this.dayTransactions = dayTransactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_trancation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BalanceTransactionDTO balanceTransactionDTO = dayTransactions.get(position);

        holder.categoryName.setText(balanceTransactionDTO.getCategoryName());
        holder.amountOfDayTransaction.setText(String.valueOf(balanceTransactionDTO.getAmount()));


        if (balanceTransactionDTO.getCategoryType().equals(CategoryType.INCOME)) {
            holder.amountOfDayTransaction.setTextColor(context.getColor(R.color.income));
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_income));
        } else {
            holder.amountOfDayTransaction.setTextColor(context.getColor(R.color.expense));
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_expenses));
        }
    }

    @Override
    public int getItemCount() {
        return dayTransactions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName, amountOfDayTransaction;

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            amountOfDayTransaction = itemView.findViewById(R.id.amountOfDayTransaction);
            imageView = itemView.findViewById(R.id.logo);
        }
    }
}

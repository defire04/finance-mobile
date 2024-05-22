package com.example.finance_mobile.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance_mobile.R;

import java.util.HashSet;
import java.util.Set;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Set<Integer> expandedItems = new HashSet<>();
    private static final int SIMPLE = 1;
    private static final int EXPAND = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EXPAND) {
            return new ViewHolderExpanded(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.transaction_item_expanded, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SIMPLE){
            ViewHolder mHolder = (ViewHolder) holder;
            mHolder.date.setText(position + "2");
        }
        else if (getItemViewType(position)==EXPAND){
            ViewHolderExpanded mHolder = (ViewHolderExpanded) holder;

            mHolder.date.setText(position + "2");
        }
        holder.itemView.setOnClickListener(v -> {
            if (expandedItems.contains(position)) {
                expandedItems.remove(position);
            } else expandedItems.add(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, dayOfWeek, yearMonth, positiveBalance, negativeBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.dayOfTheMonth);
            dayOfWeek = itemView.findViewById(R.id.dayOfTheWeek);
            yearMonth = itemView.findViewById(R.id.tvMonthYear);
            positiveBalance = itemView.findViewById(R.id.tvIncome);
            negativeBalance = itemView.findViewById(R.id.tvExpenses);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (expandedItems.contains(position)) {
            return EXPAND;
        } else return SIMPLE;
    }

    public class ViewHolderExpanded extends RecyclerView.ViewHolder {
        TextView date, dayOfWeek, yearMonth, positiveBalance, negativeBalance;

        public ViewHolderExpanded(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.dayOfTheMonth);
            //todo find views
        }
    }
}

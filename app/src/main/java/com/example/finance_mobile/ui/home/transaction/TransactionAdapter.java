package com.example.finance_mobile.ui.home.transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.balance.transaction.BalanceTransactionDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Set<Integer> expandedItems = new HashSet<>();
    private static final int SIMPLE = 1;
    private static final int EXPAND = 2;

    private final Map<String, DailyTransactions> dailyTransactionsMap;

    private List<String> itemHeader = new ArrayList<>();

    public TransactionAdapter(Map<String, DailyTransactions> dailyTransactionsMap) {

        this.dailyTransactionsMap = dailyTransactionsMap;
        itemHeader = new ArrayList<>(dailyTransactionsMap.keySet());
    }


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

        DailyTransactions dailyTransactions = dailyTransactionsMap.get(itemHeader.get(position));

        Double income = 0.0;
        Double expenses = 0.0;

        assert dailyTransactions != null;
        income = dailyTransactions.getIncomeTransactions().stream().mapToDouble(transaction -> transaction.getAmount().doubleValue())
                .sum();

        expenses = dailyTransactions.getExpenseTransactions().stream().mapToDouble(transaction -> transaction.getAmount().doubleValue())
                .sum();


        Calendar calendar = Calendar.getInstance();

        if (!dailyTransactions.getIncomeTransactions().isEmpty()) {
            calendar.setTimeInMillis(dailyTransactions.getIncomeTransactions().get(0).getTransactionDate());
        } else {
            calendar.setTimeInMillis(dailyTransactions.getExpenseTransactions().get(0).getTransactionDate());
        }


        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(calendar.getTime());

        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dayOfWeekFormat.format(calendar.getTime());


        if (getItemViewType(position) == SIMPLE) {
            ViewHolder mHolder = (ViewHolder) holder;

            mHolder.date.setText(String.valueOf(dayOfMonth));
            mHolder.dayOfWeek.setText(dayOfWeek);
            mHolder.yearMonth.setText(monthName + ", " + String.valueOf(year));
            mHolder.positiveBalance.setText(String.valueOf(income));
            mHolder.negativeBalance.setText(String.valueOf(expenses));


        } else if (getItemViewType(position) == EXPAND) {
            ViewHolderExpanded mHolder = (ViewHolderExpanded) holder;

            mHolder.date.setText(String.valueOf(dayOfMonth));
            mHolder.dayOfWeek.setText(dayOfWeek);
            mHolder.yearMonth.setText(monthName + ", " + String.valueOf(year));
            mHolder.positiveBalance.setText(String.valueOf(income));
            mHolder.negativeBalance.setText(String.valueOf(expenses));

            List<BalanceTransactionDTO> balanceTransactionDTOS = dailyTransactions.getIncomeTransactions();

            balanceTransactionDTOS.addAll(dailyTransactions.getExpenseTransactions());

            mHolder.transaction.setAdapter(new DayTransactionAdapter(balanceTransactionDTOS));

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
        return itemHeader.size();
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

        RecyclerView transaction;

        public ViewHolderExpanded(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.dayOfTheMonth);
            transaction = itemView.findViewById(R.id.recyclerView);
            dayOfWeek = itemView.findViewById(R.id.dayOfTheWeek);
            yearMonth = itemView.findViewById(R.id.tvMonthYear);
            positiveBalance = itemView.findViewById(R.id.tvIncome);
            negativeBalance = itemView.findViewById(R.id.tvExpenses);


        }
    }


}

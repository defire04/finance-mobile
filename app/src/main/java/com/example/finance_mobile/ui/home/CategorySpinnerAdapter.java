package com.example.finance_mobile.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finance_mobile.R;
import com.example.finance_mobile.data.dto.finance.balance.category.CategoryDTO;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryDTO> {
    private Context context;
    private List<CategoryDTO> categories;

    public CategorySpinnerAdapter(Context context, List<CategoryDTO> categories) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView textView = view.findViewById(R.id.category_item);
        CategoryDTO category = categories.get(position);
        textView.setText(category.getName());

        return view;
    }
}

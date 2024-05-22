package com.example.finance_mobile.ui.home;

import android.content.Context;
import android.widget.ArrayAdapter;

public class EnumAdapter<T extends Enum<T>> extends ArrayAdapter<T> {
    public EnumAdapter(Context context, T[] values) {
        super(context, android.R.layout.simple_spinner_item, values);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
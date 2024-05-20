package com.example.finance_mobile;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class FinanceMobileApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
}

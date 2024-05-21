package com.example.finance_mobile.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends AndroidViewModel {
    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public String getBalance(){
        return "$ 19,440.00";
    }
}
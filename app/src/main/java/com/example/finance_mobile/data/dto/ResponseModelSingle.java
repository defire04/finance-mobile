package com.example.finance_mobile.data.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseModelSingle<T> {
    @SerializedName("data")
    private T data;

    @SerializedName("error_msg")
    private String errorMsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
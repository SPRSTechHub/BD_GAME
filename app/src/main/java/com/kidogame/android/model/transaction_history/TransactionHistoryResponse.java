package com.kidogame.android.model.transaction_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionHistoryResponse {
    @SerializedName("data")
    @Expose
    private List<Transactions> data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status2) {
        this.status = status2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public List<Transactions> getData() {
        return this.data;
    }

    public void setData(List<Transactions> data2) {
        this.data = data2;
    }
}

package com.kidogame.android.model.market_ratio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketRatioResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Ratio ratio;
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

    public Ratio getRatio() {
        return this.ratio;
    }

    public void setRatio(Ratio ratio2) {
        this.ratio = ratio2;
    }
}

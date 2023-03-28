package com.kidogame.android.model.addmoney_online;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineMoney {
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("payment_url")
    @Expose
    private String payment_url;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }
}

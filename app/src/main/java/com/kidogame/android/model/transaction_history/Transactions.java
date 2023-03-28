package com.kidogame.android.model.transaction_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transactions {
    @SerializedName("bal")
    @Expose
    private String bal;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("descp")
    @Expose
    private String descp;
    @SerializedName("tr_amnt")
    @Expose
    private String tr_amnt;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        this.date = date2;
    }

    public String getDescp() {
        return this.descp;
    }

    public void setDescp(String descp2) {
        this.descp = descp2;
    }

    public String getTr_amnt() {
        return this.tr_amnt;
    }

    public void setTr_amnt(String tr_amnt2) {
        this.tr_amnt = tr_amnt2;
    }

    public String getBal() {
        return this.bal;
    }

    public void setBal(String bal2) {
        this.bal = bal2;
    }
}

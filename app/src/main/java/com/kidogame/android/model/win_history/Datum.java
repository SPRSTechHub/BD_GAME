package com.kidogame.android.model.win_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("patti")
    @Expose
    private String patti;
    @SerializedName("digit")
    @Expose
    private String digit;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatti() {
        return patti;
    }

    public void setPatti(String patti) {
        this.patti = patti;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
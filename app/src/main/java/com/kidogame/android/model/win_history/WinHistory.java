package com.kidogame.android.model.win_history;

public class WinHistory {
    String date;
    Datum data;

    public WinHistory(String date, Datum data) {
        this.date = date;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }
}

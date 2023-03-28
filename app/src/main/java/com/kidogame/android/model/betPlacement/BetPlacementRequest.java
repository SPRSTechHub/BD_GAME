package com.kidogame.android.model.betPlacement;

public class BetPlacementRequest {
    String amount;
    String digit;

    public BetPlacementRequest(String digit2, String amount2) {
        this.digit = digit2;
        this.amount = amount2;
    }

    public String getDigit() {
        return this.digit;
    }

    public void setDigit(String digit2) {
        this.digit = digit2;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }
}

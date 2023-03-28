package com.kidogame.android.model.betPlacement;

public class BetPlacementJodiRequest {
    String amount;
    String digit1;
    String digit2;

    public BetPlacementJodiRequest(String digit12, String digit22, String amount2) {
        this.digit1 = digit12;
        this.digit2 = digit22;
        this.amount = amount2;
    }

    public String getDigit1() {
        return this.digit1;
    }

    public void setDigit1(String digit12) {
        this.digit1 = digit12;
    }

    public String getDigit2() {
        return this.digit2;
    }

    public void setDigit2(String digit22) {
        this.digit2 = digit22;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }
}

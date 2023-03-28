package com.kidogame.android.model.offer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("offer_link")
    @Expose
    private String offer_link;

    public String getOffer_link() {
        return offer_link;
    }

    public void setOffer_link(String offer_link) {
        this.offer_link = offer_link;
    }
}

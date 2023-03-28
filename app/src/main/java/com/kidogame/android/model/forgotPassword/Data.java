package com.kidogame.android.model.forgotPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("rtv_password")
    @Expose
    private String rtv_password;

    public String getRtv_password() {
        return this.rtv_password;
    }

    public void setRtv_password(String amount) {
        this.rtv_password = amount;
    }
}

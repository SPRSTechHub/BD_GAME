package com.kidogame.android.model.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kidogame.android.model.login_response.UserInfo;

public class ResultResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private String data;
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

    public String getResult() {
        return this.data;
    }

    public void setResult(String data) {
        this.data = data;
    }
}

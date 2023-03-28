package com.kidogame.android.model.game_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameCategoryResponse {
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status2) {
        this.status = status2;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message2) {
        this.message = message2;
    }
}

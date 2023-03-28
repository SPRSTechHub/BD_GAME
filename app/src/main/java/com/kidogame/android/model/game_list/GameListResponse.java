package com.kidogame.android.model.game_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GameListResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<GameList> results = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<GameList> getResults() {
        return this.results;
    }

    public void setResults(List<GameList> results2) {
        this.results = results2;
    }

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
}

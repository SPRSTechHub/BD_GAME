package com.kidogame.android.model.game_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Message {
    @SerializedName("results")
    @Expose
    private List<GameCategory> results = null;

    public List<GameCategory> getResults() {
        return this.results;
    }

    public void setResults(List<GameCategory> results2) {
        this.results = results2;
    }
}

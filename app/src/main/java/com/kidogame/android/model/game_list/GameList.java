package com.kidogame.android.model.game_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameList {
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("match_icon")
    @Expose
    private String match_icon;
    @SerializedName("cat_title")
    @Expose
    private String catTitle;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("game_title")
    @Expose
    private String gameTitle;
    @SerializedName("id")
    @Expose

    /* renamed from: id */
    private String f70id;
    @SerializedName("live")
    @Expose
    private String live;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("match_time")
    @Expose
    private String matchTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timesamp")
    @Expose
    private String timesamp;

    public String getLive() {
        return this.live;
    }

    public void setLive(String live2) {
        this.live = live2;
    }

    public String getId() {
        return this.f70id;
    }

    public void setId(String id) {
        this.f70id = id;
    }

    public String getMatchId() {
        return this.matchId;
    }

    public void setMatchId(String matchId2) {
        this.matchId = matchId2;
    }

    public String getMatchTime() {
        return this.matchTime;
    }

    public void setMatchTime(String matchTime2) {
        this.matchTime = matchTime2;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day2) {
        this.day = day2;
    }

    public String getGameTitle() {
        return this.gameTitle;
    }

    public void setGameTitle(String gameTitle2) {
        this.gameTitle = gameTitle2;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId2) {
        this.gameId = gameId2;
    }

    public String getCatTitle() {
        return this.catTitle;
    }

    public void setCatTitle(String catTitle2) {
        this.catTitle = catTitle2;
    }

    public String getCatId() {
        return this.catId;
    }

    public void setCatId(String catId2) {
        this.catId = catId2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy2) {
        this.createdBy = createdBy2;
    }

    public String getTimesamp() {
        return this.timesamp;
    }

    public void setTimesamp(String timesamp2) {
        this.timesamp = timesamp2;
    }

    public String getMatch_icon() {
        return match_icon;
    }

    public void setMatch_icon(String match_icon) {
        this.match_icon = match_icon;
    }

    public String getF70id() {
        return f70id;
    }

    public void setF70id(String f70id) {
        this.f70id = f70id;
    }
}

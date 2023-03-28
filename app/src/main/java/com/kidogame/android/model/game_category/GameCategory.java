package com.kidogame.android.model.game_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameCategory {
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_iurl")
    @Expose
    private String cat_iurl;
    @SerializedName("cat_title")
    @Expose
    private String catTitle;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("id")
    @Expose

    /* renamed from: id */
    private String f69id;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return this.f69id;
    }

    public void setId(String id) {
        this.f69id = id;
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

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt2) {
        this.createdAt = createdAt2;
    }


    public String getCat_iurl() {
        return cat_iurl;
    }

    public void setCat_iurl(String cat_iurl) {
        this.cat_iurl = cat_iurl;
    }

    public String getF69id() {
        return f69id;
    }

    public void setF69id(String f69id) {
        this.f69id = f69id;
    }
}

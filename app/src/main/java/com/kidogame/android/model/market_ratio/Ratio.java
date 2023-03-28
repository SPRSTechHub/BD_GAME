package com.kidogame.android.model.market_ratio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ratio {
    @SerializedName("cat_id")
    @Expose
    private String cat_id;
    @SerializedName("cp")
    @Expose

    /* renamed from: cp */
    private String f63cp;
    @SerializedName("dp")
    @Expose

    /* renamed from: dp */
    private String f64dp;
    @SerializedName("id")
    @Expose

    /* renamed from: id */
    private String f65id;
    @SerializedName("jodi")
    @Expose
    private String jodi;
    @SerializedName("sd")
    @Expose

    /* renamed from: sd */
    private String f66sd;
    @SerializedName("sp")
    @Expose

    /* renamed from: sp */
    private String f67sp;
    @SerializedName("tp")
    @Expose

    /* renamed from: tp */
    private String f68tp;
    @SerializedName("upload_by")
    @Expose
    private String upload_by;

    public String getId() {
        return this.f65id;
    }

    public void setId(String id) {
        this.f65id = id;
    }

    public String getCat_id() {
        return this.cat_id;
    }

    public void setCat_id(String cat_id2) {
        this.cat_id = cat_id2;
    }

    public String getSd() {
        return this.f66sd;
    }

    public void setSd(String sd) {
        this.f66sd = sd;
    }

    public String getSp() {
        return this.f67sp;
    }

    public void setSp(String sp) {
        this.f67sp = sp;
    }

    public String getDp() {
        return this.f64dp;
    }

    public void setDp(String dp) {
        this.f64dp = dp;
    }

    public String getTp() {
        return this.f68tp;
    }

    public void setTp(String tp) {
        this.f68tp = tp;
    }

    public String getCp() {
        return this.f63cp;
    }

    public void setCp(String cp) {
        this.f63cp = cp;
    }

    public String getJodi() {
        return this.jodi;
    }

    public void setJodi(String jodi2) {
        this.jodi = jodi2;
    }

    public String getUpload_by() {
        return this.upload_by;
    }

    public void setUpload_by(String upload_by2) {
        this.upload_by = upload_by2;
    }
}

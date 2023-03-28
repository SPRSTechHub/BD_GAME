package com.kidogame.android.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wallet {

@SerializedName("bal_amnt")
@Expose
private Integer balAmnt;

public Integer getBalAmnt() {
return balAmnt;
}

public void setBalAmnt(Integer balAmnt) {
this.balAmnt = balAmnt;
}

}
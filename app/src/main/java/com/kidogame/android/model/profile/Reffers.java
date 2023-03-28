package com.kidogame.android.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reffers {

@SerializedName("refer_id")
@Expose
private String referId;
@SerializedName("rf_amount")
@Expose
private String rfAmount;
@SerializedName("active_rfr")
@Expose
private Integer activeRfr;
@SerializedName("total_rfr")
@Expose
private Integer totalRfr;

public String getReferId() {
return referId;
}

public void setReferId(String referId) {
this.referId = referId;
}

public String getRfAmount() {
return rfAmount;
}

public void setRfAmount(String rfAmount) {
this.rfAmount = rfAmount;
}

public Integer getActiveRfr() {
return activeRfr;
}

public void setActiveRfr(Integer activeRfr) {
this.activeRfr = activeRfr;
}

public Integer getTotalRfr() {
return totalRfr;
}

public void setTotalRfr(Integer totalRfr) {
this.totalRfr = totalRfr;
}

}
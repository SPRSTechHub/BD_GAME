package com.kidogame.android.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("userid")
@Expose
private String userid;
@SerializedName("refer_id")
@Expose
private String referId;
@SerializedName("imgurl")
@Expose
private String imgurl;

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getUserid() {
return userid;
}

public void setUserid(String userid) {
this.userid = userid;
}

public String getReferId() {
return referId;
}

public void setReferId(String referId) {
this.referId = referId;
}

public String getImgurl() {
return imgurl;
}

public void setImgurl(String imgurl) {
this.imgurl = imgurl;
}

}
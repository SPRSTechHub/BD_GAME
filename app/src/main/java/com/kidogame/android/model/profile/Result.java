package com.kidogame.android.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

@SerializedName("profile")
@Expose
private Profile profile;
@SerializedName("reffers")
@Expose
private Reffers reffers;
@SerializedName("wallet")
@Expose
private Wallet wallet;

public Profile getProfile() {
return profile;
}

public void setProfile(Profile profile) {
this.profile = profile;
}

public Reffers getReffers() {
return reffers;
}

public void setReffers(Reffers reffers) {
this.reffers = reffers;
}

public Wallet getWallet() {
return wallet;
}

public void setWallet(Wallet wallet) {
this.wallet = wallet;
}

}
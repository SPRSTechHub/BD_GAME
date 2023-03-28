package com.kidogame.android.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;

public class Preference {
    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    public Preference(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.sharedPreferences = defaultSharedPreferences;
        this.editor = defaultSharedPreferences.edit();
    }

    public Integer getInt(String key) {
        return this.sharedPreferences.getInt(key, 0);
    }

    public void putInt(String key, Integer value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    public String getString(String key) {
        return this.sharedPreferences.getString(key, "");
    }

    public void putString(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public Boolean getBoolean(String key) {
        return this.sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public void remove(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }

    public void storeUserDetails(UserInfo result) {
        this.editor.putString("user_info", new Gson().toJson((Object) result));
        this.editor.apply();
    }

    public UserInfo getUserDetails() {
        if (!getString("user_info").isEmpty()) {
            return (UserInfo) new Gson().fromJson(getString("user_info"), UserInfo.class);
        }
        return null;
    }


    public void storeUserProfile(ProfileResponse result) {
        this.editor.putString("profile", new Gson().toJson((Object) result));
        this.editor.apply();
    }

    public ProfileResponse getUserProfile() {
        if (!getString("profile").isEmpty()) {
            return new Gson().fromJson(getString("profile"), ProfileResponse.class);
        }
        return null;
    }
}

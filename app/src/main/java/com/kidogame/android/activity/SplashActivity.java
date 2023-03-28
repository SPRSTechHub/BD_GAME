package com.kidogame.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;

public class SplashActivity extends AppCompatActivity {
    Preference preference;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_splash);
        this.preference = new Preference(this);
        new Handler().postDelayed(() -> {
            if (this.preference.getBoolean("isLogin")) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, 3000);
    }


}

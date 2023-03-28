package com.kidogame.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.MainGameAdapter;
import com.kidogame.android.databinding.ActivityMainBinding;
import com.kidogame.android.databinding.ActivitySettingsBinding;
import com.kidogame.android.model.game_category.GameCategoryResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivitySettingsBinding binding;
    KProgressHUD hud;
    MainGameAdapter mainGameAdapter;
    Preference preference;

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }

    private void getUserDetails() {
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getUserDetails(preference.getUserDetails().getMobile(), "get_user").enqueue(new Callback<ProfileResponse>() {
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    if(response.body().getStatus() == 0){
                        preference.storeUserProfile(response.body());
                        binding.tvWallet.setText("₹ " + response.body().getResult().getWallet().getBalAmnt());
                    }
                }
            }

            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }


    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivitySettingsBinding) DataBindingUtil.setContentView(this, R.layout.activity_settings);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        UserInfo userInfo = this.preference.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.wallet.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, AddWalletActivity.class);
            startActivity(intent);
        });

        binding.whatsapp.setOnClickListener(view -> {
            openWhatsApp("+918293117066","Hello Support Team");
        });

        binding.ReferAndEarn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, ReferAndEarnActivity.class);
            startActivity(intent);
        });

        binding.transactionHistory.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, TranscationHistoryActivity.class);
            startActivity(intent);
        });

        binding.logout.setOnClickListener(view -> {
            preference.clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });


        binding.bottomBar.btnHome.setOnClickListener(view -> {
            Intent newIntent = new Intent(SettingActivity.this,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });

        binding.bottomBar.btnReferAndEarn.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,ReferAndEarnActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });


        binding.bottomBar.btnResult.setOnClickListener(view -> {
            Intent newIntent = new Intent(SettingActivity.this,WebviewActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });

        binding.bottomBar.btnResult.setBackground(null);
        binding.bottomBar.btnReferAndEarn.setBackground(null);
        binding.bottomBar.btnSetting.setBackgroundColor(getResources().getColor(R.color.white_alpha));
        binding.bottomBar.btnHome.setBackground(null);

    }

    @SuppressLint("QueryPermissionsNeeded")
    private void openWhatsApp(String number, String message){

        try {
            String headerReceiver = "";// Replace with your message.
            String bodyMessageFormal = "";// Replace with your message.
            String whatsappContain = headerReceiver + bodyMessageFormal;
            String trimToNumner = number; //10 digit number
            Intent intent = new Intent ( Intent.ACTION_VIEW );
            intent.setData ( Uri.parse ( "https://wa.me/" + trimToNumner + "/?text=" + message ) );
            startActivity ( intent );
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

}

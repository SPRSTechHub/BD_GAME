package com.kidogame.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.MainGameAdapter;
import com.kidogame.android.databinding.ActivityResultBinding;
import com.kidogame.android.model.login_response.LoginResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;
import com.kidogame.android.model.result.ResultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebviewActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivityResultBinding binding;
    KProgressHUD hud;
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
        this.binding = (ActivityResultBinding) DataBindingUtil.setContentView(this, R.layout.activity_result);
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

        binding.bottomBar.btnHome.setOnClickListener(view -> {
            Intent newIntent = new Intent(WebviewActivity.this,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });

        binding.bottomBar.btnSetting.setOnClickListener(view -> {
            Intent newIntent = new Intent(WebviewActivity.this,SettingActivity.class);
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

        binding.bottomBar.btnResult.setBackgroundColor(getResources().getColor(R.color.white_alpha));
        binding.bottomBar.btnReferAndEarn.setBackground(null);
        binding.bottomBar.btnSetting.setBackground(null);
        binding.bottomBar.btnHome.setBackground(null);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
            }
            @Override
            public void onPageFinished(WebView view, String url) {


            }
        });
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setAllowContentAccess(true);

        getResult();

    }


    private void getResult() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getWinResult(preference.getUserDetails().getMobile(), "win_result_all").enqueue(new Callback<ResultResponse>() {
            public void onResponse(@NonNull Call<ResultResponse> call, @NonNull Response<ResultResponse> response) {
                hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        binding.webView.loadUrl(response.body().getResult());

                    }
                    Toast.makeText(WebviewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<ResultResponse> call, Throwable t) {
                hud.dismiss();
            }
        });
    }
}

package com.kidogame.android.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.databinding.ActivityReferAndEarnBinding;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferAndEarnActivity extends Activity {
    ApiInterface apiService;
    ActivityReferAndEarnBinding binding;
    KProgressHUD hud;
    Preference preference;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_refer_and_earn);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        UserInfo userInfo = this.preference.getUserDetails();
        if (preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null) {
            binding.tvWallet.setText("₹ " + preference.getUserProfile().getResult().getWallet().getBalAmnt());
        } else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));

        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));

        });

        binding.btnReferAndEarn.setOnClickListener(view -> {
            /*Create an ACTION_SEND Intent*/
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BD Fatafat");
                String shareMessage = "\nGet Up to Rs.50 Refer your friend\n\n";
                shareMessage = shareMessage + "Download Link :- https://bdfatafat.live" + "\n\n" + "Refer Code : " + binding.tvReferCode.getText().toString().trim();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });


        binding.bottomBar.btnSetting.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, SettingActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0, 0);
            finish();
        });

        binding.bottomBar.btnHome.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0, 0);
            finish();
        });


        binding.bottomBar.btnResult.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, WebviewActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0, 0);
            finish();
        });

        binding.btnCopy.setOnClickListener(view -> {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", binding.tvReferCode.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
        });
        binding.bottomBar.btnResult.setBackground(null);
        binding.bottomBar.btnReferAndEarn.setBackgroundColor(getResources().getColor(R.color.white_alpha));
        binding.bottomBar.btnSetting.setBackground(null);
        binding.bottomBar.btnHome.setBackground(null);

    }

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
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus() == 0) {
                        preference.storeUserProfile(response.body());
                        binding.tvWallet.setText("₹ " + response.body().getResult().getWallet().getBalAmnt());
                        if (response.body().getResult().getReffers() != null) {
                            binding.tvReferAmount.setText("₹ " + response.body().getResult().getReffers().getRfAmount());
                            binding.tvTotalReferredCount.setText("" + response.body().getResult().getReffers().getTotalRfr());
                            binding.tvActiveReferCount.setText("" + response.body().getResult().getReffers().getActiveRfr());
                            binding.tvReferCode.setText("" + response.body().getResult().getReffers().getReferId());

                        }
                    }
                }
            }

            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
}
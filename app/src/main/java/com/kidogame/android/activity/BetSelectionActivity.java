package com.kidogame.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.databinding.ActivityBetSelectionBinding;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BetSelectionActivity extends AppCompatActivity {
    ActivityBetSelectionBinding binding;
    Preference preference;
    ApiInterface apiService;
    KProgressHUD hud;

    @Override
    protected void onResume() {
        super.onResume();
        //getUserDetails();
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
        this.binding = (ActivityBetSelectionBinding) DataBindingUtil.setContentView(this, R.layout.activity_bet_selection);
        Preference preference2 = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.preference = preference2;
        UserInfo userInfo = preference2.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }
        this.binding.btnBack.setOnClickListener(view -> {
            if (this.binding.btnSinglePanna.getVisibility() == View.VISIBLE) {
                this.binding.btnSinglePanna.setVisibility(View.GONE);
                this.binding.btnDoublePanna.setVisibility(View.GONE);
                this.binding.btnTripplePanna.setVisibility(View.GONE);
                this.binding.btnSingle.setVisibility(View.VISIBLE);
                this.binding.btnJodi.setVisibility(View.VISIBLE);
                this.binding.btnPatti.setVisibility(View.VISIBLE);
                this.binding.btnCP.setVisibility(View.VISIBLE);
                return;
            }
            finish();
        });
        if (getIntent().getStringExtra("id2").isEmpty()) {
            this.binding.btnJodi.setEnabled(false);
            this.binding.btnJodi.setAlpha(0.5f);
        }
        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });

        this.binding.btnSingle.setOnClickListener(view -> {
            Intent i = new Intent(this, BetPlacementActivity.class);
            i.putExtra("type", "single");
            i.putExtra("id", getIntent().getStringExtra("id"));
            i.putExtra("time",getIntent().getStringExtra("time"));
            startActivity(i);
        });

        this.binding.btnPatti.setOnClickListener(view -> {
            this.binding.btnSinglePanna.setVisibility(View.VISIBLE);
            this.binding.btnDoublePanna.setVisibility(View.VISIBLE);
            this.binding.btnTripplePanna.setVisibility(View.VISIBLE);
            this.binding.btnSingle.setVisibility(View.GONE);
            this.binding.btnJodi.setVisibility(View.GONE);
            this.binding.btnPatti.setVisibility(View.GONE);
            this.binding.btnCP.setVisibility(View.GONE);
        });
        this.binding.btnSinglePanna.setOnClickListener(view -> {
            Intent i = new Intent(this, BetPlacementActivity.class);
            i.putExtra("type", "singlePanna");
            i.putExtra("id", getIntent().getStringExtra("id"));
            i.putExtra("time",getIntent().getStringExtra("time"));
            startActivity(i);
        });
        this.binding.btnDoublePanna.setOnClickListener(view -> {
            Intent i = new Intent(this, BetPlacementActivity.class);
            i.putExtra("type", "doublePanna");
            i.putExtra("id", getIntent().getStringExtra("id"));
            i.putExtra("time",getIntent().getStringExtra("time"));
            startActivity(i);
        });
        this.binding.btnTripplePanna.setOnClickListener(view -> {
            Intent i = new Intent(this, BetPlacementActivity.class);
            i.putExtra("type", "triplePanna");
            i.putExtra("id", getIntent().getStringExtra("id"));
            i.putExtra("time",getIntent().getStringExtra("time"));
            startActivity(i);
        });
        this.binding.btnJodi.setOnClickListener(view -> {
            Intent i = new Intent(this, BetPlacementJodiActivity.class);
            i.putExtra("type", "jodi");
            i.putExtra("id", getIntent().getStringExtra("id"));
            i.putExtra("id2", getIntent().getStringExtra("id2"));
            i.putExtra("time",getIntent().getStringExtra("time"));
            startActivity(i);
        });
        this.binding.btnCP.setOnClickListener(view -> {
            Intent i = new Intent(this, BetPlacementActivity.class);
            i.putExtra("type", "cp");
            i.putExtra("id", getIntent().getStringExtra("id"));
            i.putExtra("time",getIntent().getStringExtra("time"));
            startActivity(i);
        });

        binding.bottomBar.btnSetting.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,SettingActivity.class);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });

        binding.bottomBar.btnReferAndEarn.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,ReferAndEarnActivity.class);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });


        binding.bottomBar.btnResult.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,WebviewActivity.class);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });


        binding.bottomBar.btnHome.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,MainActivity.class);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });
    }
}

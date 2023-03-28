package com.kidogame.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.SubGameAdapter;
import com.kidogame.android.databinding.ActivitySubGameBinding;
import com.kidogame.android.model.game_list.GameListResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.market_ratio.MarketRatioResponse;
import com.kidogame.android.model.profile.ProfileResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubGameActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivitySubGameBinding binding;
    KProgressHUD hud;
    Preference preference;
    SubGameAdapter subGameAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivitySubGameBinding) DataBindingUtil.setContentView(this, R.layout.activity_sub_game);
        Preference preference2 = new Preference(this);
        this.preference = preference2;
        UserInfo userInfo = preference2.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        this.binding.rvSubGameList.setLayoutManager(new LinearLayoutManager(this));
        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.btnMyBets.setOnClickListener(view -> {
            Intent i = new Intent(this,MyBetHistoryActivity.class);
            i.putExtra("id",getIntent().getStringExtra("id"));
            startActivity(i);
        });
        this.binding.btnMyWins.setOnClickListener(view -> {
            Intent i = new Intent(this,MyWinHistoryActivity.class);
            i.putExtra("id",getIntent().getStringExtra("id"));
            startActivity(i);
        });
        getGameList();

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

    @Override
    protected void onResume() {
        super.onResume();
        //for now
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



    private void getGameList() {
        String currentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Long.valueOf(Calendar.getInstance().getTime().getTime()));
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getGameList("game_list", currentDay, getIntent().getStringExtra("id")).enqueue(new Callback<GameListResponse>() {
            public void onResponse(Call<GameListResponse> call, Response<GameListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().intValue() == 0) {
                    SubGameActivity.this.subGameAdapter = new SubGameAdapter(response.body().getResults());
                    SubGameActivity.this.binding.rvSubGameList.setAdapter(SubGameActivity.this.subGameAdapter);
                }
                SubGameActivity.this.getMarketRatio();
            }

            public void onFailure(Call<GameListResponse> call, Throwable t) {
                SubGameActivity.this.getMarketRatio();
            }
        });
    }

    /* access modifiers changed from: private */
    public void getMarketRatio() {
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getMarketRatio("market_ratio", this.preference.getUserDetails().getMobile(), getIntent().getStringExtra("id")).enqueue(new Callback<MarketRatioResponse>() {
            public void onResponse(Call<MarketRatioResponse> call, Response<MarketRatioResponse> response) {
                SubGameActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().intValue() == 0) {
                    TextView textView = SubGameActivity.this.binding.tvSingleDigit;
                    textView.setText("Single Digit :- " + response.body().getRatio().getSd());
                    TextView textView2 = SubGameActivity.this.binding.tvSinglePanna;
                    textView2.setText("Single Panna :- " + response.body().getRatio().getSp());
                    TextView textView3 = SubGameActivity.this.binding.tvDoublePanna;
                    textView3.setText("Double Panna :- " + response.body().getRatio().getDp());
                    TextView textView4 = SubGameActivity.this.binding.tvTripplePanna;
                    textView4.setText("Tripple Panna :- " + response.body().getRatio().getTp());
                    TextView textView5 = SubGameActivity.this.binding.tvCP;
                    textView5.setText("CP :- " + response.body().getRatio().getCp());
                    TextView textView6 = SubGameActivity.this.binding.tvJodi;
                    textView6.setText("Jodi :- " + response.body().getRatio().getJodi());
                }
            }

            public void onFailure(Call<MarketRatioResponse> call, Throwable t) {
                SubGameActivity.this.hud.dismiss();
            }
        });
    }
}

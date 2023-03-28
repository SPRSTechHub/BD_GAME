package com.kidogame.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.TransactionHistoryAdapter;
import com.kidogame.android.databinding.ActivityTranscationHistoryBinding;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;
import com.kidogame.android.model.transaction_history.TransactionHistoryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranscationHistoryActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivityTranscationHistoryBinding binding;
    KProgressHUD hud;
    Preference preference;
    TransactionHistoryAdapter transactionHistoryAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivityTranscationHistoryBinding) DataBindingUtil.setContentView(this, R.layout.activity_transcation_history);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        this.binding.rvTransactionHistory.setLayoutManager(new LinearLayoutManager(this));
        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        UserInfo userInfo = this.preference.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }
        getTransactionHistory();

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



    private void getTransactionHistory() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getTransactionHistory("trans_history", this.preference.getUserDetails().getMobile(), "id", "desc", "10", "").enqueue(new Callback<TransactionHistoryResponse>() {
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                TranscationHistoryActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().intValue() == 0) {
                        TranscationHistoryActivity.this.transactionHistoryAdapter = new TransactionHistoryAdapter(response.body().getData());
                        TranscationHistoryActivity.this.binding.rvTransactionHistory.setAdapter(TranscationHistoryActivity.this.transactionHistoryAdapter);
                    }
                    Toast.makeText(TranscationHistoryActivity.this, response.body().getMessage(), 0).show();
                }
            }

            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                TranscationHistoryActivity.this.hud.dismiss();
            }
        });
    }
}

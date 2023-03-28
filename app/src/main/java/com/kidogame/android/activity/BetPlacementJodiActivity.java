package com.kidogame.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.BetPlacementJodiAdapter;
import com.kidogame.android.databinding.ActivityBetPlacementJodiBinding;
import com.kidogame.android.model.betPlacement.BetPlacementJodiRequest;
import com.kidogame.android.model.betPlacement.BetPlacementResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BetPlacementJodiActivity extends AppCompatActivity {
    ApiInterface apiService;
    ArrayList<BetPlacementJodiRequest> arrData = new ArrayList<>();
    String betAmount;
    BetPlacementJodiAdapter betPlacementAdapter;
    String betType;
    String betValue1;
    String betValue2;
    ActivityBetPlacementJodiBinding binding;
    int currentPosition = 0;
    KProgressHUD hud;
    int length = 0;
    String matchId1;
    String matchId2;
    String mobileNo;
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
        this.binding = (ActivityBetPlacementJodiBinding) DataBindingUtil.setContentView(this, R.layout.activity_bet_placement_jodi);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        UserInfo userInfo = this.preference.getUserDetails();
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }
        this.mobileNo = userInfo.getMobile();
        this.binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        this.binding.rvBetPlacement.setLayoutManager(new LinearLayoutManager(this));
        this.betPlacementAdapter = new BetPlacementJodiAdapter(this.arrData);
        this.binding.rvBetPlacement.setAdapter(this.betPlacementAdapter);
        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.btnPlaceBet.setOnClickListener(view -> {
            if (this.arrData.size() > 0) {
                this.hud.show();
                this.betValue1 = this.arrData.get(0).getDigit1();
                this.betValue2 = this.arrData.get(0).getDigit2();
                String amount = this.arrData.get(0).getAmount();
                this.betAmount = amount;
                placeBet(this.betValue1, this.betValue2, amount);
                return;
            }
            Toast.makeText(this, "Please add at-least one bet", 0).show();
        });
        String type = getIntent().getStringExtra("type");
        this.matchId1 = getIntent().getStringExtra("id");
        this.matchId2 = getIntent().getStringExtra("id2");
        if (type.equals("jodi")) {
            setEditTextMaxLength(1);
            this.length = 1;
            this.betType = "jodi";
        }
        this.binding.btnAdd.setOnClickListener(view -> {
            if (this.binding.edtEnterDigit.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter Digit",  Toast.LENGTH_SHORT).show();
            } else if (this.binding.edtEnterDigit.getText().toString().length() != this.length) {
                Toast.makeText(this, "Digit Should be in " + this.length + " Characters",  Toast.LENGTH_SHORT).show();
            } else if (this.binding.edtEnterDigit2.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter Digit2",  Toast.LENGTH_SHORT).show();
            } else if (this.binding.edtEnterDigit2.getText().toString().length() != this.length) {
                Toast.makeText(this, "Digit Should be in " + this.length + " Characters",  Toast.LENGTH_SHORT).show();
            } else if (this.binding.edtEnterAmount.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter Amount",  Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(this.binding.edtEnterAmount.getText().toString().trim()) <= 0) {
                Toast.makeText(this, "Amount should be greater than zero", Toast.LENGTH_SHORT).show();
            } else {
                int i = 0;
                while (i < this.arrData.size()) {
                    if (!this.arrData.get(i).getDigit1().equals(this.binding.edtEnterDigit.getText().toString()) || !this.arrData.get(i).getDigit2().equals(this.binding.edtEnterDigit2.getText().toString())) {
                        i++;
                    } else {
                        Toast.makeText(this, "Already added!",  Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                this.arrData.add(new BetPlacementJodiRequest(this.binding.edtEnterDigit.getText().toString().trim(), this.binding.edtEnterDigit2.getText().toString().trim(), this.binding.edtEnterAmount.getText().toString().trim()));
                this.betPlacementAdapter.notifyDataSetChanged();
            }
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

        if(getIntent().hasExtra("time") && getIntent().getStringExtra("time") != null && !getIntent().getStringExtra("time").isEmpty()
                && getIntent().getStringExtra("time").contains(":")) {
            Date date1 = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getIntent().getStringExtra("time").split(":")[0]));// for 6 hour
            calendar.set(Calendar.MINUTE, Integer.parseInt(getIntent().getStringExtra("time").split(":")[1]));// for 0 min
            calendar.set(Calendar.SECOND, 0);// for 0 sec
            System.out.println(calendar.getTime());// print 'Mon Mar 28 06:00:00 ALMT 2016'
            Date date2 = new Date();

            long difference = calendar.getTime().getTime() - date2.getTime();
            new CountDownTimer(difference, 1000) {

                public void onTick(long millisUntilFinished) {
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                    binding.timeHour.setText(String.format("%d hr %d min %d sec",
                            hours,
                            minutes, seconds)
                    );
                }

                public void onFinish() {
                    finish();
                }

            }.start();
        }
    }


    public void setEditTextMaxLength(int length2) {
        this.binding.edtEnterDigit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length2)});
    }

    /* access modifiers changed from: private */
    public void placeBet(String betValue12, String betValue22, String betAmount2) {
        Date date = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Long.valueOf(date.getTime()));
        String currentTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Long.valueOf(date.getTime()));
        String currentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Long.valueOf(date.getTime()));
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.placeBetJodi("jodiplace", this.matchId1, this.matchId2, this.mobileNo, this.betType, betValue12, betValue22, betAmount2, currentDate, currentTime, currentDay).enqueue(new Callback<BetPlacementResponse>() {
            public void onResponse(Call<BetPlacementResponse> call, Response<BetPlacementResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(BetPlacementJodiActivity.this, response.body().getMessage() + " " + BetPlacementJodiActivity.this.currentPosition + 1, 0).show();
                }
                BetPlacementJodiActivity.this.currentPosition++;
                if (BetPlacementJodiActivity.this.arrData.size() == BetPlacementJodiActivity.this.currentPosition) {
                    BetPlacementJodiActivity.this.hud.dismiss();
                    BetPlacementJodiActivity.this.finish();
                    return;
                }
                BetPlacementJodiActivity betPlacementJodiActivity = BetPlacementJodiActivity.this;
                betPlacementJodiActivity.placeBet(betPlacementJodiActivity.arrData.get(BetPlacementJodiActivity.this.currentPosition).getDigit1(), BetPlacementJodiActivity.this.arrData.get(BetPlacementJodiActivity.this.currentPosition).getDigit2(), BetPlacementJodiActivity.this.arrData.get(BetPlacementJodiActivity.this.currentPosition).getAmount());
            }

            public void onFailure(Call<BetPlacementResponse> call, Throwable t) {
                BetPlacementJodiActivity.this.currentPosition++;
                if (BetPlacementJodiActivity.this.arrData.size() == BetPlacementJodiActivity.this.currentPosition) {
                    BetPlacementJodiActivity.this.hud.dismiss();
                    BetPlacementJodiActivity.this.finish();
                    return;
                }
                BetPlacementJodiActivity betPlacementJodiActivity = BetPlacementJodiActivity.this;
                betPlacementJodiActivity.placeBet(betPlacementJodiActivity.arrData.get(BetPlacementJodiActivity.this.currentPosition).getDigit1(), BetPlacementJodiActivity.this.arrData.get(BetPlacementJodiActivity.this.currentPosition).getDigit2(), BetPlacementJodiActivity.this.arrData.get(BetPlacementJodiActivity.this.currentPosition).getAmount());
            }
        });
    }
}

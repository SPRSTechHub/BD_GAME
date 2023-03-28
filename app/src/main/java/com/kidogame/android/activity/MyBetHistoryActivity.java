package com.kidogame.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.BetHistoryAdapter;
import com.kidogame.android.adapter.WinHistoryAdapter;
import com.kidogame.android.databinding.ActivityBetHistoryBinding;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;
import com.kidogame.android.model.win_history.Datum;
import com.kidogame.android.model.win_history.WinHistoryResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBetHistoryActivity extends AppCompatActivity {
    WinHistoryAdapter betHistoryAdapter;
    ActivityBetHistoryBinding binding;
    Preference preference;
    ApiInterface apiService;
    KProgressHUD hud;
    String sorting = "desc";
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivityBetHistoryBinding) DataBindingUtil.setContentView(this, R.layout.activity_bet_history);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        UserInfo userInfo = this.preference.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }
        this.binding.rvBetHistory.setLayoutManager(new LinearLayoutManager(this));
        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });

        binding.ivSort.setOnClickListener(view -> {
            if(sorting.equals("asc")){
                sorting = "desc";
            }else{
                sorting = "asc";
            }
            binding.tvMonth.setText("MONTH");
            binding.tvBetType.setText("BET TYPE");
            getBetHistory("");
        });

        getBetHistory("");

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

        binding.rlMonth.setOnClickListener(view -> {
            CharSequence day[] = new CharSequence[]{"January", "February", "March", "April","May","June","July","August","September","October","November","December"};

            AlertDialog.Builder builder = new AlertDialog.Builder(MyBetHistoryActivity.this);
            builder.setTitle("Select Month");
            builder.setItems(day, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("value is", "" + which);
                    binding.tvMonth.setText(day[which]);
                    switch (which) {
                        case 0:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("01");
                            break;
                        case 1:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("02");
                            break;
                        case 2:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("03");
                            break;
                        case 3:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("04");
                            break;
                        case 4:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("05");
                            break;
                        case 5:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("06");
                            break;
                        case 6:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("07");
                            break;
                        case 7:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("08");
                            break;
                        case 8:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("09");
                            break;
                        case 9:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("10");
                            break;
                        case 10:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("11");
                            break;
                        case 11:
                            binding.tvBetType.setText("BET TYPE");
                            getBetHistory("12");
                            break;
                    }
                }
            });
            builder.show();
        });

        binding.rlBetType.setOnClickListener(view -> {
            CharSequence bet_type[] = new CharSequence[]{"Single Digit", "Single Panna", "Double Panna", "Triple Panna","Jodi","CP"};

            AlertDialog.Builder builder = new AlertDialog.Builder(MyBetHistoryActivity.this);
            builder.setTitle("Select Bet Type");
            builder.setItems(bet_type, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("value is", "" + which);
                    binding.tvBetType.setText(bet_type[which]);
                    switch (which) {
                        case 0:
                            binding.tvMonth.setText("MONTH");
                            getBetHistory("SD");
                            break;
                        case 1:
                            binding.tvMonth.setText("MONTH");
                            getBetHistory("SP");
                            break;
                        case 2:
                            binding.tvMonth.setText("MONTH");
                            getBetHistory("DP");
                            break;
                        case 3:
                            binding.tvMonth.setText("MONTH");
                            getBetHistory("TP");
                            break;
                        case 4:
                            binding.tvMonth.setText("MONTH");
                            getBetHistory("JODI");
                            break;
                        case 5:
                            binding.tvMonth.setText("MONTH");
                            getBetHistory("CP");
                            break;
                    }
                }
            });
            builder.show();
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


    private void getBetHistory(String searchKey) {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getWinHistory("bet_history", this.preference.getUserDetails().getMobile(), getIntent().getStringExtra("id"), "date", sorting, "0","10",searchKey).enqueue(new Callback<WinHistoryResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onResponse(Call<WinHistoryResponse> call, Response<WinHistoryResponse> response) {
                MyBetHistoryActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<Datum> list = response.body().getData();
                        if(list != null) {
                            Map<String, List<Datum>> groupByCountry = list.stream().collect(
                                    Collectors.groupingBy(Datum::getDate));
                            MyBetHistoryActivity.this.betHistoryAdapter = new WinHistoryAdapter(groupByCountry);
                            MyBetHistoryActivity.this.binding.rvBetHistory.setAdapter(MyBetHistoryActivity.this.betHistoryAdapter);
                        }else{
                            Map<String, List<Datum>> groupByCountry = new HashMap<>();
                            MyBetHistoryActivity.this.betHistoryAdapter = new WinHistoryAdapter(groupByCountry);
                            MyBetHistoryActivity.this.binding.rvBetHistory.setAdapter(MyBetHistoryActivity.this.betHistoryAdapter);
                            Toast.makeText(MyBetHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Map<String, List<Datum>> groupByCountry = new HashMap<>();
                        MyBetHistoryActivity.this.betHistoryAdapter = new WinHistoryAdapter(groupByCountry);
                        MyBetHistoryActivity.this.binding.rvBetHistory.setAdapter(MyBetHistoryActivity.this.betHistoryAdapter);
                        Toast.makeText(MyBetHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onFailure(Call<WinHistoryResponse> call, Throwable t) {
                MyBetHistoryActivity.this.hud.dismiss();
            }
        });
    }

    private List<Datum> sortArray(List<Datum> arraylist) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH); //your own date format
        if (arraylist != null) {
            Collections.sort(arraylist, (o1, o2) -> {
                try {
                    return simpleDateFormat.parse(o2.getDate()).compareTo(simpleDateFormat.parse(o1.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            });
            return arraylist;
        }
        return null;
    }
}

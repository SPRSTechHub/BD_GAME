package com.kidogame.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.SliderAdapterExample;
import com.kidogame.android.databinding.ActivityAddWalletBinding;
import com.kidogame.android.model.SliderItem;
import com.kidogame.android.model.add_money.AddMoneyResponse;
import com.kidogame.android.model.addmoney_online.AddMoneyOnlineResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.offer.OfferResponse;
import com.kidogame.android.model.profile.ProfileResponse;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddWalletActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivityAddWalletBinding binding;
    KProgressHUD hud;
    Preference preference;
    private EditText edittrno;
    String trno = null;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivityAddWalletBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_wallet);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        this.binding.btnTransactionHistory.setOnClickListener(view -> {
            startActivity(new Intent(this, TranscationHistoryActivity.class));
        });
        this.binding.btnProceedToTopup.setOnClickListener(view -> {
            if (this.binding.edtEnterAmount.getText().toString().isEmpty() || Integer.parseInt(this.binding.edtEnterAmount.getText().toString()) <= 0) {
                Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            } else {
                CharSequence colors[] = new CharSequence[]{"Online", "Offline"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddWalletActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                addMoneyOffline();
                                break;
                            case 1:
                                addMoney();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        UserInfo userInfo = this.preference.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
        }

        binding.tv500.setOnClickListener(view -> {
            binding.edtEnterAmount.setText("500");
        });

        binding.tv1000.setOnClickListener(view -> {
            binding.edtEnterAmount.setText("1000");
        });

        binding.tv3000.setOnClickListener(view -> {
            binding.edtEnterAmount.setText("3000");
        });

        getOfferDetails();

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

    private void addMoneyOffline() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.addMoneyOnline("add_money_ct", this.preference.getUserDetails().getMobile(),binding.edtEnterAmount.getText().toString().trim(),"credit","upiapi").enqueue(new Callback<AddMoneyOnlineResponse>() {
            public void onResponse(Call<AddMoneyOnlineResponse> call, Response<AddMoneyOnlineResponse> response) {
                AddWalletActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getData().getPayment_url()));
                    startActivity(browserIntent);
                    Toast.makeText(AddWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<AddMoneyOnlineResponse> call, Throwable t) {
                AddWalletActivity.this.hud.dismiss();
            }
        });
    }

    private void getOfferDetails() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getOffer("get_depo_offers", this.preference.getUserDetails().getMobile()).enqueue(new Callback<OfferResponse>() {
            public void onResponse(Call<OfferResponse> call, Response<OfferResponse> response) {
                AddWalletActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                    List<SliderItem> list = new ArrayList<>();
                    for(int i = 0;i<response.body().getOfferList().size();i++){
                        list.add(new SliderItem(response.body().getOfferList().get(i).getOffer_link()));
                    }
                    SliderAdapterExample adapter = new SliderAdapterExample(AddWalletActivity.this,list);
                    binding.imageSlider.setSliderAdapter(adapter);
                    binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                    binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                    binding.imageSlider.setScrollTimeInSec(3);
                    binding.imageSlider.setAutoCycle(true);
                    binding.imageSlider.startAutoCycle();
                }
            }

            public void onFailure(Call<OfferResponse> call, Throwable t) {
                AddWalletActivity.this.hud.dismiss();
            }
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

/*
    private void addMoney() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.addMoney("add_money", this.preference.getUserDetails().getMobile(), this.binding.edtEnterAmount.getText().toString().trim(), "credit", this.preference.getUserDetails().getBal(), "Credit Wallet", String.valueOf(System.currentTimeMillis())).enqueue(new Callback<AddMoneyResponse>() {
            public void onResponse(Call<AddMoneyResponse> call, Response<AddMoneyResponse> response) {
                AddWalletActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        AddWalletActivity.this.finish();
                    }
                    Toast.makeText(AddWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<AddMoneyResponse> call, Throwable t) {
                AddWalletActivity.this.hud.dismiss();
            }
        });
    }
*/
    private void addMoney() {
        showTrBox().show();
    }

    AlertDialog showTrBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddWalletActivity.this);
        LayoutInflater inflater = AddWalletActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialouge_trno, null);
        builder.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        trno = edittrno.getText().toString();
                        if(trno == null || trno.isEmpty() || trno.length() <= 5){
                            Toast.makeText(AddWalletActivity.this, "Transaction Number missing!", Toast.LENGTH_SHORT).show();
                        }else{
                            addMoneyOff(trno);
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        edittrno =view.findViewById(R.id.txt_trno);
        return builder.create();
    }

    private void addMoneyOff(String ntrno) {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.addMoney("add_money",
                this.preference.getUserDetails().getMobile(),
                this.binding.edtEnterAmount.getText().toString().trim(), "credit",
                this.preference.getUserDetails().getBal(), "Credit Wallet",
                ntrno.toString()).enqueue(new Callback<AddMoneyResponse>() {
            public void onResponse(Call<AddMoneyResponse> call, Response<AddMoneyResponse> response) {
                AddWalletActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        AddWalletActivity.this.finish();
                    }
                    Toast.makeText(AddWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(Call<AddMoneyResponse> call, Throwable t) {
                AddWalletActivity.this.hud.dismiss();
            }
        });
    }
}

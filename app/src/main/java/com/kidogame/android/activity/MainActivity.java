package com.kidogame.android.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.MainGameAdapter;
import com.kidogame.android.adapter.SliderAdapterExample;
import com.kidogame.android.databinding.ActivityMainBinding;
import com.kidogame.android.model.SliderItem;
import com.kidogame.android.model.game_category.GameCategoryResponse;
import com.kidogame.android.model.login_response.LoginResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.offer.OfferResponse;
import com.kidogame.android.model.profile.ProfileResponse;
import com.onesignal.OneSignal;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivityMainBinding binding;
    KProgressHUD hud;
    MainGameAdapter mainGameAdapter;
    Preference preference;
    FirebaseRemoteConfig remoteConfig;

    private static final String ONESIGNAL_APP_ID = "62c6113a-6b1c-4d8d-8e2d-228a5a70f234";


    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        UserInfo userInfo = this.preference.getUserDetails();
        TextView textView = this.binding.actHome.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
        && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.actHome.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.actHome.tvWallet.setText("₹ " + userInfo.getBal());
        }
        this.binding.actHome.ivProfile.setOnClickListener(view -> {
            this.binding.drawerLayout.openDrawer((int) GravityCompat.START);
        });

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();


        int currentVersionCode;
        currentVersionCode = getCurrentVersionCode();
        Log.d("myapp", String.valueOf(currentVersionCode));
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>(){
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    final String new_version_code = remoteConfig.getString("new_version_code");
                    if (Integer.parseInt(new_version_code)> getCurrentVersionCode()){
                        showUpdateDialog();
                    }
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.tvUsername);
        navUsername.setText(userInfo.getMobile());

        this.binding.actHome.rvGameList.setLayoutManager(new LinearLayoutManager(this));
        this.binding.actHome.btnTransfer.setOnClickListener(view -> {
            startActivity(new Intent(this, WithdrawActivity.class));
        });
        this.binding.actHome.btnAddMoney.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.actHome.btnHistory.setOnClickListener(view -> {
            startActivity(new Intent(this, TranscationHistoryActivity.class));
        });
        this.binding.actHome.btnSupport.setOnClickListener(view -> {
            openWhatsApp("+918293117066","Hello Support Team");
        });
        this.binding.actHome.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));

        });
        this.binding.actHome.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));

        });
        this.binding.nvView.setNavigationItemSelectedListener(item -> {
            this.binding.drawerLayout.closeDrawer((int) GravityCompat.START);
            if (item.getItemId() == R.id.opTransactionHistory) {
                startActivity(new Intent(this, TranscationHistoryActivity.class));
            }else if (item.getItemId() == R.id.opHome) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else if (item.getItemId() == R.id.opWithdraw) {
                startActivity(new Intent(this, WithdrawActivity.class));
            }else if (item.getItemId() == R.id.opGameRule) {
               // startActivity(new Intent(this, TranscationHistoryActivity.class));
                Toast.makeText(this, "Pending..", Toast.LENGTH_SHORT).show();
            }else if (item.getItemId() == R.id.opWhatsapp) {
                openWhatsApp("+918293117066","Hello Support Team");
            }else if (item.getItemId() == R.id.opContactUs) {
                openWhatsApp("+918293117066","Hello Support Team");
            }else if (item.getItemId() == R.id.opLogout) {
               preference.clear();
                startActivity(new Intent(this, LoginActivity.class));
                finish();

            }
            return true;
        });
        getGameList();


        binding.actHome.bottomBar.btnSetting.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,SettingActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });

        binding.actHome.bottomBar.btnReferAndEarn.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,ReferAndEarnActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });


        binding.actHome.bottomBar.btnResult.setOnClickListener(view -> {
            Intent newIntent = new Intent(this,WebviewActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            overridePendingTransition(0,0);
            finish();
        });

        binding.actHome.bottomBar.btnResult.setBackground(null);
        binding.actHome.bottomBar.btnReferAndEarn.setBackground(null);
        binding.actHome.bottomBar.btnSetting.setBackground(null);
        binding.actHome.bottomBar.btnHome.setBackgroundColor(getResources().getColor(R.color.white_alpha));

        getOfferDetails();

    }



    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
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

    private void getOfferDetails() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getOffer("get_offers", this.preference.getUserDetails().getMobile()).enqueue(new Callback<OfferResponse>() {
            public void onResponse(Call<OfferResponse> call, Response<OfferResponse> response) {
                MainActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                    List<SliderItem> list = new ArrayList<>();
                    for(int i = 0;i<response.body().getOfferList().size();i++){
                        list.add(new SliderItem(response.body().getOfferList().get(i).getOffer_link()));
                    }
                    SliderAdapterExample adapter = new SliderAdapterExample(MainActivity.this,list);
                    binding.actHome.imageSlider.setSliderAdapter(adapter);
                    binding.actHome.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    binding.actHome.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    binding.actHome.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    binding.actHome.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                    binding.actHome.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                    binding.actHome.imageSlider.setScrollTimeInSec(3);
                    binding.actHome.imageSlider.setAutoCycle(true);
                    binding.actHome.imageSlider.startAutoCycle();
                }
            }

            public void onFailure(Call<OfferResponse> call, Throwable t) {
                MainActivity.this.hud.dismiss();
            }
        });
    }

    private void getGameList() {
        String currentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Long.valueOf(Calendar.getInstance().getTime().getTime()));
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getGameCategory("game_cat", currentDay).enqueue(new Callback<GameCategoryResponse>() {
            public void onResponse(Call<GameCategoryResponse> call, Response<GameCategoryResponse> response) {
                MainActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().intValue() == 0) {
                    MainActivity.this.mainGameAdapter = new MainGameAdapter(response.body().getMessage().getResults());
                    MainActivity.this.binding.actHome.rvGameList.setAdapter(MainActivity.this.mainGameAdapter);
                }
            }

            public void onFailure(Call<GameCategoryResponse> call, Throwable t) {
                MainActivity.this.hud.dismiss();
            }
        });
    }

    private void getUserDetails() {
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.getUserDetails(preference.getUserDetails().getMobile(), "get_user").enqueue(new Callback<ProfileResponse>() {
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    if(response.body().getStatus() == 0){
                        preference.storeUserProfile(response.body());
                        binding.actHome.tvWallet.setText("₹ " + response.body().getResult().getWallet().getBalAmnt());

                    }
                }
            }

            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });


    }

    private void showUpdateDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Update Available")
                .setMessage("Update Now")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://fatafatguru.in/")));
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
        dialog.setCancelable(false);
    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            Log.d("myapp", e.getMessage());
        }

        return packageInfo.versionCode;
    }
}

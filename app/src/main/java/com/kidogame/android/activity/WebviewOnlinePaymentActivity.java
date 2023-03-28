package com.kidogame.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kaopiz.kprogresshud.KProgressHUD;

import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.adapter.MainGameAdapter;
import com.kidogame.android.databinding.ActivityPaymentonlineBinding;

public class WebviewOnlinePaymentActivity extends AppCompatActivity {
    ActivityPaymentonlineBinding binding;
    ApiInterface apiService;
    KProgressHUD hud;
    MainGameAdapter mainGameAdapter;
    Preference preference;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivityPaymentonlineBinding) DataBindingUtil.setContentView(this, R.layout.activity_paymentonline);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.webView.loadUrl(getIntent().getStringExtra("url"));
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                Uri uri = Uri.parse(url);
                String server = uri.getAuthority();
                String path = uri.getPath();
                String protocol = uri.getScheme();
                String prevStat = uri.getQueryParameter("prevStat");  //will return "V-Maths-Addition "
                if(prevStat != null){
                    if(prevStat.equalsIgnoreCase("FAILED")){
                        Toast.makeText(WebviewOnlinePaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(WebviewOnlinePaymentActivity.this, "Payment Success", Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(WebviewOnlinePaymentActivity.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.contains("http://exitme")) {
                    finish();
                }
                return true;
            }
        });

        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setAllowContentAccess(true);

    }


}
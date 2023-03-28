package com.kidogame.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.databinding.ActivityLoginBinding;
import com.kidogame.android.model.forgotPassword.ForgotPasswordResponse;
import com.kidogame.android.model.login_response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    ApiInterface apiService;
    ActivityLoginBinding binding;
    KProgressHUD hud;
    Preference preference;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.preference = new Preference(this);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.ivRememberMeCheckBox.setOnClickListener(view -> {
            if (this.binding.ivRememberMeCheckBox.getTag().equals("checked")) {
                this.binding.ivRememberMeCheckBox.setTag("uncheck");
                this.binding.ivCheckedRememberMe.setVisibility(View.GONE);
                return;
            }
            this.binding.ivRememberMeCheckBox.setTag("checked");
            this.binding.ivCheckedRememberMe.setVisibility(View.VISIBLE);
        });
        this.binding.tvRememberMe.setOnClickListener(view -> {
            if (this.binding.ivRememberMeCheckBox.getTag().equals("checked")) {
                this.binding.ivRememberMeCheckBox.setTag("uncheck");
                this.binding.ivCheckedRememberMe.setVisibility(View.GONE);
                return;
            }
            this.binding.ivRememberMeCheckBox.setTag("checked");
            this.binding.ivCheckedRememberMe.setVisibility(View.VISIBLE);
        });
        this.binding.btnSignIn.setOnClickListener(view -> {
            if (validate()) {
                doLogin();
            }
        });
        this.binding.btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        this.binding.tvForgotPassword.setOnClickListener(view -> {
            if (this.binding.edtMobileNo.getText().toString().trim().isEmpty()) {
                this.binding.edtMobileNo.setError("Please enter mobile No");
                this.binding.edtMobileNo.requestFocus();
            } else if (this.binding.edtMobileNo.getText().toString().trim().length() != 11) {
                this.binding.edtMobileNo.setError("Please enter valid mobile No");
                this.binding.edtMobileNo.requestFocus();
            } else {
                doForGotPassword();
            }
        });

    }


    private void doLogin() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.login(this.binding.edtMobileNo.getText().toString().trim(), this.binding.edtPassword.getText().toString().trim(), "login").enqueue(new Callback<LoginResponse>() {
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
//                        if (LoginActivity.this.binding.ivRememberMeCheckBox.getTag().equals("checked")) {
                            LoginActivity.this.preference.putBoolean("isLogin", true);
//                        }
                        LoginActivity.this.preference.storeUserDetails(response.body().getResult());
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginActivity.this.hud.dismiss();
            }
        });
    }

    private void doForGotPassword() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.forgotPassword(this.binding.edtMobileNo.getText().toString().trim(), "forget_pass").enqueue(new Callback<ForgotPasswordResponse>() {
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                LoginActivity.this.hud.dismiss();
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().getStatus().intValue() == 0) {
                    AlertDialog.Builder title = new AlertDialog.Builder(LoginActivity.this).setTitle("Your Password");
                    title.setMessage("Your Password is : " + response.body().getData().getRtv_password()).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null).setIcon(android.R.drawable.ic_dialog_alert).show();
                }
                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                LoginActivity.this.hud.dismiss();
            }
        });
    }

    private boolean validate() {
        if (this.binding.edtMobileNo.getText().toString().trim().isEmpty()) {
            this.binding.edtMobileNo.setError("Please enter mobile No");
            this.binding.edtMobileNo.requestFocus();
            return false;
        } else if (this.binding.edtMobileNo.getText().toString().trim().length() != 11) {
            this.binding.edtMobileNo.setError("Please enter valid mobile No");
            this.binding.edtMobileNo.requestFocus();
            return false;
        } else if (!this.binding.edtPassword.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            this.binding.edtPassword.setError("Please enter Password");
            this.binding.edtPassword.requestFocus();
            return false;
        }
    }
}

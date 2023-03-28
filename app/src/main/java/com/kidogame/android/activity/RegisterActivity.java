package com.kidogame.android.activity;

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
import com.kidogame.android.databinding.ActivitySignupBinding;
import com.kidogame.android.model.register_response.RegisterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RegisterActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivitySignupBinding binding;
    KProgressHUD hud;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivitySignupBinding) DataBindingUtil.setContentView(this, R.layout.activity_signup);
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.btnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        this.binding.btnSigUp.setOnClickListener(view -> {
            if (validate()) {
                doSignUp();
            }
        });
    }

    private void doSignUp() {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.signUp("signup",
                this.binding.edtMobileNo.getText().toString().trim(),
                this.binding.edtPassword.getText().toString().trim(),
                this.binding.edtUsername.getText().toString().trim(),
                this.binding.edtReferCode.getText().toString().trim()
        ).enqueue(new Callback<RegisterResponse>() {
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().intValue() == 0) {
                    Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }

            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                RegisterActivity.this.hud.dismiss();
            }
        });
    }

    private boolean validate() {
        if (this.binding.edtUsername.getText().toString().trim().isEmpty()) {
            this.binding.edtUsername.setError("Please enter username");
            this.binding.edtUsername.requestFocus();
            return false;
        } else if (this.binding.edtMobileNo.getText().toString().trim().isEmpty()) {
            this.binding.edtMobileNo.setError("Please enter mobile No");
            this.binding.edtMobileNo.requestFocus();
            return false;
        } else if (this.binding.edtMobileNo.getText().toString().trim().length() != 11) {
            this.binding.edtMobileNo.setError("Please enter valid mobile No");
            this.binding.edtMobileNo.requestFocus();
            return false;
        } else if (this.binding.edtPassword.getText().toString().trim().isEmpty()) {
            this.binding.edtPassword.setError("Please enter Password");
            this.binding.edtPassword.requestFocus();
            return false;
        } else if (this.binding.edtPassword.getText().toString().trim().length() >= 6) {
            return true;
        } else {
            this.binding.edtPassword.setError("Password must be at-least 6 characters");
            this.binding.edtPassword.requestFocus();
            return false;
        }
    }
}

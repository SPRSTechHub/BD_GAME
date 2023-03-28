package com.kidogame.android.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kidogame.android.API.ApiClient;
import com.kidogame.android.API.ApiInterface;
import com.kidogame.android.R;
import com.kidogame.android.Utils.Preference;
import com.kidogame.android.databinding.ActivityWithdrawBinding;
import com.kidogame.android.model.add_money.AddMoneyResponse;
import com.kidogame.android.model.login_response.UserInfo;
import com.kidogame.android.model.profile.ProfileResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity {
    ApiInterface apiService;
    ActivityWithdrawBinding binding;
    KProgressHUD hud;
    Preference preference;
    String upiID;

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
        this.binding = (ActivityWithdrawBinding) DataBindingUtil.setContentView(this, R.layout.activity_withdraw);
        Preference preference2 = new Preference(this);
        this.preference = preference2;
        UserInfo userInfo = preference2.getUserDetails();
        TextView textView = this.binding.tvWallet;
        if(preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null){
            binding.tvWallet.setText("₹ " +  preference.getUserProfile().getResult().getWallet().getBalAmnt());
            binding.edtWallet.setText(""+preference.getUserProfile().getResult().getWallet().getBalAmnt());
            binding.tvBal.setText("Rs. "+preference.getUserProfile().getResult().getWallet().getBalAmnt());
        }else {
            binding.tvWallet.setText("₹ " + userInfo.getBal());
            binding.edtWallet.setText(""+userInfo.getBal());
            binding.tvBal.setText("Rs. "+userInfo.getBal());
        }
        binding.edtWallet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double amount ;
                if(!charSequence.toString().isEmpty()) {
                    if (preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                            && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null) {
                        amount = preference.getUserProfile().getResult().getWallet().getBalAmnt();
                    } else {
                        amount = Double.parseDouble(userInfo.getBal());
                    }
                    double enteredAmount = Double.parseDouble(charSequence.toString());
                    if (amount < enteredAmount) {
                        binding.edtWallet.setText(userInfo.getBal());
                        if (preference.getUserProfile() != null && preference.getUserProfile().getResult() != null && preference.getUserProfile().getResult().getWallet() != null
                                && preference.getUserProfile().getResult().getWallet().getBalAmnt() != null) {
                            binding.edtWallet.setText("" + preference.getUserProfile().getResult().getWallet().getBalAmnt());
                        } else {
                            binding.edtWallet.setText(userInfo.getBal());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        this.binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        this.binding.tvWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.ivWallet.setOnClickListener(view -> {
            startActivity(new Intent(this, AddWalletActivity.class));
        });
        this.binding.btnAddBank.setOnClickListener(view -> {
            showCustomDialog();
        });
        this.binding.btnAddUpi.setOnClickListener(view -> {
            showCustomDialogAddUpi();
        });
        this.binding.btnTransactionHistory.setOnClickListener(view -> {
            startActivity(new Intent(this, TranscationHistoryActivity.class));
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

        upiID = preference.getString("UPIID");
        if(upiID.isEmpty()){
            binding.ivWithdraw.setImageResource(R.drawable.ic_check_gray);
            binding.upiID.setText("Please add UPI id");
        }else{
            binding.ivWithdraw.setImageResource(R.drawable.ic_check_green);
            binding.upiID.setText(upiID);
        }

        binding.btnWithDrawNow.setOnClickListener(view -> {
            if(upiID.isEmpty()){
                Toast.makeText(this, "Please add UPI Id", Toast.LENGTH_SHORT).show();
            }else{
                withdrawMoney(binding.edtWallet.getText().toString().trim(),upiID);
            }
        });

        binding.upiID.setOnClickListener(view -> {
            if(upiID.isEmpty()){
                showCustomDialogAddUpi();
            }
        });
    }

    private void showCustomDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_bank, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        EditText bankName = dialogView.findViewById(R.id.edtBankName);
        EditText accountNumber = dialogView.findViewById(R.id.edtAccountNumber);
        EditText IFSCCode = dialogView.findViewById(R.id.edtIFSCCode);
        ((ImageView) dialogView.findViewById(R.id.btnClose)).setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        ((TextView) dialogView.findViewById(R.id.btnConfirm)).setOnClickListener(view -> {
            validateBank(bankName,accountNumber,IFSCCode,alertDialog,dialogView);
        });
        alertDialog.show();
    }

    /* renamed from: lambda$showCustomDialog$7$com-kidogame-android-activity-WithdrawActivity */
    public /* synthetic */ void validateBank(EditText edtBankName, EditText edtAccountNumber, EditText edtIFSCCode, AlertDialog alertDialog, View view) {
        if (edtBankName.getText().toString().trim().isEmpty()) {
            edtBankName.setError("Please enter Bank Name");
        } else if (edtAccountNumber.getText().toString().trim().isEmpty()) {
            edtAccountNumber.setError("Please enter Bank Account Number");
        } else if (edtIFSCCode.getText().toString().trim().isEmpty()) {
            edtIFSCCode.setError("Please enter IFSC Code");
        } else if (!edtIFSCCode.getText().toString().trim().matches("^[A-Z]{4}[0][A-Z0-9]{6}$")) {
            edtIFSCCode.setError("Please enter valid IFSC Code");
        } else {
            alertDialog.dismiss();
            addBank(edtBankName.getText().toString().trim(), edtAccountNumber.getText().toString().trim(), edtIFSCCode.getText().toString().trim(), alertDialog);
        }
    }

    private void addBank(String bankName, String bankAccountNumber, String IFSCCode, final AlertDialog alertDialog) {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.addBank("add_bank", this.preference.getUserDetails().getMobile(), this.preference.getUserDetails().getMobile(), bankName, bankAccountNumber, IFSCCode).enqueue(new Callback<AddMoneyResponse>() {
            public void onResponse(Call<AddMoneyResponse> call, Response<AddMoneyResponse> response) {
                WithdrawActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        alertDialog.dismiss();
                    }
                    Toast.makeText(WithdrawActivity.this, response.body().getMessage(), 0).show();
                }
            }

            public void onFailure(Call<AddMoneyResponse> call, Throwable t) {
                WithdrawActivity.this.hud.dismiss();
            }
        });
    }

    private void withdrawMoney(String amount, String upi) {
        this.hud.show();
        Date date = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Long.valueOf(date.getTime()));
        String currentTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Long.valueOf(date.getTime()));

        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.addWithdraw("add_withdraw", this.preference.getUserDetails().getMobile(),amount, upi, currentDate, currentTime).enqueue(new Callback<AddMoneyResponse>() {
            public void onResponse(Call<AddMoneyResponse> call, Response<AddMoneyResponse> response) {
                WithdrawActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {

                        Toast.makeText(WithdrawActivity.this, "Withdraw Successfully", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(WithdrawActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<AddMoneyResponse> call, Throwable t) {
                WithdrawActivity.this.hud.dismiss();
            }
        });
    }
    private void showCustomDialogAddUpi() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_upi, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        EditText edtUpiID = dialogView.findViewById(R.id.edtBankUpi);
        ((ImageView) dialogView.findViewById(R.id.btnClose)).setOnClickListener(view -> {
            finish();
        });
        ((TextView) dialogView.findViewById(R.id.btnConfirm)).setOnClickListener(view -> {
            validate(edtUpiID,alertDialog,dialogView);
        });
        alertDialog.show();
    }

    /* renamed from: lambda$showCustomDialogAddUpi$9$com-kidogame-android-activity-WithdrawActivity */
    public /* synthetic */ void validate(EditText edtUpiId, AlertDialog alertDialog, View view) {
        if (edtUpiId.getText().toString().trim().isEmpty()) {
            edtUpiId.setError("Please enter UPI Id");
        } else if (!edtUpiId.getText().toString().trim().matches("[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}")) {
            edtUpiId.setError("Please enter valid UPI Id");
        } else {
            alertDialog.dismiss();
            addUpiId(edtUpiId.getText().toString().trim(), alertDialog);
        }
    }

    private void addUpiId(String upiId, final AlertDialog alertDialog) {
        this.hud.show();
        ApiInterface apiInterface = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        this.apiService = apiInterface;
        apiInterface.addUpiId("add_upi", this.preference.getUserDetails().getMobile(), this.preference.getUserDetails().getMobile(), upiId).enqueue(new Callback<AddMoneyResponse>() {
            public void onResponse(Call<AddMoneyResponse> call, Response<AddMoneyResponse> response) {
                WithdrawActivity.this.hud.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        preference.putString("UPIID",upiId);
                        alertDialog.dismiss();
                        upiID = upiId;
                        if(upiID.isEmpty()){
                            binding.ivWithdraw.setImageResource(R.drawable.ic_check_gray);
                            binding.upiID.setText("Please add UPI id");
                        }else{
                            binding.ivWithdraw.setImageResource(R.drawable.ic_check_green);
                            binding.upiID.setText(upiID);
                        }
                    }
                    Toast.makeText(WithdrawActivity.this, response.body().getMessage(), 0).show();
                }
            }

            public void onFailure(Call<AddMoneyResponse> call, Throwable t) {
                WithdrawActivity.this.hud.dismiss();
            }
        });
    }
}

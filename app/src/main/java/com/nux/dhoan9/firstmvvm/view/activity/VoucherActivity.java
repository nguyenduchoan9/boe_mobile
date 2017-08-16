package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.response.Balance;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VoucherActivity extends AppCompatActivity {
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnRefresh)
    Button btnRefresh;
    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;
    private Balance balance;
    @Inject
    PreferencesManager preferencesManager;
    @Inject
    UserRepo userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDependency();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.text_action_bar_add_prepaidcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        initializeUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRepo.getBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProcessing)
                .doOnTerminate(this::hideProcessing)
                .subscribe(balance -> {
                    this.balance = balance;
                    tvBalance.setText(CurrencyUtil.formatVNDecimal(balance.getBalance()));
                });
    }

    private void initializeUI() {
        btnRefresh.setOnClickListener(v -> refreshView());
        btnAdd.setOnClickListener(v -> {
            tvMessage.setVisibility(View.GONE);
            String code = etEmail.getText().toString();
            userRepo.checkVoucherCode(code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(this::showProcessing)
                    .subscribe(statusResponse -> {
                        if (!statusResponse.isStatus()) {
                            hideProcessing();
                            tvMessage.setText("Invalid Code");
                            tvMessage.setTextColor(ContextCompat.getColor(VoucherActivity.this, R.color.colorPrimaryDark));
                            tvMessage.setVisibility(View.VISIBLE);
                        } else {
                            userRepo.addVoucher(code)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(this::showProcessing)
                                    .doOnTerminate(this::hideProcessing)
                                    .subscribe(res -> {
                                        if (res.isStatus()) {
                                            tvMessage.setText("Successful");
                                            tvMessage.setTextColor(ContextCompat.getColor(VoucherActivity.this, R.color.order_green));
                                            tvMessage.setVisibility(View.VISIBLE);
                                            balance.setBalance(res.getBalance());
                                            tvBalance.setText(CurrencyUtil.formatVNDecimal(res.getBalance()));
                                        } else {
                                            tvMessage.setText("Invalid Code");
                                            tvMessage.setTextColor(ContextCompat.getColor(VoucherActivity.this, R.color.colorPrimaryDark));
                                            tvMessage.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    });
        });
    }

    private void initDependency() {
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBack();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshView() {
        tvMessage.setText("");
        tvMessage.setVisibility(View.GONE);
        etEmail.setText("");
    }

    private void handleBack() {
        preferencesManager.setBalance(balance.getBalance());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }
}

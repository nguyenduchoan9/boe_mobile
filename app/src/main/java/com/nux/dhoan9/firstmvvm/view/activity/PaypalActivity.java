package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import java.util.Date;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PaypalActivity extends BaseActivity {
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvDinerName)
    TextView tvDinerName;
    @BindView(R.id.tvOrderDate)
    TextView tvOrderDate;
    @BindView(R.id.tvItemQuantity)
    TextView tvItemQuantity;
    private final int REQUEST_PAYMENT_CODE = 4;
    private final String LOG_TAG = PaypalActivity.class.getSimpleName();
    @BindView(R.id.btnPay)
    Button btnPay;
    TextView tvGoToMenu;
    @BindView(R.id.tvProcessingTitle)
    TextView mTvProcessingTitle;
    @BindView(R.id.rlProcessing)
    RelativeLayout mRlProcessing;
    @Inject
    PreferencesManager preferencesManager;

    @Inject
    OrderRepo orderRepo;

    @Inject
    CartManager cartManager;
    private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDependency();
        setContentView(R.layout.activity_paypal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("THE BILL");
        ButterKnife.bind(this);
        tvAmount.setText(String.valueOf(new BigDecimal(getTotal())));
        tvDinerName.setText(getDinerName());
        tvOrderDate.setText(String.valueOf(new Date()));
        tvItemQuantity.setText(String.valueOf(getItemTotal()) + " item");
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        btnPay.setOnClickListener(v -> onOkPress());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProcessing();
    }

    @Override
    protected void setProcessing() {
        tvProcessingTitle = mTvProcessingTitle;
        rlProcessing = mRlProcessing;
    }

    private void initDependency() {
        ((Application) getApplication()).getComponent()
                .inject(this);
    }

    private void onOkPress() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(getTotal()),
                "USD", getOrderName(), PayPalPayment.PAYMENT_INTENT_SALE);

        Intent i = new Intent(this, PaymentActivity.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        i.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//        PayPalPayment.PAYMENT_INTENT_AUTHORIZE
        startActivityForResult(i, REQUEST_PAYMENT_CODE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AetzVQhJh6kV_v_GDex0IynGO_6ky0VLzvF0D3akJ7YDCVGYSrgTpIW-FAq-AdYlVW0TMJ7XdYpbCPz-");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (null != data) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (null != confirmation) {
                    try {
                        Log.i(LOG_TAG, confirmation.toJSONObject().toString(4));
                        handleSuccessfullyPayment();
                        btnPay.setVisibility(View.GONE);
                        isSuccess = true;
                    } catch (JSONException e) {
                        Log.i(LOG_TAG, "An extremely unlike failure occurred:", e);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i(LOG_TAG, "The user canceled");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i(LOG_TAG, "An invalid Payment of PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void handleSuccessfullyPayment() {
        orderRepo.makeOrder(cartManager.getCart())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(orderResponse -> showProcessing("Processing..."))
                .doOnTerminate(() -> hideProcessing())
                .subscribe(sub -> {
                    cartManager.clear();
                    showDialogInform();
                });
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, PaypalActivity.class);
        return i;
    }

    public float getTotal() {
        return getIntent().getFloatExtra(Constant.KEY_TOTAL_PAYMENT, 0);
    }

    public String getOrderName() {
        return getIntent().getStringExtra(Constant.KEY_ORDER_NAME);
    }

    public int getItemTotal() {
        return getIntent().getIntExtra(Constant.KEY_ORDER_ITEM_TOTAL, 0);
    }

    public String getDinerName() {
        return preferencesManager.getUser().getFullName();
    }

    private void showDialogInform() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog_order_ok, null);

        builder.setView(dialogView);
        tvGoToMenu = (TextView) dialogView.findViewById(R.id.tvGoToMenu);
        tvGoToMenu.setOnClickListener(v -> {
            startActivity(CustomerActivity.newInstance(PaypalActivity.this));
            builder.dismiss();
        });
        builder.setCanceledOnTouchOutside(false);
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            if (KeyEvent.KEYCODE_BACK == keyCode) {
                return true;
            }
            return false;
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (!isSuccess) {
            super.onBackPressed();
        }
    }
}

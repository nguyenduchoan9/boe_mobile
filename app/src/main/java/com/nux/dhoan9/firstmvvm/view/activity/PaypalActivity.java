package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PaypalActivity extends AppCompatActivity {
    private final int REQUEST_PAYMENT_CODE = 4;
    private final String LOG_TAG = PaypalActivity.class.getSimpleName();
    @BindView(R.id.btnPay)
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        ButterKnife.bind(this);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        btnPay.setOnClickListener(v -> onOkPress());
    }

    private void onOkPress() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(1000),
                "USD", "Order", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent i = new Intent(this, PaymentActivity.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        i.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(i, REQUEST_PAYMENT_CODE);
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
                        ToastUtils.toastLongMassage(this, "Payment is successfully");
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

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, PaypalActivity.class);
        return i;
    }
}

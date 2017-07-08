package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.data.request.PaypalPartialReq;
import com.nux.dhoan9.firstmvvm.data.response.Amount;
import com.nux.dhoan9.firstmvvm.data.response.FullRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PartialRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalDetailResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalInvoiceInfoResponse;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.OrderCreateResponse;
import com.nux.dhoan9.firstmvvm.model.PaypalInvoiceResp;
import com.nux.dhoan9.firstmvvm.services.PaypalServices;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.view.fragment.PaypalInfoDialog;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PaypalActivity extends BaseActivity {
    private PaypalInvoiceResp invoiceResp;
    private OrderCreateResponse orderCreateResponse;
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
        getSupportActionBar().setTitle(R.string.bill_text);
        ButterKnife.bind(this);
        tvAmount.setText(String.valueOf(new BigDecimal(getTotal())) + " VND");
        tvDinerName.setText(getDinerName());
        Date orderDate = new Date();
        tvOrderDate.setText(convertDateToVietNam(orderDate));
        tvItemQuantity.setText(String.valueOf(getItemTotal()) + " món");
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        btnPay.setOnClickListener(v -> onOkPress());
    }

    private String convertDateToVietNam(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        String sf = String.format("%s/%s/%s %s:%s:%s", day, month, year, hour, minute, seconds);
        return sf.toString();
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
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
    }

    private void onOkPress() {
        CurrencyUtil.convertVNDToUSD(getTotal())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProcessing(null))
                .doOnTerminate(() -> hideProcessing())
                .subscribe(usdMoney -> {
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(usdMoney),
                            "USD", getOrderName(), PayPalPayment.PAYMENT_INTENT_SALE);
//        LOCALEC

                    Intent i = new Intent(this, PaymentActivity.class);
                    i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
                    i.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//        PayPalPayment.PAYMENT_INTENT_AUTHORIZE
                    startActivityForResult(i, REQUEST_PAYMENT_CODE);
                });

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
            .acceptCreditCards(false)
            .languageOrLocale("en_US")
            .merchantName("BOE")
            .rememberUser(true)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AetzVQhJh6kV_v_GDex0IynGO_6ky0VLzvF0D3akJ7YDCVGYSrgTpIW-FAq-AdYlVW0TMJ7XdYpbCPz-");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (null != data) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (null != confirmation) {
                    try {
                        String res = confirmation.toJSONObject().toString(4);
                        Gson gson = new Gson();
                        invoiceResp = gson.fromJson(res, PaypalInvoiceResp.class);
                        Log.i(LOG_TAG, invoiceResp.toString());
                        showProcessing("Processing...");
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
        orderRepo.makeOrder(getCartParams(), Integer.valueOf(preferencesManager.getTableInfo().table_number))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> hideProcessing())
                .subscribe(orderCreateRes -> {
                    orderCreateResponse = orderCreateRes;
                    if (orderCreateRes.getDish().size() > 0) {
//                        String s = "";
//                        for (Dish item : dishes) {
//                            s = s + item.getName() + "-";
//                        }
//                        ToastUtils.toastLongMassage(PaypalActivity.this, s + " is not available");
                        showDialogConfirmContinue(orderCreateRes.getDish());
                    } else {
                        cartManager.clear();
                        showDialogInform();
                    }

                });
    }

    private String getCartParams() {
        StringBuilder cartParamsBuilder = new StringBuilder();
        List<Integer> cartOrder = cartManager.getCartOrder();
        for (int i = 0; i < cartOrder.size(); i++) {
            int dishId = cartOrder.get(i);
            for (Map.Entry<Integer, Integer> cartItem : cartManager.getCart().entrySet()) {
                if (cartItem.getKey() == dishId) {
                    cartParamsBuilder.append(String.valueOf(cartItem.getKey()))
                            .append("_")
                            .append(String.valueOf(cartItem.getValue()))
                            .append("_");
                }
            }
        }
        String orderParams = cartParamsBuilder.deleteCharAt(cartParamsBuilder.length() - 1).toString();
        return orderParams;
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

    public void showProcessing(String title) {
        if (null != title) {
            tvProcessingTitle.setText(title);
        } else {
            tvProcessingTitle.setText(R.string.text_processing);
        }
        rlProcessing.setVisibility(View.VISIBLE);
    }

    public void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }

    private void showDialogConfirmContinue(List<Dish> infoItemList) {
        FragmentManager fm = getSupportFragmentManager();
        PaypalInfoDialog dialog = PaypalInfoDialog.newInstance(infoItemList);
        dialog.setListener(new PaypalInfoDialog.PaypalListener() {
            @Override
            public void onOkieClick(List<Dish> items) {
                // tiep tuc va tru tien mon khong phuc vu
                float total = 0;
                for (Dish item : items) {
                    int quantity = cartManager.getQuantityById(item.getId());
                    total += (quantity * item.getPrice());
                }
                float finalTotal = total;
                CurrencyUtil.convertVNDToUSD(total)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(() -> showProcessing(null))
                        .subscribe(usdMoney -> {
                            PaypalServices.getCredential()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(credential -> {
                                        PaypalServices.getDetailByPaymentId(invoiceResp.getResponse().getId(), credential.getAccessToken())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .flatMap(new Func1<PaypalDetailResponse, Observable<PartialRefundResponse>>() {
                                                    @Override
                                                    public Observable<PartialRefundResponse> call(PaypalDetailResponse detail) {
                                                        PaypalPartialReq req = new PaypalPartialReq();
                                                        Amount amount = new Amount();
                                                        amount.setCurrency("USD");
                                                        amount.setTotal(String.valueOf(usdMoney));
                                                        req.amount = amount;
                                                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                                        float standardMoney = Float.valueOf(decimalFormat.format(usdMoney));
                                                        return PaypalServices.partialRefund(detail.getTransactions().get(0).getRelatedResource().get(0).getSale().getId(),
                                                                credential.getAccessToken(), String.valueOf(standardMoney))
                                                                .subscribeOn(Schedulers.io());
                                                    }
                                                })
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(res -> {
                                                    StringBuilder dishList = new StringBuilder();
                                                    for (Dish d : infoItemList) {
                                                        dishList.append(String.valueOf(d.getId()))
                                                                .append("_");
                                                    }
                                                    orderRepo.partialRefund(orderCreateResponse.getOrderId(),
                                                            finalTotal, dishList.deleteCharAt(dishList.length() - 1).toString())
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .doOnTerminate(() -> hideProcessing())
                                                            .subscribe(new Subscriber<StatusResponse>() {
                                                                @Override
                                                                public void onCompleted() {

                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {

                                                                }

                                                                @Override
                                                                public void onNext(StatusResponse partialRefundResponse) {
                                                                    cartManager.clear();
                                                                    startActivity(CustomerActivity.newInstance(PaypalActivity.this));
                                                                }
                                                            });
                                                });
                                    });
                        });
            }

            @Override
            public void onAngryClick() {
                // Huy order
//                ToastUtils.toastShortMassage(PaypalActivity.this, "on Angry click");
//                Transa
                PaypalServices.getCredential()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(() -> showProcessing(null))
                        .subscribe(credential -> {
                            ToastUtils.toastShortMassage(PaypalActivity.this, "credential success");
                            PaypalServices.getDetailByPaymentId(invoiceResp.getResponse().getId(), credential.getAccessToken())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMap(new Func1<PaypalDetailResponse, Observable<FullRefundResponse>>() {
                                        @Override
                                        public Observable<FullRefundResponse> call(PaypalDetailResponse detail) {
                                            ToastUtils.toastShortMassage(PaypalActivity.this, "invoive number success");
                                            PaypalPartialReq req = new PaypalPartialReq();
                                            Amount amount = new Amount();
                                            amount.setCurrency("USD");
                                            req.amount = amount;

                                            return PaypalServices.fullRefund(detail.getTransactions().get(0).getRelatedResource().get(0).getSale().getId(),
                                                    credential.getAccessToken())
                                                    .subscribeOn(Schedulers.io());
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<FullRefundResponse>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(FullRefundResponse partialRefundResponse) {
                                            ToastUtils.toastShortMassage(PaypalActivity.this, "fully success");
                                            orderRepo.fullyRefund(orderCreateResponse.getOrderId())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .doOnTerminate(() -> hideProcessing())
                                                    .subscribe(new Subscriber<StatusResponse>() {
                                                        @Override
                                                        public void onCompleted() {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {

                                                        }

                                                        @Override
                                                        public void onNext(StatusResponse statusResponse) {
                                                            ToastUtils.toastShortMassage(PaypalActivity.this, " success");
                                                            cartManager.clear();
                                                            startActivity(CustomerActivity.newInstance(PaypalActivity.this));
                                                        }
                                                    });
                                        }
                                    });
                        });

            }
        });
        dialog.show(fm, "Paypal");
    }
}


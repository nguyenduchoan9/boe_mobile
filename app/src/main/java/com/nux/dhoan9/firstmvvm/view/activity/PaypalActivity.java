package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.request.DescriptionRequest;
import com.nux.dhoan9.firstmvvm.data.request.ListDescription;
import com.nux.dhoan9.firstmvvm.data.request.OrderBaseRequest;
import com.nux.dhoan9.firstmvvm.data.request.OrderPaypalRequest;
import com.nux.dhoan9.firstmvvm.data.request.PaypalPartialReq;
import com.nux.dhoan9.firstmvvm.data.response.Amount;
import com.nux.dhoan9.firstmvvm.data.response.Balance;
import com.nux.dhoan9.firstmvvm.data.response.FullRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PartialRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalDetailResponse;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.OrderCreateResponse;
import com.nux.dhoan9.firstmvvm.model.PaypalInvoiceResp;
import com.nux.dhoan9.firstmvvm.services.PaypalServices;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.view.fragment.PaypalInfoDialog;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    @BindView(R.id.rlProcessingRollBack)
    RelativeLayout rlProcessingRollBack;
    @BindView(R.id.spinnerPayment)
    Spinner spinnerPayment;

    @BindView(R.id.rlBalance)
    RelativeLayout rlBalance;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvRecharge)
    TextView tvRecharge;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @Inject
    PreferencesManager preferencesManager;

    @Inject
    OrderRepo orderRepo;

    @Inject
    CartManager cartManager;
    @Inject
    UserRepo userRepo;
    private boolean isSuccess = false;
    private Balance balance = new Balance();
    private int paymentMethodindex = Constant.PaymentMethod.Paypal;
    private List<String> pays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDependency();
        Log.d("Hoang", "Paypal: " + preferencesManager.getLanguage());
        Utils.handleSelectLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.bill_text);
        ButterKnife.bind(this);
        confiBill();
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);
        pays = Arrays.asList(getResources().getStringArray(R.array.payment_array));
        btnPay.setOnClickListener(v -> {
            tvMsg.setVisibility(View.GONE);
            if (isPaypalMethod()) {
                onOkPress();
            } else if (isCashMethod()) {
                payByCash();
            } else if (isVoucherMethod()) {
                if (balance.getBalance() < getTotal()) {
                    tvMsg.setVisibility(View.VISIBLE);
                    return;
                }
                payByVoucher();
            }
        });

        initializePayment();

    }

    final int VOUCHER_REQUEST_CODE = 204;

    private void initializePayment() {
        tvMsg.setVisibility(View.GONE);
        rlBalance.setVisibility(View.GONE);
        spinnerPayment.setSelection(paymentMethodindex);
        spinnerPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isVoucherMethod()) {
                    setBalance(balance.getBalance());
                    rlBalance.setVisibility(View.VISIBLE);
                } else {
                    rlBalance.setVisibility(View.GONE);
                }
                tvMsg.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvRecharge.setOnClickListener(v -> {
            Intent i = new Intent(PaypalActivity.this, VoucherActivity.class);
            startActivityForResult(i, VOUCHER_REQUEST_CODE);
        });
    }

    private void setBalance(float balance) {
        if (balance >= getTotal()) {
            tvBalance.setTextColor(ContextCompat.getColor(this, R.color.primaryText));
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvBalance.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        tvBalance.setText(CurrencyUtil.formatVNDecimal(balance));
    }

    private boolean isPaypalMethod() {
        String paymentSelection = spinnerPayment.getSelectedItem().toString();
        int index = pays.indexOf(paymentSelection);
        return Constant.PaymentMethod.Paypal == index;
    }

    private boolean isCashMethod() {
        String paymentSelection = spinnerPayment.getSelectedItem().toString();
        int index = pays.indexOf(paymentSelection);
        return Constant.PaymentMethod.Cash == index;
    }

    private boolean isVoucherMethod() {
        String paymentSelection = spinnerPayment.getSelectedItem().toString();
        int index = pays.indexOf(paymentSelection);
        return Constant.PaymentMethod.Voucher == index;
    }

    private void confiBill() {
        tvAmount.setText(CurrencyUtil.formatVNDecimal(getTotal()));
        tvItemQuantity.setText(String.valueOf(getItemTotal()) + " "
                + (getItemTotal() <= 1 ? getString(R.string.text_info_dish) : getString(R.string.text_info_dishes)));
        tvDinerName.setText(getDinerName());
        Date orderDate = new Date();
        tvOrderDate.setText(convertDateToVietNam(orderDate));
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

        String sDay = String.valueOf(day).length() == 1 ? ("0" + String.valueOf(day)) : String.valueOf(day);
        String sMonth = String.valueOf(month).length() == 1 ? ("0" + String.valueOf(month)) : String.valueOf(month);
        String sHour = String.valueOf(hour).length() == 1 ? ("0" + String.valueOf(hour)) : String.valueOf(hour);
        String sMinute = String.valueOf(minute).length() == 1 ? ("0" + String.valueOf(minute)) : String.valueOf(minute);
        String sSecond = String.valueOf(seconds).length() == 1 ? ("0" + String.valueOf(seconds)) : String.valueOf(seconds);

        String sf = String.format("%s/%s/%s %s:%s:%s", sDay, sMonth, year, sHour, sMinute, sSecond);
        return sf.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProcessing();
        userRepo.getBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    this.balance = balance;
                    preferencesManager.setBalance(balance.getBalance());
                    tvBalance.setText(CurrencyUtil.formatVNDecimal(balance.getBalance()));
                });
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

    private void payByVoucher() {
        RxUtils.checkNetWork(PaypalActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_server_maintanance));
                    } else {
                        OrderBaseRequest request = new OrderBaseRequest(getCartParams(),
                                Integer.valueOf(preferencesManager.getTableInfo().table_number),
                                prepareDescriptionParams());
                        orderRepo.makeOrderByVoucher(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnError(e -> ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e)))
                                .doOnSubscribe(() -> showMProcessing(null))
                                .doOnTerminate(() -> hideMProcessing())
                                .subscribe(statusResponse -> {
                                    showDialogInform();
                                });
                    }
                });
    }

    private void payByCash() {
        RxUtils.checkNetWork(PaypalActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_server_maintanance));
                    } else {
                        OrderBaseRequest request = new OrderBaseRequest(getCartParams(),
                                Integer.valueOf(preferencesManager.getTableInfo().table_number),
                                prepareDescriptionParams());
                        orderRepo.makeOrderByCash(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnError(e -> ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e)))
                                .doOnSubscribe(() -> showMProcessing(null))
                                .doOnTerminate(() -> hideMProcessing())
                                .subscribe(statusResponse -> {
                                    showDialogInform();
                                });
                    }
                });
    }

    private String BLANK_CHAR = "@blank@";

    private List<DescriptionRequest> prepareDescriptionParams() {
        List<DescriptionRequest> descriptionRequests = new ArrayList<>();
        Map<Integer, List<String>> des = cartManager.getAllDEscription();
        for (Map.Entry<Integer, List<String>> item : des.entrySet()) {
            int dishId = item.getKey();
            List<String> desItem = item.getValue();
            DescriptionRequest requestItem = new DescriptionRequest(dishId, desItem);
            descriptionRequests.add(requestItem);
        }
        return descriptionRequests;
    }

    private void onOkPress() {
        RxUtils.checkNetWork(PaypalActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_server_maintanance));
                    } else {
                        CurrencyUtil.convertVNDToUSD(getTotal())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(() -> showMProcessing(null))
                                .doOnError(e -> ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e)))
                                .doOnTerminate(() -> hideMProcessing())
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
            if (VOUCHER_REQUEST_CODE == requestCode) {
                balance.setBalance(preferencesManager.getUser().getBalance());
                setBalance(preferencesManager.getUser().getBalance());
            } else if (null != data) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (null != confirmation) {
                    try {
                        String res = confirmation.toJSONObject().toString(4);
                        Gson gson = new Gson();
                        invoiceResp = gson.fromJson(res, PaypalInvoiceResp.class);
                        Log.i(LOG_TAG, invoiceResp.toString());
                        showMProcessing("Processing...");
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
        RxUtils.checkNetWork(PaypalActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_server_maintanance));
                    } else {
                        OrderPaypalRequest request = new OrderPaypalRequest(getCartParams(),
                                Integer.valueOf(preferencesManager.getTableInfo().table_number),
                                prepareDescriptionParams(), invoiceResp.getResponse().getId());
                        orderRepo.makeOrder(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnError(e -> ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e)))
                                .doOnSubscribe(() -> showMProcessing(null))
                                .doOnTerminate(() -> hideMProcessing())
                                .subscribe(orderCreateRes -> {
                                    orderCreateResponse = orderCreateRes;
                                    if (orderCreateRes.getDish().size() > 0) {
                                        showDialogConfirmContinue(orderCreateRes.getDish());
                                    } else {
                                        cartManager.clear();
                                        showDialogInform();
//                                        startActivity(CustomerActivity.newInstance(PaypalActivity.this));
                                    }

                                });
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
            cartManager.clear();
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

    public void showMProcessing(String title) {
        if (null != title) {
            mTvProcessingTitle.setText(title);
        } else {
            mTvProcessingTitle.setText(R.string.text_processing);
        }
        mRlProcessing.setVisibility(View.VISIBLE);
    }

    public void hideMProcessing() {
        mRlProcessing.setVisibility(View.GONE);
    }

    private void showDialogConfirmContinue(List<Dish> infoItemList) {
        hideMProcessing();
        float minusTotal = 0F;
        for (Dish dish : infoItemList) {
            int quantity = cartManager.getQuantityById(dish.getId());
            minusTotal += dish.getPrice() * quantity;
        }

        int updatedQuantity = getItemTotal() - infoItemList.size();
        float updatedAmount = getTotal() - minusTotal;

        FragmentManager fm = getSupportFragmentManager();
        PaypalInfoDialog dialog = PaypalInfoDialog.newInstance(infoItemList);
        dialog.setCancelable(false);
        float finalMinusTotal = minusTotal;
        dialog.setListener(new PaypalInfoDialog.PaypalListener() {
            @Override
            public void onOkieClick(List<Dish> items) {
                RxUtils.checkNetWork(PaypalActivity.this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isAvailable -> {
                            if (-1 == isAvailable) {
                                ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_not_available_network));
                            } else if (-2 == isAvailable) {
                                ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_server_maintanance));
                            } else {
                                // tiep tuc va tru tien mon khong phuc vu
                                tvAmount.setText(CurrencyUtil.formatVNDecimal(updatedAmount));
                                tvItemQuantity.setText(String.valueOf(updatedQuantity) + " "
                                        + (updatedQuantity <= 1 ? getString(R.string.text_info_dish) : getString(R.string.text_info_dishes)));
                                // Convert VND -> USD
                                CurrencyUtil.convertVNDToUSD(finalMinusTotal)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSubscribe(() -> showProcessingRollBack())
                                        .doOnError(e -> {
                                            hideProcessingRollBack();
                                            ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                        })
                                        .doOnError(e -> {
                                            hideProcessingRollBack();
                                            ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                        })
                                        .subscribe(usdMoney -> {
                                            // Get Paypal 's Credential
                                            PaypalServices.getCredential()
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .doOnError(e -> {
                                                        hideProcessingRollBack();
                                                        ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                    })
                                                    .subscribe(credential -> {
                                                        // Get payment ID
                                                        PaypalServices.getDetailByPaymentId(invoiceResp.getResponse().getId(), credential.getAccessToken())
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .doOnError(e -> {
                                                                    hideProcessingRollBack();
                                                                    ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                                })
                                                                .flatMap(new Func1<PaypalDetailResponse, Observable<PartialRefundResponse>>() {
                                                                    @Override
                                                                    public Observable<PartialRefundResponse> call(PaypalDetailResponse detail) {
                                                                        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                                                                        DecimalFormat df = (DecimalFormat) nf;
                                                                        df.applyPattern("#.##");
                                                                        PaypalPartialReq req = new PaypalPartialReq();
                                                                        Amount amount = new Amount();
                                                                        amount.setCurrency("USD");
                                                                        String total = String.valueOf(df.format(usdMoney));
                                                                        if (Character.toString(total.charAt(total.length() - 2)).equals(".")) {
                                                                            total = total.concat("0");
                                                                        }
                                                                        Log.d("Hoang", "total: " + total);
                                                                        amount.setTotal(total);
                                                                        req.amount = amount;

                                                                        float standardMoney;
                                                                        standardMoney = Float.valueOf(df.format(usdMoney));
                                                                        Log.d("Hoang", "total float: " + standardMoney);
                                                                        return PaypalServices.partialRefund(detail.getTransactions().get(0).getRelatedResource().get(0).getSale().getId(),
                                                                                credential.getAccessToken(), total)
                                                                                .subscribeOn(Schedulers.io());
                                                                    }
                                                                })
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .doOnError(e -> {
                                                                    hideProcessingRollBack();
                                                                    ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                                })
                                                                .subscribe(res -> {
                                                                    StringBuilder dishList = new StringBuilder();
                                                                    for (Dish d : infoItemList) {
                                                                        dishList.append(String.valueOf(d.getId()))
                                                                                .append("_");
                                                                    }
                                                                    orderRepo.partialRefund(orderCreateResponse.getOrderId(),
                                                                            finalMinusTotal, dishList.deleteCharAt(dishList.length() - 1).toString())
                                                                            .subscribeOn(Schedulers.io())
                                                                            .observeOn(AndroidSchedulers.mainThread())
                                                                            .doOnError(e -> {
                                                                                hideProcessingRollBack();
                                                                                ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                                            })
                                                                            .doOnTerminate(() -> hideProcessingRollBack())
                                                                            .subscribe(new Subscriber<StatusResponse>() {
                                                                                @Override
                                                                                public void onCompleted() {
                                                                                    hideProcessingRollBack();
                                                                                }

                                                                                @Override
                                                                                public void onError(Throwable e) {
                                                                                    hideProcessingRollBack();
                                                                                }

                                                                                @Override
                                                                                public void onNext(StatusResponse partialRefundResponse) {
                                                                                    cartManager.clear();
                                                                                    startActivity(CustomerActivity.newInstance(PaypalActivity.this));
                                                                                    hideProcessingRollBack();
                                                                                }
                                                                            });
                                                                });
                                                    });
                                        });
                            }
                        });
            }

            @Override
            public void onAngryClick() {
                RxUtils.checkNetWork(PaypalActivity.this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isAvailable -> {
                            if (-1 == isAvailable) {
                                ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_not_available_network));
                            } else if (-2 == isAvailable) {
                                ToastUtils.toastLongMassage(PaypalActivity.this, getString(R.string.text_server_maintanance));
                            } else {
                                tvAmount.setText(CurrencyUtil.formatVNDecimal(0));
                                tvItemQuantity.setText(String.valueOf(0) + " "
                                        + (updatedQuantity <= 1 ? getString(R.string.text_info_dish) : getString(R.string.text_info_dishes)));
                                PaypalServices.getCredential()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSubscribe(() -> showProcessingRollBack())
                                        .doOnError(e -> {
                                            hideProcessingRollBack();
                                            ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                        })
                                        .subscribe(credential -> {
                                            PaypalServices.getDetailByPaymentId(invoiceResp.getResponse().getId(), credential.getAccessToken())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .doOnError(e -> {
                                                        hideProcessingRollBack();
                                                        ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                    })
                                                    .flatMap(new Func1<PaypalDetailResponse, Observable<FullRefundResponse>>() {
                                                        @Override
                                                        public Observable<FullRefundResponse> call(PaypalDetailResponse detail) {
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
                                                    .doOnError(e -> {
                                                        hideProcessingRollBack();
                                                        ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                    })
                                                    .subscribe(result -> {
                                                        orderRepo.fullyRefund(orderCreateResponse.getOrderId())
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .doOnError(e -> {
                                                                    hideProcessingRollBack();
                                                                    ToastUtils.toastLongMassage(PaypalActivity.this, RetrofitUtils.getMessageError(PaypalActivity.this, e));
                                                                })
                                                                .doOnTerminate(() -> hideProcessingRollBack())
                                                                .subscribe(new Subscriber<StatusResponse>() {
                                                                    @Override
                                                                    public void onCompleted() {
                                                                        hideProcessingRollBack();
                                                                    }

                                                                    @Override
                                                                    public void onError(Throwable e) {
                                                                        hideProcessingRollBack();
                                                                    }

                                                                    @Override
                                                                    public void onNext(StatusResponse statusResponse) {
                                                                        cartManager.clear();
                                                                        startActivity(CustomerActivity.newInstance(PaypalActivity.this));
                                                                        hideProcessingRollBack();
                                                                    }
                                                                });
                                                    });
                                        });
                            }
                        });
            }
        });
        dialog.show(fm, "Paypal");
    }

    private void showProcessingRollBack() {
        rlProcessingRollBack.setVisibility(View.VISIBLE);
    }

    private void hideProcessingRollBack() {
        rlProcessingRollBack.setVisibility(View.GONE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper
                .wrap(newBase,
                        Utils.getLanguage(newBase)));
    }
}


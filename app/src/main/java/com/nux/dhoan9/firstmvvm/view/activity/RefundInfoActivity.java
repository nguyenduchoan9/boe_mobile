package com.nux.dhoan9.firstmvvm.view.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.response.AfterRefundNotification;
import com.nux.dhoan9.firstmvvm.data.response.NotificationResponse;
import com.nux.dhoan9.firstmvvm.manager.GCMIntentService;
import com.nux.dhoan9.firstmvvm.manager.GCMRegistrationIntentService;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.ParcelDishList;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.adapter.DishNotServeAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.DishRefundInfoAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RefundInfoActivity extends AppCompatActivity {
    @BindView(R.id.tvBillDate)
    TextView tvBillDate;
    @BindView(R.id.tvTableNumber)
    TextView tvTableNumber;
    @BindView(R.id.tvRefund)
    TextView tvRefund;
    @BindView(R.id.rvListNotServe)
    RecyclerView rvListNotServe;
    @BindView(R.id.btnBackToMenu)
    Button btnBackToMenu;
    DishRefundInfoAdapter adapter;

    AfterRefundNotification info;
    private BroadcastReceiver mBroadcastReceiver;
    @Inject
    PreferencesManager preferencesManager;
    @Inject
    UserRepo userRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDependency();
        Utils.handleSelectLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_info);
        setBroadcastReceiver();
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        info = b.getParcelable(Constant.LIST_DISH_NOT_SERVE);
        if (null != info) {
//            dishes = object.getDishList();
            Log.d("pac pac", String.valueOf(info.getDishes().size()));
        } else {
            finish();
        }
        btnBackToMenu.setOnClickListener(v -> naviToMenu());

        initializeInfo();
        configRecyclerView();
    }

    private void initDependency() {
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
    }

    private void initializeInfo() {
        tvBillDate.setText(info.getBillDate());
        tvTableNumber.setText(String.valueOf(info.getTableNumber()));
        tvRefund.setText(CurrencyUtil.formatVNDecimal(getTotal()));
    }
    private float getTotal(){
        float total = 0;
        for (AfterRefundNotification.DishAndQuantity d : info.getDishes()){
            total += d.getPrice() * d.getQuantity();
        }
        return total;
    }

    private void configRecyclerView() {
        adapter = new DishRefundInfoAdapter(this);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvListNotServe.setLayoutManager(manager);
        rvListNotServe.addItemDecoration(decoration);
        rvListNotServe.setAdapter(adapter);
        adapter.setData(info.getDishes());
    }

    private void naviToMenu() {
        Intent i = new Intent(this, CustomerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    protected void setBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    RxUtils.checkNetWork(RefundInfoActivity.this)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isAvailable -> {
                                if (-1 == isAvailable) {
                                    ToastUtils.toastLongMassage(RefundInfoActivity.this, getString(R.string.text_not_available_network));
                                } else if (-2 == isAvailable) {
                                    ToastUtils.toastLongMassage(RefundInfoActivity.this, getString(R.string.text_server_maintanance));
                                } else {
                                    userRepo.registerRegToken(token)
                                            .compose(RxUtils.onProcessRequest())
                                            .subscribe(new Subscriber<NotificationResponse>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    ToastUtils.toastLongMassage(RefundInfoActivity.this, RetrofitUtils.getMessageError(RefundInfoActivity.this, e));
                                                }

                                                @Override
                                                public void onNext(NotificationResponse subscribe) {
                                                    if (true == subscribe.status) {
                                                        Log.v("GCM-register", "Success");
                                                    } else {
                                                        Log.v("GCM-register", "fail");
                                                    }
                                                }
                                            });
                                }
                            });
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(RefundInfoActivity.this,
                            "GCM registration error");
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_DINER)) {
                    String message = intent.getStringExtra("body");
                    Log.d("fucking cool", intent.getStringExtra("body"));
                    if (message.length() > 0) {
                        Gson gson = new Gson();
                        List<Dish> dishes = gson.fromJson(message, new TypeToken<List<Dish>>() {
                        }.getType());
                        notifyDishNotServe(dishes);
                    }
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_DINER_REFUND)) {
                    String message = intent.getStringExtra("body");
                    Log.d("fucking cool refund", intent.getStringExtra("body"));
                    if (message.length() > 0) {
                        Gson gson = new Gson();
                        AfterRefundNotification bo = gson.fromJson(message, AfterRefundNotification.class);
                        notifyRefundSuccess(bo);
                    }
                } else {
//                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
                }
            }
        };

        Intent intent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_DINER));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_DINER_REFUND));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    private final int NOTIFY_DINER = 2;
    private ArrayList<Dish> notificationdishes = new ArrayList<>();

    private void notifyDishNotServe(List<Dish> dishes) {
        notificationdishes.addAll(dishes);
        ParcelDishList parcel = new ParcelDishList();
        parcel.setDishList(notificationdishes);
        Intent intent = new Intent(this, ListDishNotServeActivity.class);
        intent.putExtra(Constant.LIST_DISH_NOT_SERVE, parcel);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        int requestID = (int) System.currentTimeMillis();
        Log.d("Hoang", "notifyDishNotServe: " + requestID);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setContentTitle(getString(R.string.text_info_notification_annoucement))
                .setContentText(getString(R.string.text_info_notification_description))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 500, 500)
                .setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(NOTIFY_DINER, mBuilder.build());
    }

    private final int NOTIFY_DINER_REFUND = 4;

    private void notifyRefundSuccess(AfterRefundNotification response) {
        Intent intent = new Intent(this, RefundInfoActivity.class);
        intent.putExtra(Constant.LIST_DISH_NOT_SERVE, response);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        int requestID = (int) System.currentTimeMillis();
        Log.d("Hoang", "notifyRefundSuccess: " + requestID);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setContentTitle(getString(R.string.text_info_notification_annoucement))
                .setContentText(getString(R.string.text_notification_refund))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 500, 500)
                .setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(NOTIFY_DINER_REFUND, mBuilder.build());
    }
}

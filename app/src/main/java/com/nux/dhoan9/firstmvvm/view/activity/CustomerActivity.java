package com.nux.dhoan9.firstmvvm.view.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.CartDishAvailable;
import com.nux.dhoan9.firstmvvm.databinding.ActivityCustomerBinding;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.GCMIntentService;
import com.nux.dhoan9.firstmvvm.manager.GCMRegistrationIntentService;
import com.nux.dhoan9.firstmvvm.manager.LocalServices;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.ParcelDishList;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.view.fragment.CutleryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.DrinkingFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.EndpointDialogFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.HistoryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.OrderFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CustomerActivity extends BaseActivity {
    private final static String GCM_TOKEN = "GCM_TOKEN";
    private final static String LOG_TAG = "CUSTOMER";
    private BroadcastReceiver mBroadcastReceiver;

    private NavigationBottom navigationBottom;
    private final int CUTLERY_POS = 0;
    private final int DRINKING_POS = 1;
    private final int ORDER_POS = 2;
    private final int HISTORY_POS = 3;
    private FloatingNavigationView fnv;
    private RelativeLayout navigationContainer;
    private Toolbar toolbar;
    private SearchView svSearch;
    private TextView tvTotal;
    private TextView tvContinues;

    private String searchQuery = "";
    @Inject
    PreferencesManager preferencesManager;
    @Inject
    UserRepo userRepo;
    @Inject
    CartManager cartManager;
    @Inject
    OrderRepo orderRepo;
    @Inject
    DishRepo dishRepo;
    CutleryFragment cutleryFragment = new CutleryFragment();
    DrinkingFragment drinkingFragment = new DrinkingFragment();
    OrderFragment orderFragment = new OrderFragment();
    HistoryFragment historyFragment = new HistoryFragment();

    private int fragmentPos;

    private ActivityCustomerBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, CustomerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer);
        initDependency();
        super.onCreate(savedInstanceState);
        setBroadcastReceiver();
        initDrawer();
        initView();
        startService(new Intent(getBaseContext(), LocalServices.class));
    }

    private String KEY_SAVE_INSTANCE = "KEY_SAVE_INSTANCE";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SAVE_INSTANCE, fragmentPos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getInt(KEY_SAVE_INSTANCE, 0);
    }

    protected void setBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    userRepo.registerRegToken(token)
                            .compose(RxUtils.onProcessRequest())
                            .subscribe(subscribe -> {
                                if (true == subscribe.status) {
                                    Log.v("GCM-register", "Success");
                                } else {
                                    Log.v("GCM-register", "fail");
                                }
                            });
                    Log.v(GCM_TOKEN, token);
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(CustomerActivity.this,
                            "GCM registration error");
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_DINER)) {
                    String message = intent.getStringExtra("body");
                    Log.d("fucking cool", intent.getStringExtra("body"));
                    if (message.length() > 0) {
                        Gson gson = new Gson();
                        List<Dish> dishes = gson.fromJson(message, new TypeToken<List<Dish>>() {
                        }.getType());

                        ToastUtils.toastLongMassage(CustomerActivity.this,
                                dishes.size() + "");
                        notifyDishNotServe(dishes);
                    }

                } else {
                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
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
        showToolBar(fragmentPos);
    }

    private void showToolBar(int pos) {
        if (CUTLERY_POS == pos) {
            setDishToolBar();
            clearSearchBox();
        } else if (DRINKING_POS == pos) {
            setDishToolBar();
        } else if (ORDER_POS == pos) {
            setOrderToolBar();
        } else if (HISTORY_POS == pos) {
            hideToolbar();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void setProcessing() {
        rlProcessing = binding.actionBarContent.content.processingContainer.rlProcessing;
        tvProcessingTitle = binding.actionBarContent.content.processingContainer.tvProcessingTitle;
    }

    private void initDrawer() {
        initToolbar();
        initDrawerMenu();
    }

    private void initDrawerMenu() {

    }

    private void initToolbar() {
        toolbar = binding.actionBarContent.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        svSearch = (SearchView) toolbar.findViewById(R.id.svSearch);
        initSearchView();
        tvTotal = (TextView) toolbar.findViewById(R.id.tvTotal);
        tvContinues = (TextView) toolbar.findViewById(R.id.tvContinues);
        binding.navView.inflateMenu(R.menu.drawer_menu_customer);
        binding.navView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return false;
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawer,
                binding.actionBarContent.toolbar, R.string.open, R.string.close);
        binding.drawer.addDrawerListener(actionBarDrawerToggle);
    }

    private void initSearchView() {
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) {
                    return true;
                }
                hideNoSearchResult();
                svSearch.clearFocus();
                searchQuery = query;
                handleQuerySearchSubmit(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void handleQuerySearchSubmit(String keySearch) {
        if (fragmentPos == CUTLERY_POS) {
            cutleryFragment.onSearchSubmit(keySearch);
        } else if (fragmentPos == DRINKING_POS) {
            drinkingFragment.onSearchSubmit(keySearch);
        } else if (fragmentPos == HISTORY_POS) {

        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(ProfileActivity.newInstance(this));
                break;
            case R.id.logOut:
                logout();
                break;
            case R.id.apiEndpoint:
                showEndpointDialog();
                break;
            case R.id.payment:
                startActivity(PaypalActivity.newInstance(this));
                break;
            default:
                break;
        }
        binding.drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void initDependency() {
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
    }

    private void initView() {
        navigationContainer = binding.actionBarContent.content.bottomNavigationContent.navigationContainer;
        navigationBottom = binding.actionBarContent.content.bottomNavigationContent.bottomNavigation;
//        navigationBottom.showOrderBadge(12);
//        navigationBottom.showHisrotyBadge(12);
        initContent();
        navigationBottom.setPress(fragmentPos);
        navigationBottom.setListener(new NavigationBottom.NavigationListener() {
            @Override
            public void onCutleryClick() {
                if (fragmentPos == CUTLERY_POS) {
                    return;
                }
                showFragmentPosition(CUTLERY_POS);
            }

            @Override
            public void onDrinkingClick() {
                if (fragmentPos == DRINKING_POS) {
                    return;
                }
                showFragmentPosition(DRINKING_POS);
            }

            @Override
            public void onOrderClick() {
                if (fragmentPos == ORDER_POS) {
                    return;
                }
                showFragmentPosition(ORDER_POS);
            }

            @Override
            public void onHistoryClick() {
                if (fragmentPos == HISTORY_POS) {
                    return;
                }
                showFragmentPosition(HISTORY_POS);
            }
        });

        onContinuesClick();
        orderFragment.isCheckAvailable
                .takeUntil(destroyView)
                .subscribe(isAvailable ->
                        tvContinues.setTextColor(isAvailable ?
                                ContextCompat.getColor(CustomerActivity.this, R.color.white) :
                                ContextCompat.getColor(CustomerActivity.this, R.color.colorTextNavigation))
                );
    }

    private void logout() {
        userRepo.logout(preferencesManager.getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    preferencesManager.logOut();
                    startActivity(LoginActivity.newInstance(this));
                });
    }

    private void initContent() {
        replaceContent(cutleryFragment, false, "CutleryFragment");
        replaceContent(drinkingFragment, false, "DrinkingFragment");
        replaceContent(orderFragment, false, "OrderFragment");
        replaceContent(historyFragment, false, "HistoryFragment");
        showFragmentPosition(fragmentPos = 0);
        setDishToolBar();
    }

    private void replaceContent(Fragment fragment, boolean exist, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (exist) {
            ft.show(fragment);
        } else {
            ft.add(R.id.frContent, fragment, tag);
        }
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void showFragmentPosition(int pos) {
        cutleryFragment.hideNoSearchResult();
        drinkingFragment.hideNoSearchResult();
        if (CUTLERY_POS == pos) {
            showFragment(cutleryFragment);
            setDishToolBar();
            clearSearchBox();
            cutleryFragment.synTheCart();
//            cutleryFragment.clearSearchKey();
            cutleryFragment.onResume();
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (DRINKING_POS == pos) {
            showFragment(drinkingFragment);
            setDishToolBar();
            clearSearchBox();
            drinkingFragment.synTheCart();
//            drinkingFragment.clearSearchKey();
            drinkingFragment.onResume();
            hideFragment(cutleryFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (ORDER_POS == pos) {
            showFragment(orderFragment);
            orderFragment.onStart();
            setOrderToolBar();
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(historyFragment);
        } else if (HISTORY_POS == pos) {
            showFragment(historyFragment);
            historyFragment.onStart();
            hideToolbar();
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
        }

        fragmentPos = pos;
    }

    public RelativeLayout getNavigationBottom() {
        return navigationContainer;
    }

    private void showEndpointDialog() {
        EndpointDialogFragment.newInstance()
                .show(getSupportFragmentManager(),
                        EndpointDialogFragment.class.getSimpleName());
    }

    public void setTotalOrder(String total) {
        tvTotal.setText(CurrencyUtil.formatVNDecimal(Float.valueOf(total)));
    }

    public void setOrderToolBar() {
        binding.actionBarContent.rlOrderInfo.setVisibility(View.VISIBLE);
        svSearch.setVisibility(View.GONE);
    }

    public void setDishToolBar() {
        binding.actionBarContent.rlOrderInfo.setVisibility(View.GONE);
        svSearch.setVisibility(View.VISIBLE);
    }

    public void performClickContinues() {
        tvContinues.performClick();
    }

    public void onContinuesClick() {
        tvContinues.setOnClickListener(v -> {
            float total = orderFragment.getTotalPayment();
            if (0F == total) {
                return;
            }
            orderRepo.isAvailable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(() -> showProcessing("Processing..."))
                    .doOnTerminate(() -> hideProcessing())
                    .flatMap(new Func1<CanOrder, Observable<List<CartDishAvailable>>>() {
                        @Override
                        public Observable<List<CartDishAvailable>> call(CanOrder canOrder) {
                            if (canOrder.isAvailable()) {
                                return dishRepo.checkDishCartAvailable(getIdDishInCart())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                            return null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(dishNotAvailable -> {
                        if (null == dishNotAvailable) {
                            orderFragment.showrRlHourOver();
                        } else if (0 == dishNotAvailable.size()) {
                            navToPayment();
                        } else if (0 < dishNotAvailable.size()) {
                            orderFragment.showDialog(dishNotAvailable);
                        }
                    });

        });
    }

    public void navToPayment() {
        float total = orderFragment.getTotalPayment();
        Intent i = new Intent(this, PaypalActivity.class);
        i.putExtra(Constant.KEY_TOTAL_PAYMENT, total);
        i.putExtra(Constant.KEY_ORDER_NAME, "Hello");
        i.putExtra(Constant.KEY_ORDER_ITEM_TOTAL, orderFragment.getItemtotal());

        startActivity(i);
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    private void clearSearchBox() {
        svSearch.setQuery("", false);
        svSearch.clearFocus();
        svSearch.setIconified(true);
    }

    private void hideToolbar() {
        binding.actionBarContent.rlOrderInfo.setVisibility(View.GONE);
        svSearch.setVisibility(View.GONE);
    }

    public void showOrderFragment() {
        navigationBottom.setPress(ORDER_POS);
        showFragmentPosition(ORDER_POS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    private String getIdDishInCart() {
        StringBuilder ids = new StringBuilder();
        for (Map.Entry<Integer, Integer> cartItem : cartManager.getCart().entrySet()) {
            ids.append(String.valueOf(cartItem.getKey()))
                    .append("_");
        }
        return (ids.length() == 0) ? "" : ids.deleteCharAt(ids.length() - 1).toString();
    }

    public void notifyCartChange() {
        cutleryFragment.notifyChange();
        drinkingFragment.notifyChange();
    }

    public void showProgressAndDisableTouch() {
        showProcessing("Đang tìm kiếm...");
        navigationBottom.isCanHanle = false;
    }

    public void hideProgressAndEnableTouch() {
        hideProcessing();
        navigationBottom.isCanHanle = true;
    }

    public void showNoSearchResult() {
        binding.actionBarContent.content.rlNoResult.setVisibility(View.VISIBLE);
    }

    public void hideNoSearchResult() {
        binding.actionBarContent.content.rlNoResult.setVisibility(View.GONE);
    }

    public void setSearchKeyInBar(String key) {
        int searchPlateId = svSearch.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = svSearch.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(Color.TRANSPARENT);
        }
        if ("".equals(key)) {
            svSearch.setIconifiedByDefault(true);
        } else {
            svSearch.setIconifiedByDefault(false);
            svSearch.setQuery(key, false);
        }
//        svSearch.clearFocus();
    }

    private final int NOTIFY_DINER = 0;
    private ArrayList<Dish> notificationdishes = new ArrayList<>();

    private void notifyDishNotServe(List<Dish> dishes) {
        notificationdishes.addAll(dishes);
        ParcelDishList parcel = new ParcelDishList();
        parcel.setDishList(notificationdishes);
        Intent intent = new Intent(this, ListDishNotServeActivity.class);
        intent.putExtra(Constant.LIST_DISH_NOT_SERVE, parcel);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setContentTitle("Nhà hàng BOE trân trọng thông báo")
                .setContentText("Hiện tại chúng tôi không còn phục vụ...")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 500, 500)
                .addAction(R.drawable.ic_notification_bell, "Ignore", null)
                .setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(NOTIFY_DINER, mBuilder.build());
    }

//    private void setFlagNotification(boolean flag) {
//        ((BoeApplication) getApplication()).setOpenNotification(flag);
//    }
//
//    private boolean isFlagNotification() {
//        ((BoeApplication) getApplication()).isOpenNotification();
//    }
}

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
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nux.dhoan9.firstmvvm.data.request.PaypalPartialReq;
import com.nux.dhoan9.firstmvvm.data.response.AfterRefundNotification;
import com.nux.dhoan9.firstmvvm.data.response.Amount;
import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.CartDishAvailable;
import com.nux.dhoan9.firstmvvm.data.response.FullRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.NotificationResponse;
import com.nux.dhoan9.firstmvvm.data.response.PartialRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalDetailResponse;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.databinding.ActivityCustomerBinding;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.GCMIntentService;
import com.nux.dhoan9.firstmvvm.manager.GCMRegistrationIntentService;
import com.nux.dhoan9.firstmvvm.manager.LocalServices;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.DishSugesstion;
import com.nux.dhoan9.firstmvvm.model.OrderCreateResponse;
import com.nux.dhoan9.firstmvvm.model.ParcelDishList;
import com.nux.dhoan9.firstmvvm.model.SuggestionByCategory;
import com.nux.dhoan9.firstmvvm.services.PaypalServices;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.adapter.DishSugesstionAdapter;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.view.fragment.CutleryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.DrinkingFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.EndpointDialogFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.HistoryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.LanguageDialog;
import com.nux.dhoan9.firstmvvm.view.fragment.OrderFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.PaypalInfoDialog;
import com.nux.dhoan9.firstmvvm.view.fragment.QRCodeFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.RatingDialog;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CustomerActivity extends BaseActivity {
    private final static String GCM_TOKEN = "GCM_TOKEN";
    private final static String LOG_TAG = "CUSTOMER";
    private BroadcastReceiver mBroadcastReceiver;
    private boolean isSearchModeInCutlery = false;
    private boolean isSearchModeInDrinking = false;

    private NavigationBottom navigationBottom;
    private final int CUTLERY_POS = 0;
    private final int DRINKING_POS = 1;
    private final int ORDER_POS = 2;
    private final int HISTORY_POS = 3;
    private FloatingNavigationView fnv;
    private RelativeLayout navigationContainer, rlOverlay;
    private Toolbar toolbar;
    private SearchView svSearch;
    private TextView tvTotal;
    private TextView tvContinues, tvNoSuggestResult;
    private RecyclerView rvSuggestion;
    private DishSugesstionAdapter suggestionAdapter;
    private String searchQuery = "";
    private boolean isSwitchScreen = false;
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
    private List<DishSugesstion> dishSuggest;
    private int fragmentPos = -1;

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
        Utils.handleSelectLanguage(this, preferencesManager.getLanguage());
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
                    RxUtils.checkNetWork(CustomerActivity.this)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isAvailable -> {
                                if (-1 == isAvailable) {
                                    ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_not_available_network));
                                } else if (-2 == isAvailable) {
                                    ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_server_maintanance));
                                } else {
                                    userRepo.registerRegToken(token)
                                            .compose(RxUtils.onProcessRequest())
                                            .subscribe(new Subscriber<NotificationResponse>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    ToastUtils.toastLongMassage(CustomerActivity.this, RetrofitUtils.getMessageError(CustomerActivity.this, e));
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
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_DINER_CASH_PEDING)) {
                    String message = intent.getStringExtra("body");
                    Log.d("fucking cool refund", intent.getStringExtra("body"));
                    if (message.length() > 0) {
                        Gson gson = new Gson();
                        OrderCreateResponse bo = gson.fromJson(message, OrderCreateResponse.class);
                        notifyAfterPayedByCashMaterialOut(bo);
                    }
                } else {
//                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
                }
            }
        };

        Intent intent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(intent);
    }

    private void notifyAfterPayedByCashMaterialOut(OrderCreateResponse bo) {
        FragmentManager fm = getSupportFragmentManager();
        PaypalInfoDialog dialog = PaypalInfoDialog.newInstance(bo.getDish());
        dialog.setCancelable(false);
        dialog.setListener(new PaypalInfoDialog.PaypalListener() {
            @Override
            public void onOkieClick(List<Dish> items) {
                RxUtils.checkNetWork(CustomerActivity.this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isAvailable -> {
                            if (-1 == isAvailable) {
                                ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_not_available_network));
                            } else if (-2 == isAvailable) {
                                ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_server_maintanance));
                            } else {
                                orderRepo.partialRefundInCash(bo.getOrderId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<StatusResponse>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(StatusResponse statusResponse) {

                                            }
                                        });
                            }
                        });
            }

            @Override
            public void onAngryClick() {
                RxUtils.checkNetWork(CustomerActivity.this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isAvailable -> {
                            if (-1 == isAvailable) {
                                ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_not_available_network));
                            } else if (-2 == isAvailable) {
                                ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_server_maintanance));
                            } else {
                                orderRepo.fullRefundInCash(bo.getOrderId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<StatusResponse>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(StatusResponse statusResponse) {

                                            }
                                        });
                            }
                        });
            }
        });
        dialog.show(fm, "Paypal");
    }

    @Override
    protected void onStart() {
        if (preferencesManager.isTableQRCodeExpire()) {
            Intent i = new Intent(this, QRCodeActivity.class);
            i.putExtra(QRCodeActivity.QR_CODE_EXPIRE_KEY, true);
            startActivity(i);
        }
        super.onStart();
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
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_DINER_CASH_PEDING));
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
        initSuggestionContainer();
    }

    private boolean isInitFirstTime = true;

    private void initSuggestionContainer() {
        tvNoSuggestResult = binding.actionBarContent.tvNoResult;
        rvSuggestion = binding.actionBarContent.rvSuggestion;

        suggestionAdapter = new DishSugesstionAdapter(this);
        suggestionAdapter.setListener(new DishSugesstionAdapter.FilterResultListener() {
            @Override
            public void onFilterSuccess() {
                Log.d("Hoang", "onFilterSuccess: ");
                tvNoSuggestResult.setVisibility(View.GONE);
                rvSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFilterNoting() {
                Log.d("Hoang", "onFilterNoting: ");
                if (isInitFirstTime) {
                    isInitFirstTime = false;
                    tvNoSuggestResult.setVisibility(View.GONE);
                    rvSuggestion.setVisibility(View.GONE);
                } else {
                    tvNoSuggestResult.setVisibility(View.VISIBLE);
                    rvSuggestion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemClick(int dishId, String dishName) {
                hideKeyboard();
                svSearch.setQuery(dishName, true);
            }
        });
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSuggestion.setLayoutManager(manager);
        rvSuggestion.addItemDecoration(dividerItemDecoration);
        rvSuggestion.setAdapter(suggestionAdapter);
        RxUtils.checkNetWork(CustomerActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_server_maintanance));
                    } else {
                        dishRepo.getSuggestedDish()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<SuggestionByCategory>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.toastLongMassage(CustomerActivity.this, RetrofitUtils.getMessageError(CustomerActivity.this, e));
                                    }

                                    @Override
                                    public void onNext(SuggestionByCategory results) {
                                        dishSuggest = new ArrayList<>();
                                        if (null != results) {
                                            if (null != results.getDish() && results.getDish().size() > 0) {
                                                dishSuggest.addAll(results.getDish());
                                            }
                                        }
                                        suggestionAdapter.setData(dishSuggest);
                                        tvNoSuggestResult.setVisibility(View.GONE);
                                    }
                                });
                    }
                });
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
        View headerLayout = binding.navView.getHeaderView(0);
        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        tvUsername.setText(preferencesManager.getUser().getUsername());
        TextView tvTableNo = (TextView) headerLayout.findViewById(R.id.tvTableNumber);
        tvTableNo.setText(getString(R.string.text_nav_header_layout_table)
                + preferencesManager.getTableInfo().table_number);
        TextView tvBalance = (TextView) headerLayout.findViewById(R.id.tvBalance);
        tvBalance.setText(getString(R.string.text_balance) + CurrencyUtil.formatVNDecimal(preferencesManager.getUser().getBalance()));
        binding.navView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return false;
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawer,
                binding.actionBarContent.toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                userRepo.getBalance()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(balance -> setBalance(balance.getBalance()));
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        binding.drawer.addDrawerListener(actionBarDrawerToggle);
    }

    private void initSearchView() {
        int searchPlateId = svSearch.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = svSearch.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(Color.TRANSPARENT);
        }

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
                if (CUTLERY_POS == fragmentPos) {
                    isSearchModeInCutlery = true;
                } else if (DRINKING_POS == fragmentPos) {
                    isSearchModeInDrinking = true;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String term) {
                if (!isSwitchScreen) {
                    handleFilterOnChange(term);
                }
                return true;
            }
        });
        ImageView searchCloseButton = (ImageView) svSearch.findViewById(R.id.search_close_btn);
        searchCloseButton.setOnClickListener(v -> {
            if (CUTLERY_POS == fragmentPos) {
                isSearchModeInCutlery = false;
                rvSuggestion.setVisibility(View.GONE);
                tvNoSuggestResult.setVisibility(View.GONE);
                isSwitchScreen = true;
                clearSearchBox();
                isSwitchScreen = false;
                cutleryFragment.refreshMenu();
            } else if (DRINKING_POS == fragmentPos) {
                isSearchModeInDrinking = false;
                rvSuggestion.setVisibility(View.GONE);
                tvNoSuggestResult.setVisibility(View.GONE);
                isSwitchScreen = true;
                clearSearchBox();
                isSwitchScreen = false;
                drinkingFragment.refreshMenu();
            }
        });
        LinearLayout ll = binding.actionBarContent.rootViewCus;
        rlOverlay = binding.actionBarContent.content.rlOverlay;
        rlOverlay.setVisibility(View.GONE);
        ll.getViewTreeObserver().
                addOnGlobalLayoutListener(() ->
                {
                    Rect r = new Rect();
                    ll.getWindowVisibleDisplayFrame(r);
                    int screenHeight = ll.getRootView().getHeight();

                    int keypadHeight = screenHeight - r.bottom;

                    if (keypadHeight > screenHeight * 0.15) {
                        // keypad show
                        navigationBottom.setVisibility(View.GONE);
                        rlOverlay.setVisibility(View.VISIBLE);
                    } else {
                        // keypad hide
                        tvNoSuggestResult.setVisibility(View.GONE);
                        rvSuggestion.setVisibility(View.GONE);
                        navigationBottom.setVisibility(View.VISIBLE);
                        rlOverlay.setVisibility(View.GONE);
                    }
                });
        svSearch.clearFocus();
    }

    private void handleFilterOnChange(String term) {
        if (fragmentPos == CUTLERY_POS) {
            suggestionAdapter.getFilter().filter(term);
        } else if (fragmentPos == DRINKING_POS) {
            suggestionAdapter.getFilter().filter(term);
        }
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
//            case R.id.apiEndpoint:
//                showEndpointDialog();
//                break;
            case R.id.addVoucher:
                showAddVoucherDialog();
                break;
            case R.id.setting:
                startActivity(QRCodeActivity.newInstance(this));
                break;
            case R.id.language:
//                orderFragment.showDialogSelectLanguage(preferencesManager);
                showDialogSelectLanguage();
                break;
            case R.id.feedback:
//                orderFragment.showDialogSelectLanguage(preferencesManager);
                showDialogFeedback();
                break;
            default:
                break;
        }
        binding.drawer.closeDrawers();
    }

    private void showDialogFeedback() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        RatingDialog dialog = RatingDialog.newInstance();
        dialog.setListener((rate, feedback) -> {
            userRepo.postFeedBack(rate, feedback)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(this::showProgressAndDisableTouch)
                    .doOnTerminate(this::hideProcessing)
                    .subscribe(action -> {
                        ToastUtils.toastShortMassage(CustomerActivity.this, "Success");
                    });
        });
        dialog.show(fm, "rating");
    }

    final int VOUCHER_REQUEST_CODE = 204;

    private void showAddVoucherDialog() {
        Intent i = new Intent(CustomerActivity.this, VoucherActivity.class);

        startActivityForResult(i, VOUCHER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (VOUCHER_REQUEST_CODE == requestCode) {
                setBalance(preferencesManager.getUser().getBalance());
            }
        }
    }

    private void setBalance(float balance) {
        View headerLayout = binding.navView.getHeaderView(0);
        TextView tvBalance = (TextView) headerLayout.findViewById(R.id.tvBalance);
        tvBalance.setText(getString(R.string.text_balance) + CurrencyUtil.formatVNDecimal(balance));
    }

    private void showDialogSelectLanguage() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        LanguageDialog dialog = LanguageDialog.newInstance(preferencesManager.getLanguage());
        dialog.setmListener(new LanguageDialog.LanguageListener() {
            @Override
            public void onEnglishSelect() {
                preferencesManager.setLanguage(Constant.EN_LANGUAGE_STRING);
                refreshViewAfterChangeLanguage();
            }

            @Override
            public void onVNSelect() {
                preferencesManager.setLanguage(Constant.VI_LANGUAGE_STRING);
                refreshViewAfterChangeLanguage();
            }
        });
        dialog.show(fm, "putang");
    }

    private void refreshViewAfterChangeLanguage() {
        finish();
        startActivity(new Intent(this, CustomerActivity.class));
    }

    @Override
    public void onBackPressed() {
        rvSuggestion.setVisibility(View.GONE);
        tvNoSuggestResult.setVisibility(View.GONE);
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

        initContent();
        navigationBottom.setPress(fragmentPos);
        navigationBottom.setListener(new NavigationBottom.NavigationListener() {
            @Override
            public void onCutleryClick() {
                if (isSearchModeInCutlery && CUTLERY_POS == fragmentPos) {
                    isSearchModeInCutlery = false;
                    cutleryFragment.refreshMenu();
                    return;
                }
                if (fragmentPos == CUTLERY_POS) {
                    return;
                }
                showFragmentPosition(CUTLERY_POS);
            }

            @Override
            public void onDrinkingClick() {
                if (isSearchModeInDrinking && DRINKING_POS == fragmentPos) {
                    isSearchModeInDrinking = false;
                    drinkingFragment.refreshMenu();
                    return;
                }
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
        RxUtils.checkNetWork(CustomerActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_server_maintanance));
                    } else {
                        userRepo.logout(preferencesManager.getUser().getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Void>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.toastLongMassage(CustomerActivity.this, RetrofitUtils.getMessageError(CustomerActivity.this, e));
                                    }

                                    @Override
                                    public void onNext(Void aVoid) {
                                        preferencesManager.logOut();
                                        startActivity(LoginActivity.newInstance(CustomerActivity.this));
                                    }
                                });
                    }
                });

    }

    private void initContent() {
        replaceContent(cutleryFragment, false, "CutleryFragment");
        replaceContent(drinkingFragment, false, "DrinkingFragment");
        replaceContent(orderFragment, false, "OrderFragment");
        replaceContent(historyFragment, false, "HistoryFragment");
        showFragmentPosition(fragmentPos = 0);
        updateOrderBadge();
        hideOrderBadge();
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
        hideNoSearchResult();
        hideSearchDishNotServe();
        if (CUTLERY_POS == pos) {
            showFragment(cutleryFragment);
            setDishToolBar();
            isSwitchScreen = true;
            clearSearchBox();
            cutleryFragment.synTheCart();
//            cutleryFragment.clearSearchKey();
            cutleryFragment.onResume();
//            if (null != cutlerySuggest) {
//                suggestionAdapter.setData(cutlerySuggest);
//            }
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (DRINKING_POS == pos) {
            showFragment(drinkingFragment);
            setDishToolBar();
            isSwitchScreen = true;
            clearSearchBox();
            drinkingFragment.synTheCart();
//            drinkingFragment.clearSearchKey();
            drinkingFragment.onResume();
//            if (null != drinkingSuggest) {
//                suggestionAdapter.setData(drinkingSuggest);
//            }
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
            RxUtils.checkNetWork(CustomerActivity.this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isAvailable -> {
                        if (-1 == isAvailable) {
                            ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_not_available_network));
                        } else if (-2 == isAvailable) {
                            ToastUtils.toastLongMassage(CustomerActivity.this, getString(R.string.text_server_maintanance));
                        } else {
                            orderRepo.isAvailable()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(() -> showProcessing(getString(R.string.text_info_processing)))
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
                                    .subscribe(new Subscriber<List<CartDishAvailable>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.toastLongMassage(CustomerActivity.this, RetrofitUtils.getMessageError(CustomerActivity.this, e));
                                        }

                                        @Override
                                        public void onNext(List<CartDishAvailable> dishNotAvailable) {
                                            if (null == dishNotAvailable) {
                                                orderFragment.showrRlHourOver();
                                            } else if (0 == dishNotAvailable.size()) {
                                                navToPayment();
                                            } else if (0 < dishNotAvailable.size()) {
                                                orderFragment.showDialog(dishNotAvailable);
                                            }
                                        }
                                    });
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
        showProcessing(getString(R.string.text_info_searching));
        navigationBottom.isCanHanle = false;
    }

    public void hideProgressAndEnableTouch() {
        hideProcessing();
        navigationBottom.isCanHanle = true;
    }

    public void showNoSearchResult() {
        binding.actionBarContent.content.tvMessage.setText(getString(R.string.text_info_no_search_result));
        binding.actionBarContent.content.rlNoResult.setVisibility(View.VISIBLE);
    }

    public void showSearchDishNotServe() {
        binding.actionBarContent.content.tvMessage.setText(getString(R.string.text_search_dish_not_serve));
        binding.actionBarContent.content.rlNoResult.setVisibility(View.VISIBLE);
    }

    public void hideSearchDishNotServe() {
        binding.actionBarContent.content.tvMessage.setText(getString(R.string.text_search_dish_not_serve));
        binding.actionBarContent.content.rlNoResult.setVisibility(View.GONE);
    }

    public void hideNoSearchResult() {
        binding.actionBarContent.content.rlNoResult.setVisibility(View.GONE);
    }

    public void setSearchKeyInBar(String key, int from) {
        svSearch.setIconifiedByDefault(false);
        svSearch.setQuery(key, false);
        isSwitchScreen = false;
        if (CUTLERY_POS == from) {
            svSearch.setQueryHint(getString(R.string.text_info_search_cutlery));
        } else if (DRINKING_POS == from) {
            svSearch.setQueryHint(getString(R.string.text_info_search_drinking));
        } else {
            svSearch.setQueryHint(getString(R.string.text_search_default));
        }

        rvSuggestion.setVisibility(View.GONE);
        tvNoSuggestResult.setVisibility(View.GONE);
        svSearch.clearFocus();
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
    private ArrayList<AfterRefundNotification.DishAndQuantity> abc = new ArrayList<>();

    private void notifyRefundSuccess(AfterRefundNotification response) {
        abc.addAll(response.getDishes());
        response.setDishes(abc);
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

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(svSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.d("Hoang", "attachBaseContext: " + Utils.getLanguage(newBase));
        super.attachBaseContext(MyContextWrapper
                .wrap(newBase,
                        Utils.getLanguage(newBase)));
    }

    public void updateOrderBadge() {
        navigationBottom.showOrderBadge((int) cartManager.getTotal());
    }

    public void hideOrderBadge() {
        navigationBottom.hideOrderBadge();
    }
}

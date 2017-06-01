package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.databinding.ActivityCustomerBinding;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.view.fragment.CutleryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.DrinkingFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.EndpointDialogFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.HistoryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.OrderFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.QRCodeFragment;

import org.w3c.dom.Text;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import static com.nux.dhoan9.firstmvvm.utils.Constant.KEY_TOTAL_PAYMENT;

public class CustomerActivity extends BaseActivity {
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
    @Inject
    PreferencesManager preferencesManager;
    @Inject
    UserRepo userRepo;
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
        super.onCreate(savedInstanceState);
        initDependency();
        initDrawer();
        initView();
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
        ((Application) getApplication()).getComponent()
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
                showFragmentPosition(CUTLERY_POS);
            }

            @Override
            public void onDrinkingClick() {
                showFragmentPosition(DRINKING_POS);
            }

            @Override
            public void onOrderClick() {
                showFragmentPosition(ORDER_POS);
            }

            @Override
            public void onHistoryClick() {
                showFragmentPosition(HISTORY_POS);
            }
        });

        onContinuesClick();
//        initFloatActionNavigation();
    }

    private void initFloatActionNavigation() {
//        fnv = binding.actionBarContent.floatingNavigationView;
        fnv.setOnClickListener( v -> fnv.open());
        fnv.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.profile:
                    startActivity(ProfileActivity.newInstance(CustomerActivity.this));
                    break;
                case R.id.logOut:
                    logout();
                    break;
                case R.id.apiEndpoint:
                    showEndpointDialog();
                    break;
                case R.id.payment:
                    startActivity(PaypalActivity.newInstance(CustomerActivity.this));
                    break;
                default:
                    break;
            }
            fnv.setCheckedItem(itemId);
            fnv.close();
            return true;
        });
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
//        showFragmentPosition(fragmentPos=3);
        showFragmentPosition(fragmentPos = 0);
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
//        if (fragment instanceof CutleryFragment) {
//            getSupportActionBar().setTitle(R.string.cutlery_fragment);
//        } else if (fragment instanceof DrinkingFragment) {
//            getSupportActionBar().setTitle(R.string.drinking_fragment);
//        } else if (fragment instanceof OrderFragment) {
//            getSupportActionBar().setTitle(R.string.order_fragment);
//        } else if (fragment instanceof HistoryFragment) {
//            getSupportActionBar().setTitle(R.string.history_fragment);
//        }
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void showFragmentPosition(int pos) {
        if (CUTLERY_POS == pos) {
            showFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (DRINKING_POS == pos) {
            showFragment(drinkingFragment);
            hideFragment(cutleryFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (ORDER_POS == pos) {
            showFragment(orderFragment);
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(historyFragment);
        } else if (HISTORY_POS == pos) {
            showFragment(historyFragment);
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

    @Override
    protected void setPreference(PreferencesManager preference) {
        super.setPreference(this.preferencesManager);
    }

    public void setTotalOrder(String total){
        tvTotal.setText(total);
    }

    public void setOrderToolBar(){
        binding.actionBarContent.rlOrderInfo.setVisibility(View.VISIBLE);
        svSearch.setVisibility(View.GONE);
    }

    public void setDishToolBar(){
        binding.actionBarContent.rlOrderInfo.setVisibility(View.GONE);
        svSearch.setVisibility(View.VISIBLE);
    }

    private void onContinuesClick(){
        tvContinues.setOnClickListener(v -> {
            float total = orderFragment.getTotalPayment();
            Intent i = new Intent(this, PaypalActivity.class);
            i.putExtra(Constant.KEY_TOTAL_PAYMENT, total);
            i.putExtra(Constant.KEY_ORDER_NAME, "Hello");
            i.putExtra(Constant.KEY_ORDER_ITEM_TOTAL, orderFragment.getItemtotal());

            startActivity(i);
        });

    }
}

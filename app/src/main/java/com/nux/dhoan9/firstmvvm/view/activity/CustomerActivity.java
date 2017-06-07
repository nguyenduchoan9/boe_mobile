package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.view.fragment.CutleryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.DrinkingFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.EndpointDialogFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.HistoryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.OrderFragment;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        initDependency();
        super.onCreate(savedInstanceState);
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
                svSearch.clearFocus();
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
        if (CUTLERY_POS == pos) {
            showFragment(cutleryFragment);
            setDishToolBar();
            cutleryFragment.synTheCart();
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (DRINKING_POS == pos) {
            showFragment(drinkingFragment);
            setDishToolBar();
            drinkingFragment.synTheCart();
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
            setDishToolBar();
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
    protected void setPreference() {
        mPreferencesManager = this.preferencesManager;
    }

    public void setTotalOrder(String total) {
        tvTotal.setText(total);
    }

    public void setOrderToolBar() {
        binding.actionBarContent.rlOrderInfo.setVisibility(View.VISIBLE);
        svSearch.setVisibility(View.GONE);
    }

    public void setDishToolBar() {
        binding.actionBarContent.rlOrderInfo.setVisibility(View.GONE);
        svSearch.setVisibility(View.VISIBLE);
    }

    private void onContinuesClick() {
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

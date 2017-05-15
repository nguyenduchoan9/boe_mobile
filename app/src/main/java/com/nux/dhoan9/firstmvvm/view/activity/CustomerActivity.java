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
import android.view.MenuItem;
import android.view.View;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityCustomerBinding;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.view.fragment.CutleryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.DrinkingFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.HistoryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.OrderFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.QRCodeFragment;

import javax.inject.Inject;

public class CustomerActivity extends BaseActivity {
    private NavigationBottom navigationBottom;
    @Inject
    PreferencesManager preferencesManager;
    QRCodeFragment qrCodeFragment = new QRCodeFragment();
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
        srProcessing = binding.actionBarContent.content.srProcessing;
        rlProcessing = binding.actionBarContent.content.rlProcessing;
        tvProcessingTitle = binding.actionBarContent.content.tvProcessingTitle;
    }

    private void initDrawer() {
        initToolbar();
        initDrawerMenu();
    }

    private void initDrawerMenu() {
        View headerLayout = binding.navView.getHeaderView(0);
    }

    private void initToolbar() {
        binding.navView.inflateMenu(R.menu.drawer_menu_customer);
        setSupportActionBar(binding.actionBarContent.toolbar);
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
            case R.id.nav_your_profile:
                startActivity(ProfileActivity.newInstance(this));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.logout:
                logout();
                break;
            case R.id.changeColor:
                startActivity(ColorChangeActivity.newInstance(this));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        navigationBottom = binding.actionBarContent.content.bottomNavigationContent.bottomNavigation;
        initContent();
        navigationBottom.setPress(0);
        navigationBottom.setListener(new NavigationBottom.NavigationListener() {
            @Override
            public void onScanQRCodeClick() {
                showFragmentPosition(0);
            }

            @Override
            public void onCutleryClick() {
                showFragmentPosition(1);
            }

            @Override
            public void onDrinkingClick() {
                showFragmentPosition(2);
            }

            @Override
            public void onOrderClick() {
                showFragmentPosition(3);
            }

            @Override
            public void onHistoryClick() {
                showFragmentPosition(4);
            }
        });
    }

    private void logout() {
        preferencesManager.logOut();
        startActivity(LoginActivity.newInstance(this));
    }

    private void initContent() {
        replaceContent(qrCodeFragment, false, "QRCodeFragment");
        replaceContent(cutleryFragment, false, "CutleryFragment");
        replaceContent(drinkingFragment, false, "DrinkingFragment");
        replaceContent(orderFragment, false, "OrderFragment");
        replaceContent(historyFragment, false, "HistoryFragment");

        fragmentPos = 0;
        showFragmentPosition(fragmentPos);
    }

    private void replaceContent(Fragment fragment, boolean exist, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (exist) {
            ft.show(fragment);
        } else {
            ft.add(R.id.frContent, fragment, tag);
        }
        ft.commit();
    }

    private void showFragment(Fragment fragment) {
        if (fragment instanceof QRCodeFragment) {
            getSupportActionBar().setTitle(R.string.scan_qr_code_fragment);
        } else if (fragment instanceof CutleryFragment) {
            getSupportActionBar().setTitle(R.string.cutlery_fragment);
        } else if (fragment instanceof DrinkingFragment) {
            getSupportActionBar().setTitle(R.string.drinking_fragment);
        } else if (fragment instanceof OrderFragment) {
            getSupportActionBar().setTitle(R.string.order_fragment);
        } else if (fragment instanceof HistoryFragment) {
            getSupportActionBar().setTitle(R.string.history_fragment);
        }
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    private void showFragmentPosition(int pos) {
        if (0 == pos) {
            showFragment(qrCodeFragment);
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (1 == pos) {
            showFragment(cutleryFragment);
            hideFragment(qrCodeFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (2 == pos) {
            showFragment(drinkingFragment);
            hideFragment(qrCodeFragment);
            hideFragment(cutleryFragment);
            hideFragment(orderFragment);
            hideFragment(historyFragment);
        } else if (3 == pos) {
            showFragment(orderFragment);
            hideFragment(qrCodeFragment);
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(historyFragment);
        } else if (4 == pos) {
            showFragment(historyFragment);
            hideFragment(qrCodeFragment);
            hideFragment(cutleryFragment);
            hideFragment(drinkingFragment);
            hideFragment(orderFragment);
        }
        fragmentPos = pos;
    }
}

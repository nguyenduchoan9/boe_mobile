package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityCustomerBinding;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.view.adapter.CustomerFragmentAdapter;

import javax.inject.Inject;

public class CustomerActivity extends AppCompatActivity {
    @Inject
    PreferencesManager preferencesManager;

    private ActivityCustomerBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, CustomerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer);
        initDependency();
        initDrawer();
        initView();
    }

    private void initView() {
        ViewPager viewPager = binding.actionBarContent.content.viewPager;
        FragmentPagerAdapter adapter = new CustomerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = binding.actionBarContent.tabLayout;
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(adapter.getPageTitle(i));
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_item, menu);
        return super.onCreateOptionsMenu(menu);
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

    private void logout() {
        preferencesManager.logOut();
        startActivity(LoginActivity.newInstance(this));
    }
}

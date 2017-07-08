package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityHomeBinding;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.view.fragment.NoteFragment;

import javax.inject.Inject;

public class ChefActivity extends AppCompatActivity {
    @Inject
    PreferencesManager preferencesManager;
    private ActivityHomeBinding activityHomeBinding;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, ChefActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initDependency();
        initDrawer();
        initView();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContentHome, NoteFragment.newInstance());
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.addToBackStack("Note");
        ft.commit();
    }

    private void initDrawer() {
        initToolbar();
        initDrawerMenu();
    }

    private void initDrawerMenu() {
        View headerLayout = activityHomeBinding.navView.getHeaderView(0);
    }

    private void initToolbar() {
        activityHomeBinding.navView.inflateMenu(R.menu.drawer_menu_chef);
        setSupportActionBar(activityHomeBinding.actionBarContent.toolbar);
        activityHomeBinding.navView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return false;
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityHomeBinding.drawer,
                activityHomeBinding.actionBarContent.toolbar, R.string.open, R.string.close);
        activityHomeBinding.drawer.addDrawerListener(actionBarDrawerToggle);
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
            case R.id.nav_setting:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_sign_out:
                logout();
                break;
            default:
                break;
        }
        activityHomeBinding.drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (activityHomeBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding.drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void initDependency() {
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        preferencesManager.logOut();
        startActivity(LoginActivity.newInstance(this));
    }
}

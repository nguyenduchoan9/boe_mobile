package com.nux.dhoan9.firstmvvm.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nux.dhoan9.firstmvvm.view.fragment.MenuFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.QRCodeFragment;
/**
 * Created by hoang on 08/05/2017.
 */

public class CustomerFragmentAdapter extends FragmentPagerAdapter {
    String[] titles = {"QRCODE", "MENU"};

    public CustomerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return QRCodeFragment.newInstance();
            case 1:
                return MenuFragment.newInstance();
        }
        return QRCodeFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

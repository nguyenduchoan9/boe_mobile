package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.nux.dhoan9.firstmvvm.view.fragment.FeedbackFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.RatingFragment;
import java.util.ArrayList;

/**
 * Created by hoang on 07/08/2017.
 */

public class RatingPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    private Context mContext;

    public RatingPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new RatingFragment());
        fragments.add(new FeedbackFragment());
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (0 == position) {
            return fragments.get(position);
        } else if (1 == position) {
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public Fragment getFragment(int pos) {
        return fragments.get(pos);
    }
}

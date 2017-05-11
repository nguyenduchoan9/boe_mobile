package com.nux.dhoan9.firstmvvm.view.custom;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.dynamic.LifecycleDelegate;
/**
 * Created by hoang on 10/05/2017.
 */

public class BaseDelegate implements LifecycleDelegate{
    @Override
    public void onInflate(Activity activity, Bundle bundle, Bundle bundle1) {

    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }
}

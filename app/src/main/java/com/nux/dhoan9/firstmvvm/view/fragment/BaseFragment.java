package com.nux.dhoan9.firstmvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nux.dhoan9.firstmvvm.view.activity.BaseActivity;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import rx.subjects.PublishSubject;

/**
 * Created by hoang on 27/03/2017.
 */

public abstract class BaseFragment extends Fragment {
    private PublishSubject<BaseFragment> createView = PublishSubject.create();
    private PublishSubject<BaseFragment> destroyView = PublishSubject.create();

    public PublishSubject<BaseFragment> preDestroyView() {
        return destroyView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createView.onNext(this);
    }

    @Override
    public void onDestroyView() {
        destroyView.onNext(this);
        super.onDestroyView();
    }

    public void showProcessing(String title) {
        ((CustomerActivity) getActivity()).showProcessing(title);
    }

    public void hideProcessing() {
        ((CustomerActivity) getActivity()).hideProcessing();
    }
}

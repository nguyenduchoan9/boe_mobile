package com.nux.dhoan9.firstmvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nux.dhoan9.firstmvvm.view.activity.BaseActivity;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import org.w3c.dom.Text;
import rx.subjects.PublishSubject;

/**
 * Created by hoang on 27/03/2017.
 */

public abstract class BaseFragment extends Fragment {
    private PublishSubject<BaseFragment> createView = PublishSubject.create();
    private PublishSubject<BaseFragment> destroyView = PublishSubject.create();
    protected boolean isInit = true;

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

    protected void setTotal(String total) {
        ((CustomerActivity) getActivity()).setTotalOrder(total);
    }

    protected void setOrderToolBar() {
        ((CustomerActivity) getActivity()).setOrderToolBar();
    }

    protected void setDishToolBar() {
        ((CustomerActivity) getActivity()).setDishToolBar();
    }

    protected void setProcessing(RelativeLayout rlProcessing, TextView tvProcessingTitle) {
        ((BaseActivity) getActivity()).rlProcessing = rlProcessing;
        ((BaseActivity) getActivity()).tvProcessingTitle = tvProcessingTitle;
    }

    protected void showProgressingOnSearching(){
        ((CustomerActivity)getActivity()).showProgressAndDisableTouch();
    }

    protected void hideProgressingOnSearching(){
        ((CustomerActivity)getActivity()).hideProgressAndEnableTouch();
    }

    public void showNoSearchResult(){
        ((CustomerActivity)getActivity()).showNoSearchResult();
    }

    public void hideNoSearchResult(){
        ((CustomerActivity)getActivity()).hideNoSearchResult();
    }

    protected void setSearchKeyOnSearchBar(String key, int from){
        ((CustomerActivity)getActivity()).setSearchKeyInBar(key, from);
    }
}

package com.nux.dhoan9.firstmvvm.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentMenuBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.view.adapter.DishListAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

public class MenuFragment extends BaseFragment {
    FragmentMenuBinding binding;
    private RecyclerView rvDish;

    @Inject
    DishListAdapter adapter;
    @Inject
    DishListViewModel viewModel;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getActivity().getApplication()).getComponent()
                .plus(new ActivityModule(getActivity()))
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializer();
    }

    private void initializer() {
        binding.setViewModel(viewModel);
        viewModel.initialize();
        viewModel.scrollTo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pos -> rvDish.smoothScrollToPosition(pos));
    }

    private void initView() {
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvDish = binding.rvDish;
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(layoutManager);
    }
}

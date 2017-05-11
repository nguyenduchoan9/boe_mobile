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
import com.nux.dhoan9.firstmvvm.model.ActionMenuHorizontal;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.adapter.ActionAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.DishListAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

public class MenuFragment extends BaseFragment {
    FragmentMenuBinding binding;
    private RecyclerView rvDish;
    private RecyclerView rvAction;

    @Inject
    MenuCategoryListAdapter adapter;
    @Inject
    MenuCateListViewModel viewModel;

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
//        viewModel.scrollTo()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(pos -> rvDish.smoothScrollToPosition(pos));
    }

    private void initView() {
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDish = binding.rvDish;
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(manager);

        LinearLayoutManager managerAction =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvAction = binding.rvAction;
        ActionAdapter actionAdapter = new ActionAdapter(getContext());
        rvAction.setAdapter(actionAdapter);
        rvAction.setLayoutManager(managerAction);

        List<ActionMenuHorizontal> actionMenuHorizontals = new ArrayList<>();
        actionMenuHorizontals.add(new ActionMenuHorizontal("CART", Constant.MENU_TYPE_CART));
        actionMenuHorizontals.add(new ActionMenuHorizontal("HISTORY", Constant.MENU_TYPE_HISTORY));
        actionAdapter.initialize(actionMenuHorizontals);
    }
}

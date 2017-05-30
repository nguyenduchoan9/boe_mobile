package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentDrinkingBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;

import javax.inject.Inject;
import javax.inject.Named;

public class DrinkingFragment extends Fragment {
    FragmentDrinkingBinding binding;
    private RecyclerView rvDish;

    @Inject
    @Named("drinking")
    MenuCategoryListAdapter adapter;
    @Inject
    @Named("drinking")
    MenuCateListViewModel viewModel;

    public DrinkingFragment() {
        // Required empty public constructor
    }

    public static DrinkingFragment newInstance() {
        DrinkingFragment fragment = new DrinkingFragment();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drinking, container, false);
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
        initializerData();
        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initializerData() {
        viewModel.initializeDrinking();
    }

    private void initView() {
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDish = binding.rvDish;
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(manager);
        rvDish.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                NavigationBottom view = ((CustomerActivity) getActivity()).getNavigationBottom();
                if (dy > 0) {
                    // Scrolling up
                    view.animate()
                            .setDuration(300)
                            .translationY(view.getHeight() * 1.5F);
                    view.setVisibility(View.GONE);
                } else {
                    // Scrolling down
                    view.setVisibility(View.VISIBLE);
                    view.animate()
                            .setDuration(300)
                            .translationY(0);
                }
            }
        });
    }
}

package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentDrinkingBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;

import javax.inject.Inject;
import javax.inject.Named;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DrinkingFragment extends BaseFragment {
    FragmentDrinkingBinding binding;
    private RecyclerView rvDish;
    private boolean isHaveResult = true;

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
        ((BoeApplication) getActivity().getApplication()).getComponent()
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
        setDishToolBar();
        initializerData();
        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initializerData() {
        binding.setViewModel(viewModel);
        viewModel.initializeDrinking(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {});
        Glide.with(this)
                .load(Constant.API_ENDPOINT + "/images/background.jpg")
                .into(binding.ivBackground);
    }

    public void handleNoSearchResultView() {
        if (isInit) {
            isInit = false;
        } else {
            if (!isHaveResult) {
                showNoSearchResult();
                rvDish.setVisibility(View.INVISIBLE);
                setSearchKeyOnSearchBar(adapter.getKeySearch(),1);
            } else {
                hideNoSearchResult();
                if (rvDish.getVisibility() != View.VISIBLE) {
                    rvDish.setVisibility(View.VISIBLE);
                }
                setSearchKeyOnSearchBar(adapter.getKeySearch(), 1);
            }
        }
    }

    private void initView() {
        setActionSwipeContainer();
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDish = binding.rvDish;
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(manager);
    }

    private void setActionSwipeContainer() {
        binding.srRefresh.setOnRefreshListener(() -> {
            viewModel.initializeDrinking(true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        isHaveResult = true;
                        clearSearchKey();
                        hideNoSearchResult();
                        rvDish.setVisibility(View.VISIBLE);
                        binding.srRefresh.setRefreshing(false);
                    });
        });

        binding.srRefresh.setColorSchemeResources(R.color.holoBlueBright,
                R.color.holoGreenLight,
                R.color.holoOrangeLight,
                R.color.holoRedLight);
    }

    public void onSearchSubmit(String keySearch) {
        viewModel.onDrinkingSearch(keySearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(action -> setAdapterSearchKey(keySearch))
                .doOnSubscribe(() -> showProgressingOnSearching())
                .doOnTerminate(() -> hideProgressingOnSearching())
                .subscribe(result -> {
                    if (result.size() == 0) {
                        showNoSearchResult();
                        rvDish.setVisibility(View.INVISIBLE);
                        isHaveResult = false;
                    } else {
                        hideNoSearchResult();
                        rvDish.setVisibility(View.VISIBLE);
                        isHaveResult = true;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        handleNoSearchResultView();
    }

    public void synTheCart(){
        viewModel.synCartInCate();
    }

    private void setAdapterSearchKey(String searchKey) {
        adapter.setKeySearch(searchKey);
    }

    public void clearSearchKey(){
        setAdapterSearchKey("");
    }

    public void notifyChange(){
        viewModel.notifyCartChange();
    }
}

package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentCutleryBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;
import javax.inject.Inject;
import javax.inject.Named;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CutleryFragment extends BaseFragment {
    FragmentCutleryBinding binding;
    private RecyclerView rvDish;
    @Inject
    @Named("cutlery")
    MenuCategoryListAdapter adapter;
    @Inject
    @Named("cutlery")
    MenuCateListViewModel viewModel;

    public CutleryFragment() {
        // Required empty public constructor
    }

    public static CutleryFragment newInstance() {
        CutleryFragment fragment = new CutleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getActivity().getApplication()).getComponent()
                .plus(new ActivityModule(getActivity()))
                .inject(this);
        Log.i(log, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cutlery, container, false);
        Log.i(log, "onCreateView");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(log, "viewCreated");

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(log, "onStart");
        initializerData();
        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    private String log = "zzzzzz-Cutlery_TAG";

    @Override
    public void onStop() {
        Log.i(log, "onStop");
        super.onStop();
    }

    private void initializerData() {
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        viewModel.initialize(false)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() ->showProcessing(getString(R.string.text_processing)))
                .doOnCompleted(() -> hideProcessing())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                });
        Glide.with(this)
                .load(Constant.API_ENDPOINT + "/images/background.jpg")
                .into(binding.ivBackground);
    }

    private void initView() {
        setActionSwipeContainer();
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDish = binding.rvDish;
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(manager);
//        rvDish.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                RelativeLayout view = ((CustomerActivity) getActivity()).getNavigationBottom();
//                if (dy > 0) {
////                    // Scrolling up
//                    view.setVisibility(View.GONE);
//                } else {
//                    // Scrolling down
//                    view.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    public void synTheCart() {
        viewModel.synCartInCate();
    }

    private void setActionSwipeContainer() {
        binding.srRefresh.setOnRefreshListener(() -> {
            viewModel.initialize(true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(v -> clearSearchKey())
                    .subscribe(result -> {
                        binding.srRefresh.setRefreshing(false);
                    });
        });

        binding.srRefresh.setColorSchemeResources(R.color.holoBlueBright,
                R.color.holoGreenLight,
                R.color.holoOrangeLight,
                R.color.holoRedLight);
    }

    public void onSearchSubmit(String keySearch) {
        viewModel.onCutlerySearch(keySearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(action -> setAdapterSearchKey(keySearch))
                .doOnNext(v -> showProcessing(getString(R.string.text_processing)))
                .doOnTerminate(() -> hideProcessing())
                .subscribe(result -> {
                });
    }

    private void setAdapterSearchKey(String searchKey) {
        adapter.setKeySearch(searchKey);
    }

    public void clearSearchKey() {
        setAdapterSearchKey("");
    }
}

package com.nux.dhoan9.firstmvvm.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bumptech.glide.Glide;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.databinding.FragmentCutleryBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
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
    @Inject
    CartManager cartManager;
    @Inject
    DishRepo dishRepo;
    private boolean isHaveResult = true;
    private boolean isHaveResultDishNotServe = false;

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
        Glide.with(this)
                .load(BuildConfig.BASE_URL + "images/background.jpg")
                .into(binding.ivBackground);
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
                .doOnSubscribe(() -> showProcessing(getString(R.string.text_processing)))
                .doOnCompleted(() -> hideProcessing())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e)))
                .subscribe(result -> {
                });
//        RxUtils.checkNetWork(getContext())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(isAvailable -> {
//                    if (-1 == isAvailable) {
//                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
//                    } else if (-2 == isAvailable) {
//                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
//                    } else {
//
//                    }
//                });
    }

    public void handleNoSearchResultView() {
        if (isInit) {
            isInit = false;
        } else {
            if (!isHaveResult && !isHaveResultDishNotServe) {
                showNoSearchResult();
                rvDish.setVisibility(View.INVISIBLE);
                setSearchKeyOnSearchBar(adapter.getKeySearch(), 0);
            } else if (isHaveResultDishNotServe) {
                ((CustomerActivity) getActivity()).showSearchDishNotServe();
                rvDish.setVisibility(View.INVISIBLE);
                setSearchKeyOnSearchBar(adapter.getKeySearch(), 0);
            } else {
                hideSearchDishNotServe();
                hideNoSearchResult();
                if (rvDish.getVisibility() != View.VISIBLE) {
                    rvDish.setVisibility(View.VISIBLE);
                }
                setSearchKeyOnSearchBar(adapter.getKeySearch(), 0);
            }
        }
    }

    private void initView() {
        setActionSwipeContainer();
        adapter.setListener((isMax, dishId) -> {
            RxUtils.checkNetWork(getContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isAvailable -> {
                        if (-1 == isAvailable) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
                        } else if (-2 == isAvailable) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
                        } else {
                            dishRepo.checkDishCartAvailable(String.valueOf(dishId))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnTerminate(() -> hideProcessing())
                                    .doOnError(e -> ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e)))
                                    .subscribe(dishNotAvailable -> {
                                        if (null == dishNotAvailable || 0 == dishNotAvailable.size()) {
                                            if (isMax) {
                                                ToastUtils.toastLongMassage(getContext(), getString(R.string.text_toast_maximum_quantity));
                                            } else {
                                                updateOrderBagde();
                                            }
                                        } else if (0 < dishNotAvailable.size()) {
                                            cartManager.removeOutOfCart(dishId);
                                            handleDishNotServe();
                                        }
                                        updateOrderBagde();
                                    });
                        }
                    });
        });
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDish = binding.rvDish;
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(manager);
    }

    private void handleDishNotServe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCustomTitle(inflater.inflate(R.layout.layout_dialog_dish_cutlery_title, null, false));
        View content = inflater.inflate(R.layout.layout_dialog_update_menu, null, false);
        builder.setView(content);

        AlertDialog alertDialog = builder.create();
        Button btnYes = (Button) content.findViewById(R.id.btnSelect);
        btnYes.setOnClickListener(v -> {
            alertDialog.dismiss();
            refreshMenu();
        });

        alertDialog.show();
    }

    public void synTheCart() {
        viewModel.synCartInCate();
    }

    public void refreshMenu() {
        RxUtils.checkNetWork(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
                    } else {
                        viewModel.initialize(true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(v -> clearSearchKey())
                                .doOnError(e -> ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e)))
                                .subscribe(result -> {
                                    isHaveResult = true;
                                    isHaveResultDishNotServe = false;
                                    clearSearchKey();
                                    hideNoSearchResult();
                                    hideSearchDishNotServe();
                                    rvDish.setVisibility(View.VISIBLE);
                                    binding.srRefresh.setRefreshing(false);
                                });
                    }
                });
    }

    private void setActionSwipeContainer() {
        binding.srRefresh.setOnRefreshListener(() -> {
            refreshMenu();
        });

        binding.srRefresh.setColorSchemeResources(R.color.holoBlueBright,
                R.color.holoGreenLight,
                R.color.holoOrangeLight,
                R.color.holoRedLight);
    }

    public void onSearchSubmit(String keySearch) {
        RxUtils.checkNetWork(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
                    } else {
                        viewModel.onCutlerySearch(keySearch)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(action -> setAdapterSearchKey(keySearch))
                                .doOnSubscribe(() -> showProgressingOnSearching())
                                .doOnTerminate(() -> hideProgressingOnSearching())
                                .doOnError(e -> ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e)))
                                .subscribe(result -> {
                                    hideNoSearchResult();
                                    hideSearchDishNotServe();
                                    if (result.isNoResult() == true && result.getResult().size() == 0) {
                                        // dish not serve
                                        ((CustomerActivity) getActivity()).showSearchDishNotServe();
                                        rvDish.setVisibility(View.INVISIBLE);
                                        isHaveResult = false;
                                        isHaveResultDishNotServe = true;
                                    } else if (result.isNoResult() == false) {
                                        // no search result
                                        showNoSearchResult();
                                        rvDish.setVisibility(View.INVISIBLE);
                                        isHaveResult = false;
                                    } else {
                                        rvDish.setVisibility(View.VISIBLE);
                                        isHaveResult = true;
                                    }
                                    hideProgressingOnSearching();
                                });
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        handleNoSearchResultView();
    }

    private void setAdapterSearchKey(String searchKey) {
        adapter.setKeySearch(searchKey);
    }

    public void clearSearchKey() {
        setAdapterSearchKey("");
    }

    public void notifyChange() {
        viewModel.notifyCartChange();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(MyContextWrapper
                .wrap(context,
                        Utils.getLanguage(context)));
    }
}

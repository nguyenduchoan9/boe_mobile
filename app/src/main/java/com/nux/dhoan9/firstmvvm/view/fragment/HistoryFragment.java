package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.databinding.FragmentHistoryBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.model.OrderInfoItem;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.HistoryAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.HistoryListViewModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HistoryFragment extends BaseFragment {
    FragmentHistoryBinding binding;

    RecyclerView rv;
    @Inject
    HistoryListViewModel viewModel;
    @Inject
    HistoryAdapter adapter;
    @Inject
    OrderRepo orderRepo;
    @Inject
    CartManager cartManager;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        rv = binding.rvHistory;
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter.setLinstener(orderListId -> {
            RxUtils.checkNetWork(getContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isAvailable -> {
                        if (-1 == isAvailable) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
                        } else if (-2 == isAvailable) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
                        } else {
                            orderRepo.getOrderInfo(orderListId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(() -> showProcessing(getString(R.string.text_processing)))
                                    .doOnError(e -> ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e)))
                                    .doOnCompleted(() -> hideProcessing())
                                    .subscribe(orderInfo -> showDialog(orderInfo.getList()));
                        }
                    });

        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void showDialog(List<OrderInfoItem> infoItemList) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        HistoryInfoDialog dialog = HistoryInfoDialog.newInstance(infoItemList);
        dialog.setListener(new HistoryInfoDialog.OrderInfoListener() {
            @Override
            public void onOrderClick(List<OrderInfoItem> items) {
                for (OrderInfoItem item : items) {
                    if (!cartManager.isInCart(item.getId())) {
                        cartManager.plus(item.getId(), 1, "");
                    }
                }
                updateOrderBagde();
                ((CustomerActivity) getActivity()).showOrderFragment();
            }

            @Override
            public void onCloseClick() {
                onStart();
            }
        });
        dialog.show(fm, "eeee");
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeData();
        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    private void initializeData() {
        binding.setVm(viewModel);
        binding.executePendingBindings();
        RxUtils.checkNetWork(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAvailable -> {
                    if (-1 == isAvailable) {
                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
                    } else if (-2 == isAvailable) {
                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
                    } else {
                        viewModel.initialize()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(() -> showProcessing(getString(R.string.text_loading)))
                                .doOnError(e -> ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e)))
                                .doOnCompleted(() -> hideProcessing())
                                .subscribe(v -> {
                                    if (v) {
                                        hideNoResult();
                                    } else {
                                        showNoResult();
                                    }
                                });
                    }
                });
    }

    private void showNoResult() {
        binding.rvHistory.setVisibility(View.GONE);
        binding.tvNoHistory.setVisibility(View.VISIBLE);
    }

    private void hideNoResult() {
        binding.rvHistory.setVisibility(View.VISIBLE);
        binding.tvNoHistory.setVisibility(View.GONE);
    }
}

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
import android.widget.RelativeLayout;
import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.databinding.FragmentHistoryBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.model.OrderInfoItem;
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
        ((Application) getActivity().getApplication()).getComponent()
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
            orderRepo.getOrderInfo(orderListId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(() -> showProcessing("Processing"))
                    .doOnCompleted(() -> hideProcessing())
                    .subscribe(orderInfo -> showDialog(orderInfo.getList()));
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RelativeLayout view = ((CustomerActivity) getActivity()).getNavigationBottom();
                if (dy > 0) {
                    // Scrolling up
                    view.setVisibility(View.GONE);
                } else {
                    // Scrolling down\
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showDialog(List<OrderInfoItem> infoItemList) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        OrderInfoDialog dialog = OrderInfoDialog.newInstance(infoItemList);
        dialog.setListener(items -> {
            for (OrderInfoItem item : items){
                cartManager.plus(item.getId(), 1);
            }
            ((CustomerActivity)getActivity()).showOrderFragment();
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
        viewModel.initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProcessing("Loading..."))
                .doOnCompleted(() -> hideProcessing())
                .subscribe(v -> {
                    if (v) {
                        hideNoResult();
                    } else {
                        showNoResult();
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

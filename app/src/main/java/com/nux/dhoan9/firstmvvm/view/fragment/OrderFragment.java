package com.nux.dhoan9.firstmvvm.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.CartDishAvailable;
import com.nux.dhoan9.firstmvvm.databinding.FragmentOrderBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.OrderAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class OrderFragment extends BaseFragment {
    FragmentOrderBinding binding;

    @Inject
    CartItemListViewModel cartItemListViewModel;
    @Inject
    OrderAdapter adapter;
    @Inject
    CartManager cartManager;
    @Inject
    OrderRepo orderRepo;
    @Inject
    DishRepo dishRepo;
    float total = 0F;
    RecyclerView rvCart;

    public PublishSubject<Boolean> isCheckAvailable = PublishSubject.create();

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getActivity().getApplication()).getComponent()
                .plus(new ActivityModule(getActivity()))
                .inject(this);
        isCheckAvailable.observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
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
        binding.rlHourOver.setVisibility(View.GONE);
        if (cartManager.getCart().isEmpty()) {
            binding.rlEmptyMsg.setVisibility(View.VISIBLE);
            isCheckAvailable.onNext(false);
        } else {
            binding.rlEmptyMsg.setVisibility(View.GONE);
            isCheckAvailable.onNext(true);
        }
        initializeData();
        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    private String log = "zzzzzz-Order_TAG";

    @Override
    public void onResume() {
        Log.i(log, "onStop");
        super.onResume();
    }

    private void initializeData() {
        binding.setViewModel(cartItemListViewModel);
        binding.executePendingBindings();
        cartItemListViewModel
                .initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProcessing(getString(R.string.text_processing)))
                .subscribe(result -> {
                    initTotalPayment(cartItemListViewModel.getCartItems());
                    checkDishIsAvailable();
                });
//        showProcessing("Loading...");
    }

    private void checkDishIsAvailable() {
        orderRepo.isAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> hideProcessing())
                .flatMap(new Func1<CanOrder, Observable<List<CartDishAvailable>>>() {
                    @Override
                    public Observable<List<CartDishAvailable>> call(CanOrder canOrder) {
                        if (canOrder.isAvailable()) {
                            if (cartManager.getCart().size() == 0) {
                                return dishRepo.checkDishCartAvailable("")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            } else {
                                return dishRepo.checkDishCartAvailable(getIdDishInCart())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dishNotAvailable -> {
                    if (null == dishNotAvailable) {
                        showrRlHourOver();
                    } else if (0 < dishNotAvailable.size()) {
                        showBeforeHandle(dishNotAvailable);
                    }
                });

    }

    private String getIdDishInCart() {
        StringBuilder ids = new StringBuilder();
        for (Map.Entry<Integer, Integer> cartItem : cartManager.getCart().entrySet()) {
            ids.append(String.valueOf(cartItem.getKey()))
                    .append("_");
        }
        return ids.deleteCharAt(ids.length() - 1).toString();
    }

    private void navigationBottom() {
    }

    private void initTotalPayment(List<CartItemViewModel> cartItems) {
        total = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItemViewModel cartItem = cartItems.get(i);
            total += cartItem.quantity * cartItem.price;
        }
        setTotal(String.valueOf(new BigDecimal(total)));
    }

    private void initView() {

        rvCart = binding.rvCart;
        adapter.setListener(new OrderAdapter.CartListener() {
            @Override
            public void onIncrementQuantity(CartItemViewModel.Oops oops) {
                if (oops.isUpperQuantityBound) {
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_toast_maximum_quantity));
                    return;
                }
                total += oops.price;
                String textTotal = String.valueOf(new BigDecimal(getTotalPayment()));
                setTotal(textTotal);
                updateOrderBagde();
            }

            @Override
            public void onDecrementQuantity(CartItemViewModel.Oops oops) {
                if (oops.isLowerQuantityBound) {
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_toast_minimum));
                    return;
                }
                total -= oops.price;
                String textTotal = String.valueOf(new BigDecimal(getTotalPayment()));
                setTotal(textTotal);
                updateOrderBagde();
            }

            @Override
            public void onRemove(float minus) {
                total -= minus;
                String textTotal = String.valueOf(new BigDecimal(getTotalPayment()));
                setTotal(textTotal);
                updateOrderBagde();
            }

            @Override
            public void onRemoveAll() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setCustomTitle(inflater.inflate(R.layout.layout_dialog_remove_dish_title, null, false));
                View content = inflater.inflate(R.layout.layout_dialog_remove_dish_content, null, false);
                builder.setView(content);

                AlertDialog alertDialog = builder.create();
                Button btnYes = (Button) content.findViewById(R.id.btnSelect);
                btnYes.setOnClickListener(v -> {
                    cartManager.clear();
                    alertDialog.dismiss();
                    updateOrderBagde();
                    onStart();
                });

                Button btnNo = (Button) content.findViewById(R.id.btnClose);
                btnNo.setOnClickListener(v -> {
                    alertDialog.dismiss();
                });
                builder.setPositiveButton(R.string.text_dialog_negative_button, (dialog, which) -> {

                });
                builder.setNegativeButton(R.string.text_dialog_positive_button, (dialog, which) -> {
                    dialog.dismiss();
                });
                alertDialog.show();
            }
        });
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvCart.setAdapter(adapter);
        rvCart.setLayoutManager(manager);
        rvCart.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    public float getTotalPayment() {
        List<CartItemViewModel> vms = cartItemListViewModel.getCartItems();
        float mtotal = 0F;
        for (int i = 0; i < vms.size(); i++) {
            CartItemViewModel item = vms.get(i);
            mtotal += (item.quantity * item.price);
        }
        return mtotal;
    }

    public int getItemtotal() {
        return cartManager.getItemTotal();
    }

    public void showDialog(List<CartDishAvailable> infoItemList) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        OrderNotAvailableDialog dialog = OrderNotAvailableDialog.newInstance(infoItemList);
        dialog.setListener(new OrderNotAvailableDialog.OrderInfoListener() {
            @Override
            public void onOrderClick(List<CartDishAvailable> items) {
//                for (CartDishAvailable item : items) {
//                    cartManager.removeOutOfCart(item.getId());
//                }
                cartItemListViewModel.notifyCartChange(items);
                ((CustomerActivity) getActivity()).notifyCartChange();
                setTotal(String.valueOf(new BigDecimal(getTotalPayment())));
                updateOrderBagde();
                ((CustomerActivity) getActivity()).performClickContinues();
            }

            @Override
            public void onCancelClick() {
                cartManager.clear();
//                cartItemListViewModel.notifyCartChange();
                updateOrderBagde();
                ((CustomerActivity) getActivity()).notifyCartChange();
                ((CustomerActivity) getActivity()).showOrderFragment();
            }
        });
        dialog.show(fm, "eeee");
    }

    public void showrRlHourOver() {
        binding.rlHourOver.setVisibility(View.VISIBLE);
    }

    public void showBeforeHandle(List<CartDishAvailable> infoItemList) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        BeforeDishNotAvailableDialog dialog = BeforeDishNotAvailableDialog.newInstance(infoItemList);
        dialog.setListener(items -> {
            for (CartDishAvailable item : items) {
                cartManager.removeOutOfCart(item.getId());
            }
            updateOrderBagde();
            ((CustomerActivity) getActivity()).showOrderFragment();
        });
        dialog.show(fm, "beyond");
    }
}

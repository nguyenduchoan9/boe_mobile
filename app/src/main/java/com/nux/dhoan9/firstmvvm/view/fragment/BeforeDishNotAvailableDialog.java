package com.nux.dhoan9.firstmvvm.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.response.CartDishAvailable;
import com.nux.dhoan9.firstmvvm.view.adapter.DishNotAvailableInfoAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 14/06/2017.
 */

public class BeforeDishNotAvailableDialog extends DialogFragment {
    DishNotAvailableInfoAdapter adapter;
    RecyclerView rvList;
    Button btnCancel;
    Button btnOrder;

    public BeforeDishNotAvailableDialog() {
    }

    public static BeforeDishNotAvailableDialog newInstance(List<CartDishAvailable> items) {
        BeforeDishNotAvailableDialog dialog = new BeforeDishNotAvailableDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) items);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog_before_order_info, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvList = (RecyclerView) view.findViewById(R.id.rvList);
        List<CartDishAvailable> items = getArguments().getParcelableArrayList("list");
        setOrderInfoList(items);
        btnOrder = (Button) view.findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(v -> {
            listener.onOrderClick(getArguments().getParcelableArrayList("list"));
            dismiss();
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setOrderInfoList(List<CartDishAvailable> items) {
        adapter = new DishNotAvailableInfoAdapter(getActivity(), items);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(decoration);
        rvList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        // Call super onResume after sizing

        super.onResume();
    }

    public interface OrderInfoListener {
        void onOrderClick(List<CartDishAvailable> items);
    }

    private OrderInfoListener listener;

    public void setListener(OrderInfoListener listener) {
        this.listener = listener;
    }
}

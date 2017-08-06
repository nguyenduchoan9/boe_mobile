package com.nux.dhoan9.firstmvvm.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.nux.dhoan9.firstmvvm.view.adapter.MinusDesAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 06/08/2017.
 */

public class MinusDesDialog extends DialogFragment {
    RecyclerView rvDes;
    Button btnSelect, btnCancel;
    private int dishId = -1;
    List<String> des;

    public MinusDesDialog() {
    }

//    public static String DISH_KEY = "DISH_KEY";

    public static String DES_KEY = "DES_KEY";

    public static MinusDesDialog newInstance(List<String> des) {
        MinusDesDialog dialog = new MinusDesDialog();
        Bundle arg = new Bundle();
        arg.putStringArrayList(DES_KEY, (ArrayList<String>) des);
        dialog.setArguments(arg);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            des = getArguments().getStringArrayList(DES_KEY);
        } else {
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_minus, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDes = (RecyclerView) view.findViewById(R.id.rvDes);
        List<MinusModel> adapterData = prepareDataAdapter();
        MinusDesAdapter adapter = new MinusDesAdapter(getContext());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDes.addItemDecoration(dividerItemDecoration);
        rvDes.setLayoutManager(layoutManager);
        rvDes.setAdapter(adapter);
        adapter.setData(adapterData);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSelect = (Button) view.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(v -> {
            if(null != listener){
                int selectedPosAdapter = adapter.getSelectedPos();
                if(selectedPosAdapter == -1){
                    return;
                }else {
                    des.remove(selectedPosAdapter);
                    listener.onSelectClick(des);
                    dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private List<MinusModel> prepareDataAdapter() {
        List<MinusModel> data = new ArrayList<>();
        boolean isHaveBlankDes = false;
        int lastBlankDes = -1;
        for (int i = 0; i < des.size(); i++) {
            String desItem = des.get(i);
            if (desItem.length() > 0) {
                MinusModel minusModel = new MinusModel(desItem, i);
                data.add(minusModel);
            } else {
                if (!isHaveBlankDes)
                    isHaveBlankDes = true;
                lastBlankDes = i;
            }
        }
        if (isHaveBlankDes) {
            MinusModel minusModel = new MinusModel("", lastBlankDes);
            data.add(minusModel);
        }

        return data;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    public interface MinusnDialogListener {
        void onSelectClick(List<String> updatedDes);
    }

    private MinusnDialogListener  listener;

    public void setListener(MinusnDialogListener  listener) {
        this.listener = listener;
    }

    public class MinusModel {
        public String description;
        public int pos;

        public MinusModel(String description, int pos) {
            this.description = description;
            this.pos = pos;
        }
    }
}

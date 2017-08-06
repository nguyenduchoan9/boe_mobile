package com.nux.dhoan9.firstmvvm.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.nux.dhoan9.firstmvvm.R;

/**
 * Created by hoang on 06/08/2017.
 */

public class DescriptionDialogFragment extends DialogFragment {
    EditText etDescription;
    Button btnSelect, btnCancel;
    private int dishId = -1;

    public DescriptionDialogFragment() {
    }

    public static String DISH_KEY = "DISH_KEY";

    public static DescriptionDialogFragment newInstance() {
        DescriptionDialogFragment dialog = new DescriptionDialogFragment();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (null != getArguments()) {
//            dishId = getArguments().getInt(DISH_KEY, -1);
//        }
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
        return inflater.inflate(R.layout.layout_description, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSelect = (Button) view.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(v -> {
            if (null != listener) {
                String des = etDescription.getText().toString().trim();
                if (des.length() > 0) {
                    listener.onOrderClick(des);
                } else {
                    listener.onOrderClick();
                }
                dismiss();
            }
        });
        btnCancel.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    public interface DescriptionDialogListener {
        void onOrderClick(String description);

        void onOrderClick();
    }

    private DescriptionDialogListener listener;

    public void setListener(DescriptionDialogListener listener) {
        this.listener = listener;
    }
}

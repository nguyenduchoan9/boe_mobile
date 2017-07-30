package com.nux.dhoan9.firstmvvm.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.Constant;

/**
 * Created by hoang on 20/07/2017.
 */

public class LanguageDialog extends DialogFragment {
    private TextView tvEnglish, tvVN;
    private Button btnSelect, btnClose;
    private Context mContext;
    private boolean isVnSelect = true;
    private boolean originisdata = true;

    public LanguageDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.VI_LANGUAGE_STRING.equals(getLanguage())) {
            isVnSelect = true;
            originisdata = isVnSelect;
        } else if (Constant.EN_LANGUAGE_STRING.equals(getLanguage())) {
            isVnSelect = false;
            originisdata = isVnSelect;
        }
    }

    public static LanguageDialog newInstance(String language) {
        LanguageDialog dialog = new LanguageDialog();
        Bundle args = new Bundle();
        args.putString(Constant.LANGUAGE_KEY, language);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_dialog_language, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        tvEnglish = (TextView) v.findViewById(R.id.tvEnglish);
        tvVN = (TextView) v.findViewById(R.id.tvVN);
        btnClose = (Button) v.findViewById(R.id.btnClose);
        btnSelect = (Button) v.findViewById(R.id.btnSelect);

        if (Constant.VI_LANGUAGE_STRING.equals(getLanguage())) {
            setSelectVn();
        } else if (Constant.EN_LANGUAGE_STRING.equals(getLanguage())) {
            setSelectEng();
        }

        tvEnglish.setOnClickListener(e -> setSelectEng());
        tvVN.setOnClickListener(e -> setSelectVn());

        btnClose.setOnClickListener(e -> dismiss());
        btnSelect.setOnClickListener(e -> {
            if (originisdata == isVnSelect) {
                dismiss();
            }
            if (null != mListener) {
                if (isVnSelect) {
                    mListener.onVNSelect();
                } else {
                    mListener.onEnglishSelect();
                }
            }
            dismiss();

        });
    }

    private void setSelectEng() {
        isVnSelect = false;
        tvEnglish.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        tvEnglish.setBackgroundColor(ContextCompat.getColor(mContext, R.color.register));
        tvVN.setTextColor(ContextCompat.getColor(mContext, R.color.primaryText));
        tvVN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
    }

    private void setSelectVn() {
        isVnSelect = true;
        tvVN.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        tvVN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.register));
        tvEnglish.setTextColor(ContextCompat.getColor(mContext, R.color.primaryText));
        tvEnglish.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
//        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
//        // Assign window properties to fill the parent
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        getDialog().getWindow().setAttributes(params);

//        WindowManager.LayoutParams layoutParamsb = getDialog().getWindow().getAttributes();
//        getDialog().getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
//        layoutParamsb.x = 100; // right margin
////        layoutParamsb.y = 170; // top margin
//        getDialog().getWindow().setAttributes(layoutParamsb);
//        // e.g. bottom + left margins:
//        getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.LEFT);
//        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
//        layoutParams.x = 100; // left margin
////        layoutParams.y = 170; // bottom margin
//        getDialog().getWindow().setAttributes(layoutParams);

        // Call super onResume after sizing

        super.onResume();
    }

    private String getLanguage() {
        return getArguments().getString(Constant.LANGUAGE_KEY, Constant.VI_LANGUAGE_STRING);
    }

    public interface LanguageListener {
        void onEnglishSelect();

        void onVNSelect();
    }

    private LanguageListener mListener;

    public void setmListener(LanguageListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}

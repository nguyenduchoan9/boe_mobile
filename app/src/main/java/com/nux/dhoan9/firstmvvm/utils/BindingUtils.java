package com.nux.dhoan9.firstmvvm.utils;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.custom.TextChange;
import com.nux.dhoan9.firstmvvm.view.custom.TextChangeAdapter;

/**
 * Created by hoang on 27/03/2017.
 */

public class BindingUtils {
    @BindingAdapter("textChange")
    public static void setInputError(EditText editText, TextChange textChange) {
        editText.addTextChangedListener(new TextChangeAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChange.onChange(s.toString());
            }
        });
    }

    @BindingAdapter("validateError")
    public static void setInputError(TextInputLayout textInputLayout, String value) {
        if (StringUtils.isEmpty(value)) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(value);
        }
    }

    @BindingAdapter("listBinder")
    public static <E> void bindItems(RecyclerView recyclerView, ListBinder<E> listBinder) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (null != adapter) {
            listBinder.setListener(diffResult -> diffResult.dispatchUpdatesTo(adapter));
        }
    }

    @BindingAdapter("android:checked")
    public static void setChecked(CheckBox checkBox, boolean checked) {
        if (checked != checkBox.isChecked()) {
            checkBox.setChecked(checked);
            checkBox.jumpDrawablesToCurrentState();
        }
    }

    @BindingAdapter("setImage")
    public static void setResizedBackground(ImageView imageView, String url) {
        String key = imageView.getContext().getClass().getSimpleName().toString();
//        Glide.with(imageView.getContext())
//                .load(Constant.API_ENDPOINT + url)
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(imageView);
        Glide.with(imageView.getContext())
                .load(Constant.API_ENDPOINT + url)
                .placeholder(R.drawable.dish_placeholder)
                .centerCrop()
                .signature(new StringSignature(key + url))
                .into(imageView);
    }
}

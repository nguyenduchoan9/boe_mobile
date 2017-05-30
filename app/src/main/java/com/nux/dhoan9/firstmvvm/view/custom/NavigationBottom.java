package com.nux.dhoan9.firstmvvm.view.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.Utils;

/**
 * Created by hoang on 13/05/2017.
 */

public class NavigationBottom extends LinearLayout implements View.OnClickListener {
    public RelativeLayout foodIconContainer;
    public RelativeLayout drinkIconContainer;
    public RelativeLayout orderIconContainer;
    public RelativeLayout historyIconContainer;
    public ImageView ivCutlery;
    public ImageView ivDrinking;
    public ImageView ivOrder;
    public ImageView ivHistory;

    public NavigationBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        foodIconContainer = (RelativeLayout) findViewById(R.id.rlCutlery);
        drinkIconContainer = (RelativeLayout) findViewById(R.id.rlDrinking);
        orderIconContainer = (RelativeLayout) findViewById(R.id.rlOrder);
        historyIconContainer = (RelativeLayout) findViewById(R.id.rlHistory);
        ivCutlery = (ImageView) findViewById(R.id.ivCutlery);
        ivDrinking = (ImageView) findViewById(R.id.ivDrinking);
        ivOrder = (ImageView) findViewById(R.id.ivOrder);
        ivHistory = (ImageView) findViewById(R.id.ivHistory);
        initViewClick(R.id.rlCutlery);
        initViewClick(R.id.ivCutlery);
        initViewClick(R.id.rlDrinking);
        initViewClick(R.id.ivDrinking);
        initViewClick(R.id.rlOrder);
        initViewClick(R.id.ivOrder);
        initViewClick(R.id.rlHistory);
        initViewClick(R.id.ivHistory);
    }

    private void initViewClick(int id) {
        View view = findViewById(id);
        if (null != view) {
            view.setOnClickListener(this);
        }
    }

    private void clearBackground() {
        Utils.changeDrawableColor(ivCutlery.getDrawable(),
                ContextCompat.getColor(getContext(), android.R.color.black));
        Utils.changeDrawableColor(ivDrinking.getDrawable(),
                ContextCompat.getColor(getContext(), android.R.color.black));
        Utils.changeDrawableColor(ivOrder.getDrawable(),
                ContextCompat.getColor(getContext(), android.R.color.black));
        Utils.changeDrawableColor(ivHistory.getDrawable(),
                ContextCompat.getColor(getContext(), android.R.color.black));
    }

    public void setPress(int pos) {
        clearBackground();
        switch (pos) {
            case 0:
                Utils.changeDrawableColor(ivCutlery.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                break;
            case 1:
                Utils.changeDrawableColor(ivDrinking.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                break;
            case 2:
                Utils.changeDrawableColor(ivOrder.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                break;
            case 3:
                Utils.changeDrawableColor(ivHistory.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                break;
            default:
                Utils.changeDrawableColor(ivCutlery.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (null != listener) {
            int id = v.getId();
            switch (id) {
                case R.id.rlCutlery:
                    setPress(0);
                    listener.onCutleryClick();
                    break;
                case R.id.rlDrinking:
                    setPress(1);
                    listener.onDrinkingClick();
                    break;
                case R.id.rlOrder:
                    setPress(2);
                    listener.onOrderClick();
                    break;
                case R.id.rlHistory:
                    setPress(3);
                    listener.onHistoryClick();
                    break;
                case R.id.ivCutlery:
                    setPress(0);
                    listener.onCutleryClick();
                    break;
                case R.id.ivDrinking:
                    setPress(1);
                    listener.onDrinkingClick();
                    break;
                case R.id.ivOrder:
                    setPress(2);
                    listener.onOrderClick();
                    break;
                case R.id.ivHistory:
                    setPress(3);
                    listener.onHistoryClick();
                    break;
                default:
                    break;
            }
        }
    }

    private NavigationListener listener;

    public void setListener(NavigationListener listener) {
        this.listener = listener;
    }

    public interface NavigationListener {
        void onCutleryClick();

        void onDrinkingClick();

        void onOrderClick();

        void onHistoryClick();
    }
}

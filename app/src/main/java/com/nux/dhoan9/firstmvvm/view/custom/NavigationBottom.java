package com.nux.dhoan9.firstmvvm.view.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nux.dhoan9.firstmvvm.R;
/**
 * Created by hoang on 13/05/2017.
 */

public class NavigationBottom extends LinearLayout implements View.OnClickListener {
    public RelativeLayout foodIconContainer;
    public RelativeLayout drinkIconContainer;
    public RelativeLayout orderIconContainer;
    public RelativeLayout historyIconContainer;
    public RelativeLayout qrCodeContainer;

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
        qrCodeContainer = (RelativeLayout) findViewById(R.id.rlScanQRCode);

        initViewClick(R.id.rlScanQRCode);
        initViewClick(R.id.ivScanQRCode);
        initViewClick(R.id.rlCutlery);
        initViewClick(R.id.ivCutlery);
        initViewClick(R.id.rlDrinking);
        initViewClick(R.id.ivDrinking);
        initViewClick(R.id.rlOrder);
        initViewClick(R.id.ivOrder);
        initViewClick(R.id.rlHistory);
        initViewClick(R.id.ivHistory);
//
//        foodIcon.
//        drinkIcon.setOnClickListener(this);
//        orderIcon.setOnClickListener(this);
//        historyIcon.setOnClickListener(this);
    }

    private void initViewClick(int id) {
        View view = findViewById(id);
        if (null != view) {
            view.setOnClickListener(this);
        }
    }

    private void clearBackground() {
        qrCodeContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        foodIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        drinkIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        orderIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        historyIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    public void setPress(int pos) {
        clearBackground();
        switch (pos) {
            case 0:
                qrCodeContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                break;
            case 1:
                foodIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                break;
            case 2:
                drinkIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                break;
            case 3:
                orderIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                break;
            case 4:
                historyIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                break;
            default:
                foodIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (null != listener) {
            int id = v.getId();
            switch (id) {
                case R.id.rlScanQRCode:
                    clearBackground();
                    qrCodeContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onScanQRCodeClick();
                    break;
                case R.id.ivScanQRCode:
                    clearBackground();
                    qrCodeContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onScanQRCodeClick();
                    break;
                case R.id.rlCutlery:
                    clearBackground();
                    foodIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onCutleryClick();
                    break;
                case R.id.rlDrinking:
                    clearBackground();
                    drinkIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onDrinkingClick();
                    break;
                case R.id.rlOrder:
                    clearBackground();
                    orderIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onOrderClick();
                    break;
                case R.id.rlHistory:
                    clearBackground();
                    historyIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onHistoryClick();
                    break;
                case R.id.ivCutlery:
                    clearBackground();
                    foodIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onCutleryClick();
                    break;
                case R.id.ivDrinking:
                    clearBackground();
                    drinkIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onDrinkingClick();
                    break;
                case R.id.ivOrder:
                    clearBackground();
                    orderIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
                    listener.onOrderClick();
                    break;
                case R.id.ivHistory:
                    clearBackground();
                    historyIconContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteDark));
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
        void onScanQRCodeClick();

        void onCutleryClick();

        void onDrinkingClick();

        void onOrderClick();

        void onHistoryClick();
    }
}

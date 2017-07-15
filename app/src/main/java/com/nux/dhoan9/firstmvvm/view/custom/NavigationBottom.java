package com.nux.dhoan9.firstmvvm.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import org.w3c.dom.Text;

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
    public TextView tvOrderBadge;
    public TextView tvHistoryBadge;
    public TextView tvHistory, tvDrinking, tvOrder, tvCutlery;
    public boolean isCanHanle = true;

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
        tvOrderBadge = (TextView) findViewById(R.id.tvOrderBadge);
        tvHistoryBadge = (TextView) findViewById(R.id.tvHistoryBadge);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        tvHistory.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        tvCutlery = (TextView) findViewById(R.id.tvCutlery);
        tvCutlery.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        tvDrinking = (TextView) findViewById(R.id.tvDrinking);
        tvDrinking.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
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

        setInActive(foodIconContainer, tvCutlery);
        setInActive(drinkIconContainer, tvDrinking);
        setInActive(orderIconContainer, tvOrder);
        setInActive(historyIconContainer, tvHistory);
    }

    public void setPress(int pos) {
        clearBackground();
        switch (pos) {
            case 0:
                Utils.changeDrawableColor(ivCutlery.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                setActive(foodIconContainer, tvCutlery);
                break;
            case 1:
                Utils.changeDrawableColor(ivDrinking.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                setActive(drinkIconContainer, tvDrinking);
                break;
            case 2:
                Utils.changeDrawableColor(ivOrder.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                setActive(orderIconContainer, tvOrder);
                break;
            case 3:
                Utils.changeDrawableColor(ivHistory.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                setActive(historyIconContainer, tvHistory);
                break;
            default:
                Utils.changeDrawableColor(ivCutlery.getDrawable(),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (null != listener && isCanHanle) {
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

    public void showOrderBadge(int quantity) {
        if (9 < quantity) {
            tvOrderBadge.setText("+9");
        } else {
            tvOrderBadge.setText(String.valueOf(quantity));
        }
        tvOrderBadge.setVisibility(VISIBLE);
    }

    public void hideOrderBadge() {
        tvOrderBadge.setVisibility(GONE);
    }

    public void showHisrotyBadge(int quantity) {
        if (9 < quantity) {
            tvHistoryBadge.setText("+9");
        } else {
            tvHistoryBadge.setText(String.valueOf(quantity));
        }
        tvHistoryBadge.setVisibility(VISIBLE);
    }

    public void hideHistoryBadge() {
        tvHistoryBadge.setVisibility(GONE);
    }

    private void setInActive(View container, TextView label) {
        // padding top 6 & 10dp under text
        container.setPadding(0, 8, 0, 10);
        // text size roboto regular 12
        label.setVisibility(GONE);
    }

    private void setActive(View container, TextView label) {
        // padding top 8
        container.setPadding(0, 6, 0, 10);
        label.setVisibility(VISIBLE);
        //10dp under text
        // text size roboto regular 14
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        p.setStrokeWidth(1F);
        canvas.drawLine(0,0, this.getWidth(), 0, p);
    }
}

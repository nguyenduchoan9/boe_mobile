package com.nux.dhoan9.firstmvvm.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
/**
 * Created by hoang on 09/05/2017.
 */

public class DynamicHeightImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "DynamicHeightImageView";
    private double whRatio = 0;


    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public void setRatio(double ratio) {
        whRatio = ratio;
        Log.d(TAG, "setRatio: " + whRatio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (!(getDrawable() == null)) {
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
//            setMeasuredDimension(width, height);
//        }
        if (whRatio != 0) {
            int width = getMeasuredWidth();
            int height = (int) (whRatio * width);
            setMeasuredDimension(width, height);
//			Log.d(TAG, "onMeasure: "+whRatio);
        } else {

            if (!(getDrawable() == null)) {

                int width = getDrawable().getIntrinsicWidth();
                int height = getDrawable().getIntrinsicHeight();
                if (height > 0 && width > 0) {
                    whRatio = (double) height / (double) width;
                    setMeasuredDimension(getMeasuredWidth(), (int) (whRatio * getMeasuredWidth()));
                }
            }else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

    }
}

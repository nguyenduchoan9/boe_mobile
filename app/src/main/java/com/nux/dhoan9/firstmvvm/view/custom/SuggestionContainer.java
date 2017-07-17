package com.nux.dhoan9.firstmvvm.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.nux.dhoan9.firstmvvm.R;
import java.util.ArrayList;

/**
 * Created by hoang on 16/07/2017.
 */

public class SuggestionContainer extends RelativeLayout {
    ArrayList<Line> lines;
    public SuggestionContainer(Context context) {
        super(context);
        lines = new ArrayList<>();
        Line top = new Line(0, 0, this.getWidth(), 0);
        Line bottom= new Line(this.getHeight(), 0, this.getWidth(), 0);
        Line left = new Line(0, this.getWidth(), 0, this.getHeight());
        Line right = new Line(0, 0, 0, this.getHeight());
        lines.add(top);
        lines.add(bottom);
        lines.add(left);
        lines.add(right);
    }

    public SuggestionContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuggestionContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SuggestionContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null != lines){
            Paint p = new Paint();
            //top
            p.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            p.setStrokeWidth(1F);
            for (Line line : lines){
                canvas.drawLine(line.startX, line.startY, line.stopX, line.stopY, p);
            }
        }
//
//        canvas.drawLine(0, 0, this.getWidth(), 0, p);
//        //bottom
//        canvas.drawLine(this.getHeight(), 0, this.getWidth(), 0, p);
//        //right
//        canvas.drawLine(0, 0, 0, this.getHeight(), p);
//        //left
//        canvas.drawLine(0, this.getWidth(), 0, this.getHeight(), p);
    }

    class Line {
        public int startX;
        public int startY;
        public int stopX;
        public int stopY;

        public Line(int startX, int startY, int stopX, int stopY) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }
    }
}

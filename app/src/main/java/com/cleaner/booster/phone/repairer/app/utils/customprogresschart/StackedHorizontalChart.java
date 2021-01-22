package com.cleaner.booster.phone.repairer.app.utils.customprogresschart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StackedHorizontalChart extends View {
    private ArrayList<StackedHorizontalChartModel> list;
    Rect progressRect ;

    public StackedHorizontalChart(Context context) {
        super(context);

    }

    public StackedHorizontalChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackedHorizontalChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StackedHorizontalChart(@NonNull Context context,
                                  @Nullable AttributeSet attrs, int defStyleAttr,
                                  ArrayList<StackedHorizontalChartModel> list) {

        super(context, attrs, defStyleAttr);
        this.list = list;

    }

    public void init(ArrayList<StackedHorizontalChartModel> list) {
        this.list = list;
        progressRect   = new Rect();
//        this.setBackground(ResourcesCompat.getDrawable(getResources(),
//                R.drawable.white_bg,null));
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (list!=null)
        {
        if (list.size() > 0  ) {
            int progressBarWidth = getWidth();
            int progressBarHeight = getHeight();
            int lastProgressX = 0;
            int progressItemWidth, progressItemRight;
            for (int i = 0; i < list.size(); i++) {
                StackedHorizontalChartModel progressItem = list.get(i);
                Paint progressPaint = new Paint();
                progressPaint.setColor(getResources().getColor(
                        progressItem.filledColor));

                progressItemWidth = (int) (list.get(i).itemPercentage * progressBarWidth / 100);

                progressItemRight = lastProgressX + progressItemWidth;

                // for last item give right to progress item to the width
                if (i == list.size() - 1
                        && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                progressRect.set(lastProgressX, 5,
                        progressItemRight, progressBarHeight - 1);
                canvas.drawRect(progressRect, progressPaint);
                lastProgressX = progressItemRight;
            }

        }
            super.onDraw(canvas);
        }

    }

}

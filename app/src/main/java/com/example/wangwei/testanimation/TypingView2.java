package com.example.wangwei.testanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangwei on 16/5/3.
 */
public class TypingView2 extends View {

    private int radius = 10;
    private int pointDis = 10;
    private Paint mPaint;
    private int pointHeight = -1;
    private boolean isAniStarted = false;
    private int highlightPos = 0;
    private int oldWidth = -1;
    private int oldHeight = -1;
    private int firstPointX = -1;
    private int midPointX = -1;
    private int lastPointX = -1;
    private final ValueAnimator va = ValueAnimator.ofInt(1, 4);

    public TypingView2(Context context) {
        this(context,null);
    }

    public TypingView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypingView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();
        mPaint.setColor(Color.parseColor("#d3d3d3"));
        if(width != oldWidth || height != oldHeight)
        {
            pointHeight = height/2;
            midPointX = width/2;
            firstPointX = midPointX - 2*radius - pointDis;
            lastPointX = midPointX + 2*radius + pointDis;
        }

        canvas.drawCircle(firstPointX, pointHeight, radius, mPaint);
        canvas.drawCircle(midPointX,pointHeight,radius,mPaint);
        canvas.drawCircle(lastPointX, pointHeight, radius, mPaint);

        mPaint.setColor(Color.parseColor("#222222"));
        int selectedRadius = radius + 3;
        switch (highlightPos)
        {
            case 1:
                canvas.drawCircle(firstPointX, pointHeight, selectedRadius, mPaint);
                break;
            case 2:
                canvas.drawCircle(midPointX, pointHeight, selectedRadius, mPaint);
                break;
            case 3:
                canvas.drawCircle(lastPointX, pointHeight, selectedRadius, mPaint);
                break;
        }
        if(!isAniStarted)
        {
            startAnimation();
            isAniStarted = !isAniStarted;
        }
//        va.start();
    }

    public void startAnimation()
    {
        va.setDuration(500);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                highlightPos = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        va.start();

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (va != null) {
            if (visibility == GONE) {
                va.cancel();
            }else if(visibility == VISIBLE){
                va.start();
            }
        }

    }

    public void starta()
    {
        va.start();
    }
}

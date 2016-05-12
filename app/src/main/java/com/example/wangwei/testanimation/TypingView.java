package com.example.wangwei.testanimation;

import android.animation.AnimatorSet;
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
public class TypingView extends View {

    private int radius = 10;
    private Paint mPaint;
    private Integer pointHeight = -1;
    private Integer pointHeight2 = -1;
    private Integer pointHeight3 = -1;
    private int shift = 40;
    private boolean isAniStarted = false;

    public TypingView(Context context) {
        this(context,null);
    }

    public TypingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = shift;//getHeight();
        if(pointHeight == -1)
        {
            pointHeight = height - radius;
            pointHeight2 = height - radius;
            pointHeight3 = height - radius;
        }



        canvas.drawCircle(10 + radius, pointHeight, radius, mPaint);
        canvas.drawCircle(10 + radius + 2*radius + radius,pointHeight2,radius,mPaint);
        canvas.drawCircle(10 + radius + 2 * (2 * radius + radius), pointHeight3, radius, mPaint);
        if(!isAniStarted)
        {
            startAnimation(height);
            isAniStarted = !isAniStarted;
        }
    }

    public void startAnimation(final int height)
    {
        ValueAnimator va = ValueAnimator.ofInt(height - radius,radius,height - radius);
        ValueAnimator va2 = ValueAnimator.ofInt(height - radius,radius,height - radius);
        ValueAnimator va3 = ValueAnimator.ofInt(height - radius,radius,height - radius);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointHeight = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointHeight2 = (int) animation.getAnimatedValue();
            }
        });
        va3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointHeight3 = (int) animation.getAnimatedValue();
            }
        });
//        va.addUpdateListener(new AUL(pointHeight));
//        va.addUpdateListener(new AUL(pointHeight2));
//        va.addUpdateListener(new AUL(pointHeight3));
        va.setDuration(1000);
        va2.setDuration(1000);
        va3.setDuration(1000);

        va.setRepeatCount(ValueAnimator.INFINITE);
        va2.setRepeatCount(ValueAnimator.INFINITE);
        va3.setRepeatCount(ValueAnimator.INFINITE);

        va2.setStartDelay(200);
        va3.setStartDelay(400);

        AnimatorSet as = new AnimatorSet();
        as.play(va).with(va2).with(va3);
        as.start();
    }

    class AUL implements ValueAnimator.AnimatorUpdateListener {

        private Integer i;

        public AUL(Integer i){
            this.i = i;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            i = (int) animation.getAnimatedValue();
        }
    }
}

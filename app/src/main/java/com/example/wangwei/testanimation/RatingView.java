package com.example.wangwei.testanimation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by wangwei on 16/5/11.
 */
public class RatingView extends FrameLayout{

    private ArrayList<RatingBar> ratingBars;
    private int STROKE_OFFSET = 10;

    private int mCenterX;
    private int mCenterY;

    private static final long ROTATING_ANIMATION_DURATION = 3000L;

    private static final long RATING_ANIMATION_DURATION = 1000L;

    private static final long TEXT_ANIMATION_DURATION = 1000L;

    private ValueAnimator rotateAnimator, ratingAnimator, titleAnimator;

    private float rotateAngle;
    private int ratingGap = -1, textAlpha = 0;
    private boolean isShow = false;

    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        ratingBars = new ArrayList<>();
        ratingBars.add(new RatingBar("哈哈",10));
        ratingBars.add(new RatingBar("嘿嘿",10));
        ratingBars.add(new RatingBar("呵呵",10));
//        initRatingBars();
        initRotatingAnimation();
        initTitleAnimation();
        initRatingAnimation();
    }

    private void initRatingAnimation() {
        ratingAnimator = ValueAnimator.ofInt(0,9);
        ratingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ratingGap = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        ratingAnimator.setDuration(RATING_ANIMATION_DURATION);
        ratingAnimator.setInterpolator(new LinearInterpolator());
    }

    private void initTitleAnimation() {
        titleAnimator = ValueAnimator.ofInt(0,255);
        titleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        titleAnimator.setDuration(TEXT_ANIMATION_DURATION);
        titleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void initRotatingAnimation() {
        rotateAnimator = ValueAnimator.ofFloat(0.0f, 360f * 5);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.setDuration(ROTATING_ANIMATION_DURATION);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                titleAnimator.start();
                ratingAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void addRatingBar(RatingBar rb)
    {
        ratingBars.add(rb);
    }

    private void initRatingBars(){
        int dividePart = ratingBars.size();

        int sweepAngle = dividePart == 1 ? 360 : (360 - dividePart * STROKE_OFFSET) / dividePart;

        int rotateOffset = dividePart == 1 ? 90 : 90 + sweepAngle / 2;

        for(int i = 0; i < dividePart; i++){
            float startAngle = i * (sweepAngle + STROKE_OFFSET) - rotateOffset;
            RatingBar ratingBar = ratingBars.get(i);

            if(dividePart == 1)
            {
                ratingBar.setIsSingle(true);
            }
            ratingBar.setmCenterX(mCenterX);
            ratingBar.setmCenterY(mCenterY);
            ratingBar.setmStartAngle(startAngle);
            ratingBar.setmSweepAngle(sweepAngle);

            ratingBar.init();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        initRatingBars();
    }

    public void clear() {
        isShow = false;
        ratingGap = -1;
        textAlpha = 0;
    }

    public void show() {
        if(ratingBars.size() == 0){
            return;
        }
        initRatingBars();
        rotateAnimator.start();
        isShow = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow){
            canvas.save();
            canvas.rotate(rotateAngle, mCenterX, mCenterY);
            for(RatingBar ratingBar : ratingBars){
                ratingBar.drawOutline(canvas);
            }
            canvas.restore();

            canvas.save();
            canvas.rotate(-rotateAngle, mCenterX, mCenterY);
            for (RatingBar ratingBar : ratingBars) {
                ratingBar.drawUnRate(canvas);
                ratingBar.drawShadow(canvas);
            }
            canvas.restore();

            if(ratingGap != -1){
                for(RatingBar ratingBar : ratingBars){
                    for(int rate = 0; rate < ratingBar.getmRate(); rate++){
                        if(rate <= ratingGap){
                            ratingBar.drawRate(canvas, rate);
                        }
                    }
                }
            }

            for(RatingBar ratingBar : ratingBars){
                ratingBar.drawTitle(canvas,textAlpha);
            }
        }
    }
}

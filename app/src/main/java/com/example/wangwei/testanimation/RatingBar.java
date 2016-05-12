package com.example.wangwei.testanimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;

import java.util.ArrayList;

/**
 * Created by wangwei on 16/5/11.
 */
public class RatingBar {

    private Paint outlinePaint,unRatedPaint,ratedPaint,shadowPaint;
    private TextPaint titlePaint;
    
    
    private int mCenterX, mCenterY;
    
    private int maxRate = 10;

    private int mRadius;
    private int textHeight,ratingBarHeight,shadowHeight,outlineHeight;
    private float mStartAngle, mSweepAngle;

    private RectF outlineOval, ratingOval, shadowOval;

    private ArrayList<Rate> rates;

    private int mRate;

    private String mTitle;

    private boolean isSingle = false;

    private final int ITEM_OFFSET = 1;
    private boolean isShowTitle = true;

    public RatingBar(String title, int mRate){
        outlinePaint = new Paint();
        unRatedPaint = new Paint();
        ratedPaint = new Paint();
        shadowPaint = new Paint();
        titlePaint = new TextPaint();
        this.mTitle = title;
        this.mRate = mRate;
    }
    
    public void init(){
        initRatingBar();
        initOval();
        initPaint();
    }

    private void initOval() {
        mRadius = mCenterX < mCenterY ? mCenterX : mCenterY;
        textHeight = mRadius/10;
        ratingBarHeight = mRadius/10;
        shadowHeight = ratingBarHeight/3;
        outlineHeight = shadowHeight/3;

        int outlineRadius = mRadius - textHeight/2;
        int paddingRadius = outlineRadius - textHeight/2;
        int ratingBarRadius = paddingRadius - textHeight/2 - ratingBarHeight/2;
        int shadowRadius = ratingBarRadius - ratingBarHeight/2 - shadowHeight/2;

        outlineOval = new RectF();
        ratingOval = new RectF();
        shadowOval = new RectF();

        outlineOval.left = mCenterX - outlineRadius;
        outlineOval.top = mCenterY - outlineRadius;
        outlineOval.right = mCenterX + outlineRadius;
        outlineOval.bottom = mCenterY + outlineRadius;

        ratingOval.left = mCenterX - ratingBarRadius;
        ratingOval.top = mCenterY - ratingBarRadius;
        ratingOval.right = mCenterX + ratingBarRadius;
        ratingOval.bottom= mCenterY + ratingBarRadius;

        shadowOval.left = mCenterX - shadowRadius;
        shadowOval.top = mCenterY - shadowRadius;
        shadowOval.right = mCenterX + shadowRadius;
        shadowOval.bottom = mCenterY + shadowRadius;
    }

    private void initRatingBar() {
        rates = new ArrayList<>();

        float itemSweepAngle;
        if(isSingle){
            itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate))) / maxRate;
        }else {
            itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate - 1))) / maxRate;
        }

        for(int i = 0; i<maxRate; i++){
            float itemStartAngle = mStartAngle + i * (itemSweepAngle + ITEM_OFFSET);
            rates.add(new Rate(itemStartAngle,itemSweepAngle));
        }
    }

    private void initPaint() {
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(outlineHeight);
        outlinePaint.setColor(Color.RED);

        unRatedPaint.setAntiAlias(true);
        unRatedPaint.setStyle(Paint.Style.STROKE);
        unRatedPaint.setStrokeWidth(ratingBarHeight);
        unRatedPaint.setColor(Color.BLUE);

        ratedPaint.setAntiAlias(true);
        ratedPaint.setStyle(Paint.Style.STROKE);
        ratedPaint.setStrokeWidth(ratingBarHeight);
        ratedPaint.setColor(Color.GRAY);

        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(shadowHeight);
        shadowPaint.setColor(Color.GREEN);

        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(textHeight);
        titlePaint.setAlpha(255);
        titlePaint.setColor(Color.WHITE);
    }

    protected void drawOutline(Canvas canvas) {
        float circumference = (float) (Math.PI * (outlineOval.right - outlineOval.left));
        float textAngle = (360 / circumference) * titlePaint.measureText(getmTitle());

        float sweepAngle = (mSweepAngle - textAngle - 1 - 1) / 2;

        if(isShowTitle){
            float leftStartAngle = mStartAngle;
            canvas.drawArc(outlineOval, leftStartAngle, sweepAngle, false, outlinePaint);
            float rightStartAngle = mStartAngle + mSweepAngle - sweepAngle;
            canvas.drawArc(outlineOval, rightStartAngle, sweepAngle, false, outlinePaint);
        }else {
            canvas.drawArc(outlineOval,mStartAngle,mSweepAngle,false,outlinePaint);
        }
    }

    protected void drawTitle(Canvas canvas,int alpha){
        if(alpha > 0 && isShowTitle){
            Path path = new Path();
            float circumference = (float) (Math.PI * (outlineOval.right - outlineOval.left));
            float textAngle = (360 / circumference) * titlePaint.measureText(getmTitle());

            float startAngle = mStartAngle + mSweepAngle / 2 - textAngle / 2;

            if(isSingle){
//                path.addArc(outlineOval);// TODO: 16/5/12 when single
            }else {
                path.addArc(outlineOval, startAngle, mSweepAngle);
            }

            titlePaint.setAlpha(alpha);
            canvas.drawTextOnPath(mTitle,path,0,textHeight / 3,titlePaint);
        }
    }

    protected void drawRate(Canvas canvas,int index){
        if(index >= maxRate){
            return;
        }
        Rate arc = rates.get(index);
        arc.drawArc(canvas,ratingOval, ratedPaint);
    }

    protected void drawUnRate(Canvas canvas){
        for(Rate arc : rates){
            arc.drawArc(canvas,ratingOval,unRatedPaint);
        }
    }

    protected void drawShadow(Canvas canvas){
        for(Rate arc : rates){
            arc.drawArc(canvas,shadowOval,shadowPaint);
        }
    }

    public class Rate{
        private float startAngle, sweepAngle;

        public Rate(float startAngle, float sweepAngle){
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }

        public void drawArc(Canvas canvas, RectF oval, Paint paint){
            canvas.drawArc(oval,startAngle,sweepAngle,false,paint);
        }
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public Paint getOutlinePaint() {
        return outlinePaint;
    }

    public void setOutlinePaint(Paint outlinePaint) {
        this.outlinePaint = outlinePaint;
    }

    public Paint getUnRatedPaint() {
        return unRatedPaint;
    }

    public void setUnRatedPaint(Paint unRatedPaint) {
        this.unRatedPaint = unRatedPaint;
    }

    public Paint getRatedPaint() {
        return ratedPaint;
    }

    public void setRatedPaint(Paint ratedPaint) {
        this.ratedPaint = ratedPaint;
    }

    public Paint getShadowPaint() {
        return shadowPaint;
    }

    public void setShadowPaint(Paint shadowPaint) {
        this.shadowPaint = shadowPaint;
    }

    public TextPaint getTitlePaint() {
        return titlePaint;
    }

    public void setTitlePaint(TextPaint titlePaint) {
        this.titlePaint = titlePaint;
    }

    public int getmCenterX() {
        return mCenterX;
    }

    public void setmCenterX(int mCenterX) {
        this.mCenterX = mCenterX;
    }

    public int getmCenterY() {
        return mCenterY;
    }

    public void setmCenterY(int mCenterY) {
        this.mCenterY = mCenterY;
    }

    public int getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public int getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }

    public int getRatingBarHeight() {
        return ratingBarHeight;
    }

    public void setRatingBarHeight(int ratingBarHeight) {
        this.ratingBarHeight = ratingBarHeight;
    }

    public int getShadowHeight() {
        return shadowHeight;
    }

    public void setShadowHeight(int shadowHeight) {
        this.shadowHeight = shadowHeight;
    }

    public int getOutlineHeight() {
        return outlineHeight;
    }

    public void setOutlineHeight(int outlineHeight) {
        this.outlineHeight = outlineHeight;
    }

    public float getmStartAngle() {
        return mStartAngle;
    }

    public void setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    public float getmSweepAngle() {
        return mSweepAngle;
    }

    public void setmSweepAngle(float mSweepAngle) {
        this.mSweepAngle = mSweepAngle;
    }

    public RectF getOutlineOval() {
        return outlineOval;
    }

    public void setOutlineOval(RectF outlineOval) {
        this.outlineOval = outlineOval;
    }

    public RectF getRatingOval() {
        return ratingOval;
    }

    public void setRatingOval(RectF ratingOval) {
        this.ratingOval = ratingOval;
    }

    public RectF getShadowOval() {
        return shadowOval;
    }

    public void setShadowOval(RectF shadowOval) {
        this.shadowOval = shadowOval;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<Rate> rates) {
        this.rates = rates;
    }

    public int getITEM_OFFSET() {
        return ITEM_OFFSET;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmRate() {
        return mRate;
    }

    public void setmRate(int mRate) {
        this.mRate = mRate;
    }
}

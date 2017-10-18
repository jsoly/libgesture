package d.cityaurora.com.libgesture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jsj on 2017/10/16.
 */

public class GestureView extends View {



    private Paint mPaint;
    private PatternManager mPatternManager;
    private float downX;
    private float downY;
    private Callback mCallback;
    private boolean lock = false;
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(36);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int square = Math.min(getMeasuredWidth(),getMeasuredHeight());
        int y = (getMeasuredHeight()-square)*6/11;
        mPatternManager = new PatternManager(getMeasuredWidth(),getMeasuredHeight(),y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        mPatternManager.drawPattern(canvas,mPaint,downX,downY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(lock==true) return false;
        if(event.getPointerCount()>1)
            return false;
        downX = event.getX();
        downY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPatternManager.reset();
                mPatternManager.setText("");
            case MotionEvent.ACTION_MOVE:
                mPatternManager.isContain(downX,downY);
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                downX = 0;
                downY = 0;
                if(mCallback!=null)
                    mCallback.onFinish(mPatternManager.getPassword());
                break;
        }
        return true;
    }

    public void resetDelay(final String text, long time){
        lock();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                reset(text);
                unlock();
            }
        },time);
    }
    public void reset(){
        reset("");
    }
    public void reset(final String text){
        if(mPatternManager!=null){
            mPatternManager.reset();
            setText(text);
        }else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset(text);
                }
            },50);
        }
    }


    public void setText(String text){
        setText(text,false);
    }
    public void setTextError(String text){
        setText(text,true);
    }

    private void setText(String text,boolean isErrorText){
        if(mPatternManager==null)return;
        mPatternManager.setError(isErrorText);
        mPatternManager.setText(text);
        postInvalidate();
    }

    public void setErrorLine(boolean b){
        mPatternManager.setErrorLine(true);
        postInvalidate();
    }

    public interface Callback{
        void onFinish(String password);
    }

    public void addCallback(Callback callback){
        this.mCallback = callback;
    }


    public GestureView(Context context) {
        this(context,null);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public boolean isLock() {
        return lock;
    }

    public void lock() {
        this.lock = true;
    }
    public void unlock() {
        this.lock = false;
    }
}

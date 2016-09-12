package com.demo.ms.pullscaleviewdemo;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 *  pull scale head scroll view
 * Created by ms on 2016/9/9.
 */
public class PullScaleScrollView extends NestedScrollView {

    private final int mTouchSlop;
    private View mSonView;
    private View mGrandSon;
    private int mDefaultWidth;
    private int mDefaultHeight;

    public PullScaleScrollView(Context context) {
        this(context,null);
    }

    public PullScaleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()>0){
            //获取ScrollView中第一个子View(有且仅有一个)
            mSonView = getChildAt(0);
            //判断该View是不是ViewGroup子类
            if (mSonView instanceof ViewGroup){
                //判断是否有子View
                if (((ViewGroup) mSonView).getChildCount() > 0){
                    //获取子View中的第一个View（"孙"View），用来缩放
                    mGrandSon = ((ViewGroup) mSonView).getChildAt(0);
                }
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDefaultHeight == 0 && mDefaultWidth == 0){
            //获取View的原始默认高度
            mDefaultHeight = mGrandSon.getHeight();
            mDefaultWidth = mGrandSon.getWidth();
        }
        doScaleHeadView(ev);
        return super.onTouchEvent(ev);
    }

    int y = 0;

    /**
     * 根据触摸动作 做缩放
     * @param ev
     */
    private void doScaleHeadView(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录第一次手指按下的y值
                y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算 得到滑动过程中的差值
                int deltaY = y - (int)ev.getY();
                //重新记录手指位置
                y = (int) ev.getY();
                if (mSonView != null && mGrandSon != null && isNeedDoScale()) {
                    if (deltaY > 0){
                        //根据手指放大 缩小到原始位置
                        mGrandSon.getLayoutParams().height = Math.max(mGrandSon.getHeight() - Math.abs(deltaY),mDefaultHeight);
                        //请求重新布局
                        mGrandSon.requestLayout();
                    }else {
                        //根据手指滑动 放大View高度
                        mGrandSon.getLayoutParams().height = Math.max(mGrandSon.getHeight() + Math.abs(deltaY), mDefaultHeight);
                        //请求重新布局
                        mGrandSon.requestLayout();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mGrandSon != null) {
                    //手指抬起时，重新回到默认宽高
                    mGrandSon.getLayoutParams().width = mDefaultWidth;
                    mGrandSon.getLayoutParams().height = mDefaultHeight;
                    //请求重新布局
                    mGrandSon.requestLayout();
                }
                break;
        }
    }

    /**
     * 判断当前是不是需要做缩放
     *
     * @return 滑到顶部返回True
     */
    private boolean isNeedDoScale(){
        return getScrollY() == 0 || mDefaultHeight != mGrandSon.getHeight();
    }
}

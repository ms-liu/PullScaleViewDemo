package com.demo.ms.pullscaleviewdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.LinkedList;
import java.util.List;

/**
 * 自动轮播
 * Created by msliu on 2016/1/11.
 */
public class RecycleViewPager extends ViewPager implements View.OnTouchListener {

    private static final String TAG = "RecyleViewPager" ;
    private final Context mContext;


    private Handler mHandler = new Handler();

    private AutoRunTask mAutoRunTask;

    private List<String> mDatas;

    private List<View> mDots;

    /**
     * 上次位置
     */
    private int mOldPosition = 0;
    /**
     * 当前position
     */
    private int mCurrentPosition = 0;
    private RecycleViewPagerAdapter mAdapter;
    private OnItemClickLitener listener;

    public RecycleViewPager(Context context) {
        this(context, null);

    }
    public RecycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mOldPosition = 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRecyle();
    }


    /**
     * 启动轮播功能
     * @param context 上下文
     * @param datas 数据
     */
    public void startRecyle(Context context,List<String> datas){
        setData(context, datas);
        stopRecyle();
        if (datas != null && datas.size()>1) {
            mAutoRunTask = new AutoRunTask();
            mAutoRunTask.start();
        }

    }

    /**
     * 关闭轮播功能
     *
     */
    public void stopRecyle(){
        if (mAutoRunTask == null)
            return;
        mAutoRunTask.stop();
        if (getHandler() != null) {
            getHandler().removeCallbacks(mAutoRunTask);
        }
    }

    public void setDot(List<View> dots){
        mDots = dots;
    }


    public void setData(Context context, List<String> datas) {
        if (null == datas || datas.size() < 0)
            return;
        mAdapter = new RecycleViewPagerAdapter(context, datas);
        setAdapter(mAdapter);
        if (datas.size()>1){
            setCurrentItem(10000 * datas.size());
            setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (null == mAutoRunTask){
            return  false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mAutoRunTask.stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mAutoRunTask.start();
                break;
        }
        return false;
    }


    private class RecylePagerChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            if (null != mDots && mDots.size() > 0){
                mCurrentPosition = position % mDots.size();
//                mDots.get(mCurrentPosition).setBackgroundResource(R.drawable.selected_point);
                if (mOldPosition >= 0)
//                    mDots.get(mOldPosition).setBackgroundResource(R.drawable.selected_point_white);
                mOldPosition = position %  mDots.size();
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 自动轮播任务
     */
    private class AutoRunTask implements Runnable{

        private boolean flag;

        @Override
        public void run() {
            if (flag){
                mHandler.removeCallbacks(this);
                int currentItem = getCurrentItem();
                currentItem++;
                setCurrentItem(currentItem);
                mHandler.postDelayed(AutoRunTask.this,4000);
            }
        }

        public void start(){
            if (!flag){
                mHandler.removeCallbacks(AutoRunTask.this);
                flag = true;
                RecylePagerChangeListener listener = new RecylePagerChangeListener();
                setOnPageChangeListener(listener);
                mHandler.postDelayed(AutoRunTask.this,4000);
            }
        }

        public void stop(){
            if (flag){
                flag = false;
                mHandler.removeCallbacks(this);
            }
        }

    }

    public  class RecycleViewPagerAdapter extends PagerAdapter {
        private List<String> mDatas;

        private Context mContext;
        /**
         *链表集合用于管理图片，达到复用
         */
        private LinkedList<SimpleDraweeView> convertView = new LinkedList<SimpleDraweeView>();



        public RecycleViewPagerAdapter(){

        }

        /**
         * 用于数据的构造
         * @param context 上下文
         * @param datas 数据
         */
        public RecycleViewPagerAdapter(Context context, List<String> datas){
            if (null != datas && datas.size() > 0)
                this.mDatas = datas;
            if (null != context)
                this.mContext = context;
        }

        @Override
        public int getCount() {
            if (null == mDatas || mDatas.size() <0)
                return 0;
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container,  final int position) {
            final int currentPosition = position % mDatas.size();
            SimpleDraweeView imageView = null;
            if (convertView.size() > 0){
                imageView = convertView.remove(0);
            }else {
                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(null);
                builder.setFailureImage(getResources().getDrawable(R.drawable.side_nav_bar));
                builder.setPlaceholderImage(getResources().getDrawable(R.drawable.side_nav_bar));
                imageView = new SimpleDraweeView(mContext,builder.build());
                imageView.setAspectRatio(1);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(v, currentPosition);
                    }
                }
            });
            container.addView(imageView);
            imageView.setImageURI(Uri.parse(mDatas.get(currentPosition)));
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 条目点击事件接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View v, int position);
    }

    /**
     * 设置轮播图中每个条目的点击监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickLitener listener){
        if (listener == null)
            return;
        this.listener = listener;
    }

    int downX = 0;
    int downY = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:         //父控件不可以去拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if(Math.abs(moveX-downX)<Math.abs(moveY-downY)){            //y轴上面移动的多，刷新操作，需要去拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {
                    if(moveX-downX>0 && getCurrentItem() == 0){
                       getParent().requestDisallowInterceptTouchEvent(false);
                  }else if(moveX-downX>0 && getCurrentItem()>0){
                       getParent().requestDisallowInterceptTouchEvent(true);
                  }else if(moveX-downX<0 && getCurrentItem() == mAdapter.getCount()-1){
                       getParent().requestDisallowInterceptTouchEvent(false);
                  }else if(moveX-downX<0 && getCurrentItem()<mAdapter.getCount()-1){
                       getParent().requestDisallowInterceptTouchEvent(true);
                  }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
        }
    }

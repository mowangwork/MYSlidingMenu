package com.mywork.myslidingmenulibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by 沫 on 2015/4/27.
 */
public class MYSlidingMenu extends RelativeLayout {
    private Context mContext;

    private SlidingMenuProxy proxy;
    private int mLeft = 0;
    private int mMoveX = 0;
    private int LeftWidth = -1;
    private int RightWidth = -1;

    private boolean mLockInterface = false;

    private ViewDragHelper mDragHelper;
    public MYSlidingMenu(Context context) {
        super(context);
        init(context,null);
    }

    public MYSlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MYSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context,AttributeSet attrs)
    {
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MYSlidingMenu);
        int leftId = a.getResourceId(R.styleable.MYSlidingMenu_leftmenu, 0);
        int rightId = a.getResourceId(R.styleable.MYSlidingMenu_rightmenu,0);
        LeftWidth = a.getDimensionPixelOffset(R.styleable.MYSlidingMenu_leftwidth,0);
        RightWidth = a.getDimensionPixelOffset(R.styleable.MYSlidingMenu_rightwidth,0);
        mLockInterface = a.getBoolean(R.styleable.MYSlidingMenu_lockinterface,false);
        a.recycle();
        proxy = SlidingMenuProxy.getSingleInstance(this.getContext());
        proxy.setLeftSlidingMenuView(leftId);
        proxy.setRightSlidingMenuView(rightId);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }
    public void setLeftView(int resId) {
        proxy.setLeftSlidingMenuView(resId);
    }
    public void setLeftView(int resId,int width) {
        proxy.setLeftSlidingMenuView(resId);
        LeftWidth = width;
        proxy.setmLeftWidth(LeftWidth);
    }
    public void setRightView(int resId) {
        proxy.setRightSlidingMenuView(resId);
    }
    public void setRightView(int resId,int width) {
        proxy.setRightSlidingMenuView(resId);
        RightWidth = width;
        proxy.setmRightWidth(RightWidth);
    }

    public void setLeftWidth(int leftWidth){
        LeftWidth = leftWidth;
    }
    public int getLeftWidth() {
        return LeftWidth;
    }
    public void setRightWidth(int rightWidth) {
        RightWidth = rightWidth;
    }
    public int getRightWidth(){
        return RightWidth;
    }

    public void setLockInterface(boolean islock) {
        mLockInterface = islock;
    }
    public boolean getLockInterface(){
        return mLockInterface;
    }

    public void addView(int resId) {
        View view = LayoutInflater.from(mContext).inflate(resId, null);
        if(view!=null) {
            addView(view);
        }
    }

    @Override
    public void addView(View child) {
        addView(child, -1);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(!proxy.IsInterfaceViewContent(child))
            super.addView(child,index,params);
        else
            proxy.setView(this,child,LeftWidth,RightWidth);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.equals(proxy.getInterfaceView());
        }
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            mMoveX += dx;
            if (mMoveX > LeftWidth)
                mMoveX = LeftWidth;
            else if (mMoveX < -RightWidth)
                mMoveX = -RightWidth;
            return proxy.getLeft(left, mLockInterface);
        }

        /**
         * view移动后的回调
         * @param left 移动后的view最新的left值
         * @param top 移动后的view最新的top值
         * @param dx x方向移动了多少
         * @param dy y方向移动了多少
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if(changedView == proxy.getInterfaceView()) {
                mLeft = left;
            }
            requestLayout();
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * 手指抬起回调
         * @param xvel x方向滑动的速度
         * @param yvel y方向滑动的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(!mLockInterface) {
                if ((proxy.getInterfaceView().getLeft() < LeftWidth / 2
                        && proxy.getInterfaceView().getLeft() > 0) ||
                        (proxy.getInterfaceView().getLeft() > -RightWidth / 2
                                && proxy.getInterfaceView().getLeft() < 0)) {
                    mDragHelper.smoothSlideViewTo(proxy.getInterfaceView(), 0, 0);
                } else if (proxy.getInterfaceView().getLeft() >= LeftWidth / 2) {
                    mDragHelper.smoothSlideViewTo(proxy.getInterfaceView(), LeftWidth, 0);
                } else if (proxy.getInterfaceView().getLeft() <= -RightWidth / 2) {
                    mDragHelper.smoothSlideViewTo(proxy.getInterfaceView(), -RightWidth, 0);
                }
            } else {
                if(mMoveX > 0) {
                    if (proxy.getLeftSlidingMenuView().getLeft() < -LeftWidth / 2) {
                        mMoveX = 0;
                        mDragHelper.smoothSlideViewTo(proxy.getLeftSlidingMenuView(), -LeftWidth, 0);
                    } else {
                        mMoveX = LeftWidth;
                        mDragHelper.smoothSlideViewTo(proxy.getLeftSlidingMenuView(), 0, 0);
                    }
                } else {
                    if (proxy.getInterfaceView().getWidth() - proxy.getRightSlidingMenuView().getLeft() < RightWidth / 2) {
                        mMoveX = 0;
                        mDragHelper.smoothSlideViewTo(proxy.getRightSlidingMenuView(), proxy.getInterfaceView().getWidth(), 0);
                    } else {
                        mMoveX = -RightWidth;
                        mDragHelper.smoothSlideViewTo(proxy.getRightSlidingMenuView(), proxy.getInterfaceView().getWidth() - RightWidth, 0);
                    }
                }
            }
            ViewCompat.postInvalidateOnAnimation(MYSlidingMenu.this);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if(mLockInterface)
            proxy.LayoutWithLockInterface(mMoveX);
        else
            proxy.Layout(mLeft);
    }

    /**
     * 对Scroller的封装
     */
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}

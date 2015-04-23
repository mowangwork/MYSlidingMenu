package com.mywork.myslidingmenulibrary;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 沫 on 2015/4/15.
 */
public class MYMainView extends ViewGroup {
    private Context mContext;
    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    protected int mActivePointerId = INVALID_POINTER;
    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    /**
     * Position of the last motion event.
     */
    private float mLastMotionX;
    private float mLastMotionY;

    private Scroller mScroller;

    private MYMenuView mMenu;
    private MYSlidingMenu.Mode mMode;
    /**
     * Determines speed during touch scrolling
     */
    protected VelocityTracker mVelocityTracker;

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private boolean mIsBeingDragged = false;

    public MYMainView(Context context) {
        super(context);
        init(context);
    }

    public MYMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MYMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View v = getChildAt(0);
        v.layout(l, t, r, b);
    }

    /**
     * 添加View的方法
     */
    public void init(Context mContext) {
        this.mContext = mContext;
        mScroller = new Scroller(mContext);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSpecSize, heightSpecSize);
        View child=getChildAt(0);
        child.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();

        /*
        * Shortcut the most recurring case: the user is in the dragging
        * state and he is moving his finger.  We want to intercept this
        * motion.
        */
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        switch(action & MotionEventCompat.ACTION_MASK)
        {
            case MotionEvent.ACTION_MOVE:
                mIsBeingDragged = true;
                determineDrag(event);
                break;
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = MotionEventCompat.getX(event, index);
                mLastMotionY = MotionEventCompat.getY(event, index);
                /*
                * If being flinged and user touches the screen, initiate drag;
                * otherwise don't.  mScroller.isFinished should be false when
                * being flinged.
                */
                mIsBeingDragged = !mScroller.isFinished();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch(action & MotionEventCompat.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                if (getChildCount() == 0) {
                    return false;
                }
                if ((mIsBeingDragged = !mScroller.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();

                }
                // Remember where the motion event started
                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                mLastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // Scroll to follow the motion event
                int activePointerIndex = getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                float x = MotionEventCompat.getX(event, activePointerIndex);
                final float deltaX = mLastMotionX - x;
                final ViewConfiguration configuration = ViewConfiguration.get(mContext);
                if(!mIsBeingDragged && Math.abs(deltaX) > configuration.getScaledTouchSlop())
                mLastMotionX = x;
                float oldScrollX = getScrollX();
                float scrollX = oldScrollX + deltaX;
                if(mMode== MYSlidingMenu.Mode.LEFT) {
                    if(scrollX>mMenu.getContentWidth())
                        scrollX=mMenu.getContentWidth();
                    if(scrollX>0)
                        scrollX=0;
                } else if(mMode== MYSlidingMenu.Mode.RIGHT) {
                    if(scrollX > this.getWidth() - mMenu.getContentWidth())
                        scrollX = mMenu.getContentWidth();
                    if(scrollX < 0)
                        scrollX= 0;
                }
                mLastMotionX += scrollX - (int) scrollX;
                ScrollMenuTo((int) scrollX,0);
                break;
            case MotionEvent.ACTION_UP:
                if(mMenu.isOpenBehind(this))
                {
                    ScrollMenuTo(0, 0);
                }
                else
                {
                    if(mMode == MYSlidingMenu.Mode.LEFT)
                        ScrollMenuTo(-mMenu.getContentWidth(),0);
                    else if(mMode == MYSlidingMenu.Mode.RIGHT)
                        ScrollMenuTo(mMenu.getContentWidth(),0);
                }
                mActivePointerId = INVALID_POINTER;
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                mIsBeingDragged = false;
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int indexx = MotionEventCompat.getActionIndex(event);
                mLastMotionX = MotionEventCompat.getX(event, indexx);
                mActivePointerId = MotionEventCompat.getPointerId(event, indexx);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                int pointerIndex = getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = MotionEventCompat.getX(event, pointerIndex);
                break;

        }
        return true;
    }

    public void ScrollMenuTo(int fx,int fy)
    {
        //滚动ViewAbove
        this.smoothScrollTo(fx, fy);
        mMenu.smoothScrollTo(this,fx,fy);
    }
    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);

        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    //确定拖动
    private void determineDrag(MotionEvent ev) {
        final int activePointerId = mActivePointerId;
        final int pointerIndex = getPointerIndex(ev, activePointerId);
        if (activePointerId == INVALID_POINTER || pointerIndex == INVALID_POINTER)
            return;
        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float dx = x - mLastMotionX;
        final float xDiff = Math.abs(dx);
        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float dy = y - mLastMotionY;
        final float yDiff = Math.abs(dy);
        mLastMotionX = x;
        mLastMotionY = y;
    }
    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1)
            mActivePointerId = INVALID_POINTER;
        return activePointerIndex;
    }

    private boolean thisTouchAllowed(MotionEvent ev) {
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {

        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    public MYSlidingMenu.Mode getmMode() {
        return mMode;
    }

    public void setmMode(MYSlidingMenu.Mode mMode) {
        this.mMode = mMode;
    }

    public void setmMenu(MYMenuView mMenu) {
        this.mMenu = mMenu;
    }
}

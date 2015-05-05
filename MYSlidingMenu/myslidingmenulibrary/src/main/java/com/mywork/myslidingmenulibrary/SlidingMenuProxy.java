package com.mywork.myslidingmenulibrary;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.internal.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Inflater;

/**
 * Created by 沫 on 2015/4/27.
 */
public class SlidingMenuProxy {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private final static SlidingMenuProxy proxy = new SlidingMenuProxy();
    private static InterfaceView mInterfaceView;
    private static SlidingMenuView mLeftSlidingMenuView;
    private static SlidingMenuView mRightSlidingMenuView;
    private static int mLeftWidth;
    private static int mRightWidth;

    private static Context mContext;

    private SlidingMenuProxy(){ }

    public static SlidingMenuProxy getSingleInstance(Context context) {
        mContext = context;
        mInterfaceView = new InterfaceView(context);
        mLeftSlidingMenuView = new SlidingMenuView(context);
        mRightSlidingMenuView = new SlidingMenuView(context);
        return proxy;
    }

    public void setmLeftWidth(int leftWidth) {
        mLeftWidth = leftWidth;
        mLeftSlidingMenuView.setContentViewSize(leftWidth);
    }
    public void setmRightWidth(int rightWidth) {
        mRightWidth = rightWidth;
        mRightSlidingMenuView.setContentViewSize(rightWidth);
    }

    public void setView(RelativeLayout parent,int resId,int leftWidth,int rightWidth) {
        View view = LayoutInflater.from(mContext).inflate(resId, null);
        if(view!=null) {
            setView(parent, view, leftWidth,rightWidth);
        }
    }
    public void setView(RelativeLayout parent,View view,int leftWidth,int rightWidth) {
        mInterfaceView.setContentView(view);
        mInterfaceView.setId(generateViewId());
        mLeftSlidingMenuView.setContentViewSize(leftWidth);

        RelativeLayout.LayoutParams interfaceParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        parent.addView(mInterfaceView, interfaceParams);

        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(leftWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parent.addView(mLeftSlidingMenuView,leftParams);
        mRightSlidingMenuView.setContentViewSize(rightWidth);
        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(leftWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parent.addView(mRightSlidingMenuView,rightParams);


        interfaceParams = (RelativeLayout.LayoutParams) mInterfaceView.getLayoutParams();
        interfaceParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        interfaceParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mInterfaceView.setLayoutParams(interfaceParams);

        mLeftWidth = leftWidth;
        mRightWidth = rightWidth;
    }

    public InterfaceView getInterfaceView() {
        return mInterfaceView;
    }

    public void setLeftSlidingMenuView(int resId) {
        if(resId==0) {
            return;
        }
        View view = LayoutInflater.from(mContext).inflate(resId, null);
        if(view!=null) {
            setLeftSlidingMenuView(view);
        }
    }
    public void setLeftSlidingMenuView(View view) {
        mLeftSlidingMenuView.setContentView(view);
    }
    public SlidingMenuView getLeftSlidingMenuView() {
        return mLeftSlidingMenuView;
    }

    public void setRightSlidingMenuView(int resId) {
        if(resId==0) {
            return;
        }
        View view = LayoutInflater.from(mContext).inflate(resId, null);
        if(view!=null) {
            setRightSlidingMenuView(view);
        }
    }
    public void setRightSlidingMenuView(View view) {
        mRightSlidingMenuView.setContentView(view);
    }
    public SlidingMenuView getRightSlidingMenuView() {
        return mRightSlidingMenuView;
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public boolean IsInterfaceViewContent(View content)
    {
        return !(content.equals(mInterfaceView)||content.equals(mLeftSlidingMenuView)||content.equals(mRightSlidingMenuView));
    }

    public void Layout(int left) {
        if (left > mLeftWidth)
            left = mLeftWidth;
        else if (left < -mRightWidth)
            left = -mRightWidth;

        int height = mInterfaceView.getMeasuredHeight();
        int left_left = left - mLeftWidth;
        int interface_left = left;
        int right_left = left + mInterfaceView.getMeasuredWidth();
        int right_right = right_left + mRightWidth;

        mLeftSlidingMenuView.layout(left_left, 0, left, height);
        mInterfaceView.layout(interface_left, 0, right_left, height);
        mRightSlidingMenuView.layout(right_left, 0, right_right, height);
    }

    public void LayoutWithLockInterface(int left) {
        if (left > mLeftWidth)
            left = mLeftWidth;
        else if (left < -mRightWidth)
            left = -mRightWidth;

        int width = mInterfaceView.getMeasuredWidth();
        int height = mInterfaceView.getMeasuredHeight();

        mLeftSlidingMenuView.layout(left - mLeftWidth, 0, left, height);
        mInterfaceView.layout(0, 0, width, height);
        mRightSlidingMenuView.layout(left + width, 0, left + width + mRightWidth, height);
        if(left>0) {
            if (mLeftWidth != 0)
                mInterfaceView.setAlpha((float) (1 - ((float) left / mLeftWidth) * 0.6));
        } else {
            if (mRightWidth != 0)
                mInterfaceView.setAlpha((float) (1 + ((float) left / mRightWidth) * 0.6));
        }
    }

    public int getLeft(int left,boolean isLockInterface) {
        if(isLockInterface)
            return 0;

        int mLeft;
        if (left > mLeftWidth)
            mLeft = mLeftWidth;
        else if (left < -mRightWidth)
            mLeft = -mRightWidth;
        else
            mLeft = left;
        return mLeft;
    }
}

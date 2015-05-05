package com.mywork.myslidingmenulibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 沫 on 2015/4/27.
 */
public class SlidingMenuView extends ViewGroup {
    private View ContentView;
    private int mWidth;
    public SlidingMenuView(Context context) {
        super(context);
    }

    public SlidingMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContentViewSize(int Width)
    {
        mWidth = Width;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(ContentView==null)
            return;
        int measureHeight = ContentView.getMeasuredHeight();
        ContentView.layout(0, 0, mWidth, measureHeight);
    }

    public View getContentView() {
        return ContentView;
    }

    public void setContentView(View contentView) {
        ContentView = contentView;
        addView(contentView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(mWidth, measureHeight);
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }
    public void setViewLeft(int left) {
        this.setLeft(left - ContentView.getWidth());
    }
}

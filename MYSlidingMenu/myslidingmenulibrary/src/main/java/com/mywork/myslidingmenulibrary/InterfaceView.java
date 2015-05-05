package com.mywork.myslidingmenulibrary;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 沫 on 2015/4/27.
 */
public class InterfaceView extends ViewGroup {
    private View ContentView;
    private Context mContext;
    public InterfaceView(Context context) {
        super(context);
        init(context);
    }

    public InterfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InterfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ContentView.layout(0, 0, r-l, b-t);
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
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSpecSize, heightSpecSize);
        ContentView.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }
}

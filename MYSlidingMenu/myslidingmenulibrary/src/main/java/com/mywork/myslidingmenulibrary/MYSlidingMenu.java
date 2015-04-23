package com.mywork.myslidingmenulibrary;

import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by 沫 on 2015/4/21.
 */
public class MYSlidingMenu extends RelativeLayout {
    private Context mContext;
    private MYMainView mainView;
    private MYMenuView menuView;

    public enum Mode{
        LEFT,RIGHT,LEFT_RIGHT
    };
    private Mode mMode;
    public Mode getmMode() {
        return mMode;
    }
    public void setmMode(Mode mMode) {
        this.mMode = mMode;
    }

    public MYSlidingMenu(Context context) {
        super(context);
        init(context);
    }

    public MYSlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MYSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context _context) {
        mContext = _context;
        menuView = new MYMenuView(_context);
        addView(menuView);

        mainView = new MYMainView(_context);
        addView(mainView);
        mainView.setmMenu(menuView);
    }

    @Override
    public void addView(View child) {

        if(child.equals(mainView)||child.equals(menuView))
            super.addView(child);
        else
            mainView.addView(child);
    }

    @Override
    public void addView(View child, int index) {

        if(child.equals(mainView)||child.equals(menuView))
            super.addView(child, index);
        else
            mainView.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {

        if(child.equals(mainView)||child.equals(menuView))
            super.addView(child, params);
        else
            mainView.addView(child, params);
    }

    public void setMenuView(Fragment menu) {

    }
    /* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(int)
	 */
    public void setMenuView(int id) {
        setMenuView(LayoutInflater.from(mContext).inflate(id, null));
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View)
     */
    public void setMenuView(View v) {
        setMenuView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    public void setMenuView(View v, LayoutParams params) {
        menuView.addView(v, params);
    }
}

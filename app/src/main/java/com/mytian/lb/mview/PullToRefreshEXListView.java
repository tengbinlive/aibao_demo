package com.mytian.lb.mview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.handmark.pulltorefresh.PullToRefreshListView;

public class PullToRefreshEXListView extends PullToRefreshListView {


    public PullToRefreshEXListView(Context context) {
        super(context);
    }

    public PullToRefreshEXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshEXListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshEXListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;   //表示向下传递事件
                }else{
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

        }

        return super.onInterceptTouchEvent(ev);
    }

}

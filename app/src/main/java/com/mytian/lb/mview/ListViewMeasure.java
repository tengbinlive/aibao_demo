package com.mytian.lb.mview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class ListViewMeasure extends ListView {


    public ListViewMeasure(Context context) {
        super(context);
    }

    public ListViewMeasure(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewMeasure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

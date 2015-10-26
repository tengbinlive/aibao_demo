package com.bin.kndle.activity;

import android.os.Bundle;
import android.os.Process;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.bin.kndle.AbsActivity;
import com.bin.kndle.App;
import com.bin.kndle.R;
import com.bin.kndle.fragment.ContentFragment;
import com.bin.kndle.mview.MDrawerView;
import com.core.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AbsActivity {

    /**
     * 两次点击返回之间的间隔时间, 这个时间内算为双击
     */
    private static final int EXIT_DOUBLE_CLICK_DIFF_TIME = 2000;
    @Bind(R.id.left_drawer)
    MDrawerView leftDrawer;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ContentFragment contentFragment;

    /**
     * 记录第一次点击返回的时间戳
     */
    private long exitClickTimestamp = 0L;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doubleTouchToExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 按两次退出键才退出.
     */
    public void doubleTouchToExit() {
        long clickTime = System.currentTimeMillis();
        // 如果双击时间在规定时间范围内,则退出
        if (clickTime - exitClickTimestamp < EXIT_DOUBLE_CLICK_DIFF_TIME) {
            App.getInstance().exit();
        } else {
            exitClickTimestamp = clickTime;
            CommonUtil.showToast(this, R.string.press_more_times_for_exit);
        }
    }

    @Override
    public void EInit() {
        super.EInit();
        setSwipeBackEnable(false);
        init();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    private void init() {
        initLayout();
        setActionBar();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        drawerLayout.setDrawerListener(new MyDrawerListener());//设置drawer的开关监听
        leftDrawer.setOnButtonClick(new MDrawerView.OnButtonClick() {
            @Override
            public void onClick(int type) {
            }
        });

    }

    private void toggerDrawer() {
        boolean is = drawerLayout.isDrawerOpen(leftDrawer);
        if (is) {
            drawerLayout.closeDrawer(leftDrawer);
        } else {
            drawerLayout.openDrawer(leftDrawer);
        }
    }

    private void initLayout() {
        contentFragment = new ContentFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentlayout, contentFragment)
                .commit();

    }

    private void setActionBar() {
        setToolbarIntermediateStrID(R.string.test_text);
        setToolbarLeft(R.mipmap.menu_normal);
        setToolbarLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggerDrawer();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * drawer的监听
     */
    private class MyDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {// 打开drawer
        }

        @Override
        public void onDrawerClosed(View drawerView) {// 关闭drawer
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
            //滑动的百分比，完全划出为 slideOffset =1.0
            if (slideOffset >= 1.0) {
                leftDrawer.setUserInfo();
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
        }
    }
}

package com.mytian.lb.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.core.util.CommonUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.event.SettingEventType;
import com.mytian.lb.fragment.AgreementFragment;
import com.mytian.lb.fragment.ContentFragment;
import com.mytian.lb.push.PushHelper;

import de.greenrobot.event.EventBus;


public class MainActivity extends AbsActivity {

    /**
     * 两次点击返回之间的间隔时间, 这个时间内算为双击
     */
    private static final int EXIT_DOUBLE_CLICK_DIFF_TIME = 2000;
//    @Bind(R.id.left_drawer)
//    MDrawerView leftDrawer;
//    @Bind(R.id.drawer_layout)
//    DrawerLayout drawerLayout;

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
        PushHelper.getInstance().initPush(getApplicationContext());
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
//        initDrawerLayout();
    }

//    private void initDrawerLayout() {
//        drawerLayout.setDrawerListener(new MyDrawerListener());//设置drawer的开关监听
//        leftDrawer.setOnButtonClick(new MDrawerView.OnButtonClick() {
//            @Override
//            public void onClick(int type) {
//            }
//        });
//
//    }

//    private void toggerDrawer() {
//        boolean is = drawerLayout.isDrawerOpen(leftDrawer);
//        if (is) {
//            drawerLayout.closeDrawer(leftDrawer);
//        } else {
//            drawerLayout.openDrawer(leftDrawer);
//        }
//    }

    private void initLayout() {
        contentFragment = new ContentFragment();
        contentFragment.setActionbarSet(new ContentFragment.ActionbarSet() {
            @Override
            public void OnIndexSet(int position) {
                actionbarIcon(position);
            }

            @Override
            public void OnChanger() {
            }

            @Override
            public void OnTitleSet(int title) {
                setToolbarLeftStrID(title);
            }

        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentlayout, contentFragment)
                .commit();

    }

    private void actionbarIcon(final int position) {
        if (position == ContentFragment.USER) {
            setToolbarRight(R.mipmap.icon_settings);
            setToolbarRightVisbility(View.VISIBLE, View.VISIBLE);
            setToolbarRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == settingEventType) {
                        View settingView = getToolbar().findViewById(R.id.toolbar_right_tv);
                        settingEventType = new SettingEventType(settingView);
                    }
                    EventBus.getDefault().post(settingEventType);
                }
            });
        } else if (position == ContentFragment.AGREEMENT || position == ContentFragment.HABIT) {
            setToolbarRight(R.mipmap.icon_friendslist);
            setToolbarRightVisbility(View.VISIBLE, View.VISIBLE);
            setToolbarRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == ContentFragment.AGREEMENT) {
                        if (!AgreementFragment.isSettingShow) {
                            Intent intent = new Intent(MainActivity.this, FriendslistActivity.class);
                            startActivity(intent);
                        }
                    } else if (position == ContentFragment.HABIT) {
                        Intent intent = new Intent(MainActivity.this, FriendslistActivity.class);
                        intent.putExtra("TYPE", position);
                        startActivity(intent);
                    }
                }
            });
        } else {
            setToolbarRightVisbility(View.INVISIBLE, View.VISIBLE);
        }
    }


    private SettingEventType settingEventType;

    private void setActionBar() {
        setToolbarLeft(R.mipmap.menu_normal);
        setToolbarLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toggerDrawer();
            }
        });

    }


//    /**
//     * drawer的监听
//     */
//    private class MyDrawerListener implements DrawerLayout.DrawerListener {
//        @Override
//        public void onDrawerOpened(View drawerView) {// 打开drawer
//        }
//
//        @Override
//        public void onDrawerClosed(View drawerView) {// 关闭drawer
//        }
//
//        @Override
//        public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
//            //滑动的百分比，完全划出为 slideOffset =1.0
//            if (slideOffset >= 1.0) {
//                leftDrawer.setUserInfo();
//            }
//        }
//
//        @Override
//        public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
//        }
//    }
}

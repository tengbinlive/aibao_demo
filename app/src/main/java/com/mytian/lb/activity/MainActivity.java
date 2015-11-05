package com.mytian.lb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.event.SettingEventType;
import com.mytian.lb.fragment.ContentFragment;
import com.mytian.lb.mview.CircleNetworkImageView;
import com.mytian.lb.mview.MDrawerView;
import com.nineoldandroids.animation.ValueAnimator;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class MainActivity extends AbsActivity {

    /**
     * 两次点击返回之间的间隔时间, 这个时间内算为双击
     */
    private static final int EXIT_DOUBLE_CLICK_DIFF_TIME = 2000;
    @Bind(R.id.left_drawer)
    MDrawerView leftDrawer;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.user_phone)
    TextView user_phone;
    @Bind(R.id.user_icon)
    CircleNetworkImageView user_icon;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.layout_user)
    LinearLayout layout_user;
    @BindDimen(R.dimen.actionbar_user_height)
    float EDITEXT_OFFER;
    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();

    private boolean isOpenUser;

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
        setUserInfo();
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
        contentFragment.setActionbarSet(new ContentFragment.ActionbarSet() {
            @Override
            public void OnIndexSet(int position) {
                actionbarAnim(position);
                actionbarIcon(position);
            }

            @Override
            public void OnChanger() {
                activityHandler.removeMessages(ANIMATION);
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

    private void actionbarAnim(int position) {
        if ((position == ContentFragment.USER)) {
            if (!isOpenUser)
                sendActionBarAnim(true);
        } else if (isOpenUser) {
            sendActionBarAnim(false);
        }
    }

    private void setUserInfo(){
        String name = App.getInstance().userResult.getName();
        name = StringUtil.isNotBlank(name)?name:"你猜";
        String head = App.getInstance().userResult.getHead();
        head = StringUtil.isNotBlank(head)?head:"";
        String phone = App.getInstance().userResult.getPhone();
        phone = StringUtil.isNotBlank(phone)?phone:"...";
        user_name.setText(name);
        user_phone.setText(phone);
        Glide.with(this).load(head).placeholder(R.mipmap.default_head).centerCrop().crossFade().into(user_icon);
    }

    private void actionbarIcon(int position) {
        if (position == ContentFragment.USER) {
            setToolbarRightVisbility(View.VISIBLE, View.VISIBLE);
        } else {
            setToolbarRightVisbility(View.INVISIBLE, View.VISIBLE);
        }
    }

    private void sendActionBarAnim(boolean is) {
        Message message = new Message();
        message.what = ANIMATION;
        message.obj = is;
        activityHandler.sendMessageDelayed(message, 800);
    }

    private SettingEventType settingEventType;

    private void setActionBar() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_user.getLayoutParams();
        lp.height = 0;
        layout_user.setLayoutParams(lp);
        layout_user.setVisibility(View.VISIBLE);
        setToolbarLeft(R.mipmap.menu_normal);
        setToolbarRight(R.mipmap.icon_settings);
        setToolbarLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggerDrawer();
            }
        });

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

    }

    /**
     * actionbar user info animation
     */
    private void userAnimation(boolean is) {
        ValueAnimator animation = ValueAnimator.ofFloat(is ? 0 : EDITEXT_OFFER, is ? EDITEXT_OFFER : 0);
        isOpenUser = is;
        if (is) {
            animation.setInterpolator(mInterpolator);
            animation.setDuration(450);
        } else {
            animation.setDuration(300);
        }
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_user.getLayoutParams();
                lp.height = (int) value;
                layout_user.setLayoutParams(lp);
            }
        });
        animation.start();
    }

    private final static int ANIMATION = 1;

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ANIMATION:
                    userAnimation((Boolean) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


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

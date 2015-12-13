package com.mytian.lb.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.CommonUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.adapter.MainViewPagerAdapter;
import com.mytian.lb.enums.BottomMenu;
import com.mytian.lb.event.SettingEventType;
import com.mytian.lb.fragment.AgreementFragment;
import com.mytian.lb.fragment.DtnameicFragment;
import com.mytian.lb.fragment.HabitFragment;
import com.mytian.lb.fragment.KindleFragment;
import com.mytian.lb.fragment.UserFragment;
import com.mytian.lb.push.PushHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.Bind;
import de.greenrobot.event.EventBus;


public class MainActivity extends AbsActivity {

    public final static int DYNAMIC = 0;
    public final static int AGREEMENT = DYNAMIC + 1;
    public final static int HABIT = AGREEMENT + 1;
    public final static int KINDLE = HABIT + 1;
    public final static int USER = KINDLE + 1;

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.viewpager_tab)
    SmartTabLayout viewPagerTab;

    public ArrayList<AbsFragment> fragments;

    /**
     * 两次点击返回之间的间隔时间, 这个时间内算为双击
     */
    private static final int EXIT_DOUBLE_CLICK_DIFF_TIME = 2000;

    /**
     * 记录第一次点击返回的时间戳
     */
    private long exitClickTimestamp = 0L;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (UserFragment.isSettingShow) {
                toggleSetting();
            } else {
                doubleTouchToExit();
            }
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
        setActionBar();
        initViewPager();
    }

    // 初始化资源
    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new DtnameicFragment());
        fragments.add(new AgreementFragment());
        fragments.add(new HabitFragment());
        fragments.add(new KindleFragment());
        fragments.add(new UserFragment());
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LinearLayout custom_ly = (LinearLayout) mInflater.inflate(R.layout.tab_main_icon, container, false);
                switch (position) {
                    case DYNAMIC:
                        setIconInfo(custom_ly, BottomMenu.DYNAMIC, true);
                        break;
                    case AGREEMENT:
                        setIconInfo(custom_ly, BottomMenu.AGREEMENT);
                        break;
                    case HABIT:
                        setIconInfo(custom_ly, BottomMenu.HABIT);
                        break;
                    case KINDLE:
                        setIconInfo(custom_ly, BottomMenu.KINDLE);
                        break;
                    case USER:
                        setIconInfo(custom_ly, BottomMenu.USER);
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return custom_ly;
            }
        });

        viewPagerTab.setViewPager(viewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fragments.get(position).EResetInit();
                setSelectedTabBg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void actionbarIcon(final int position) {
        if (position == USER) {
            setToolbarRight(R.mipmap.icon_settings);
            setToolbarRightVisbility(View.VISIBLE);
            setToolbarRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toSysSettingActivity();
                }
            });
        } else if (position == AGREEMENT || position == HABIT) {
            setToolbarRight(R.mipmap.icon_friendslist);
            setToolbarRightVisbility(View.VISIBLE);
            setToolbarRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == AGREEMENT) {
                        if (!AgreementFragment.isSettingShow) {
                            Intent intent = new Intent(MainActivity.this, FriendslistActivity.class);
                            startActivity(intent);
                        }
                    } else if (position == HABIT) {
                        Intent intent = new Intent(MainActivity.this, FriendslistActivity.class);
                        intent.putExtra("TYPE", position);
                        startActivity(intent);
                    }
                }
            });
        } else {
            setToolbarRightVisbility(View.GONE);
        }
    }

    private void toSysSettingActivity() {
        Intent intent = new Intent(this, SysSettingActivity.class);
        startActivity(intent);
    }

    private void toggleSetting() {
        if (null == settingEventType) {
            View settingView = getToolbar().findViewById(R.id.toolbar_right_tv);
            settingEventType = new SettingEventType(settingView);
        }
        EventBus.getDefault().post(settingEventType);
    }

    private SettingEventType settingEventType;

    private void setActionBar() {
        setToolbarLeft(0);
        setToolbarLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setIconInfo(ViewGroup custom_ly, BottomMenu menu, boolean isClick) {
        ImageView icon = (ImageView) custom_ly.findViewById(R.id.menu_icon);
        TextView title = (TextView) custom_ly.findViewById(R.id.menu_title);
        int titleStr = menu.getTitle();
        title.setText(titleStr);
        if (!isClick) {
            icon.setImageResource(menu.getResid_normal());
            title.setTextColor(menu.getTitle_colos_normal());
        } else {
            icon.setImageResource(menu.getResid_press());
            title.setTextColor(menu.getTitle_colos_press());
            setToolbarLeftStrID(titleStr);
        }
        custom_ly.setTag(R.id.main_tab_menu, menu);
    }

    private void setIconInfo(ViewGroup custom_ly, BottomMenu menu) {
        setIconInfo(custom_ly, menu, false);
    }

    private void setSelectedTabBg(int position) {
        int count = fragments.size();
        for (int i = 0; i < count; i++) {
            ViewGroup view = (ViewGroup) viewPagerTab.getTabAt(i);
            setTabViewBackground(view, i == position, position);
        }
    }

    private void setTabViewBackground(ViewGroup custom_ly, boolean isSelect, int position) {
        BottomMenu menu = (BottomMenu) custom_ly.getTag(R.id.main_tab_menu);
        ImageView icon = (ImageView) custom_ly.findViewById(R.id.menu_icon);
        TextView title = (TextView) custom_ly.findViewById(R.id.menu_title);
        int titleStr = menu.getTitle();
        title.setText(titleStr);
        if (!isSelect) {
            icon.setImageResource(menu.getResid_normal());
            title.setTextColor(menu.getTitle_colos_normal());
        } else {
            icon.setImageResource(menu.getResid_press());
            title.setTextColor(menu.getTitle_colos_press());
            setToolbarLeftStrID(titleStr);
            actionbarIcon(position);
        }
    }

}

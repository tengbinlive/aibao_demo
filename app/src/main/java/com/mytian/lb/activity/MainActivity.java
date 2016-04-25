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
import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.adapter.MainViewPagerAdapter;
import com.mytian.lb.enums.BottomMenu;
import com.mytian.lb.event.PushUserEventType;
import com.mytian.lb.fragment.DynameicFragment;
import com.mytian.lb.fragment.FriendslistFragment;
import com.mytian.lb.fragment.KindleFragment;
import com.mytian.lb.fragment.UserFragment;
import com.mytian.lb.push.PushCode;
import com.mytian.lb.push.PushHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * （动态&关注&kindle&帐号管理）主界面
 */
public class MainActivity extends AbsActivity {

    public final static int DYNAMIC = 0;
    public final static int FRIENDS = DYNAMIC + 1;
    public final static int KINDLE = FRIENDS + 1;
    public final static int USER = KINDLE + 1;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.viewpager_tab)
    SmartTabLayout viewPagerTab;
    @BindView(R.id.toolbar_tips_message)
    ImageView toolbarTipsMessage;
    ImageView menuFriendsTipsMessage;

    public ArrayList<AbsFragment> fragments;

    private int currentPosition;

    private boolean isToolbarTipsMessage; //是否显示红点

    /**
     * 两次点击返回之间的间隔时间, 这个时间内算为双击
     */
    private static final int EXIT_DOUBLE_CLICK_DIFF_TIME = 2000;

    /**
     * 记录第一次点击返回的时间戳
     */
    private long exitClickTimestamp = 0L;

    private int selectPager = 0 ;

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
        PushHelper.getInstance().sendPushState(PushHelper.STATE_UPLOAD_ID_NO);
        super.EInit();
        setSwipeBackEnable(false);
        init();
        String NOTICE_TYPE = getIntent().getStringExtra(PushCode.NOTICE_TYPE);
        toNOTICE_TYPE(NOTICE_TYPE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String NOTICE_TYPE = intent.getStringExtra(PushCode.NOTICE_TYPE);
        toNOTICE_TYPE(NOTICE_TYPE);
    }

    private void toNOTICE_TYPE(String type) {
        if (StringUtil.isBlank(type)) {
            return;
        }
        viewPager.setCurrentItem(FRIENDS);
        if (PushCode.FOLLOW_NOTICE.equals(type)) {
            toAddFollowActivity();
        }
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
        fragments.add(new DynameicFragment());
        fragments.add(new FriendslistFragment());
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
                    case FRIENDS:
                        setIconInfo(custom_ly, BottomMenu.AGREEMENT);
                        menuFriendsTipsMessage = (ImageView) custom_ly.findViewById(R.id.menu_message);
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
                currentPosition = position;
                fragments.get(position).EResetInit();
                setSelectedTabBg(position);
                actionbarIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void actionbarIcon(final int position) {
        if (position == FRIENDS) {
            setToolbarRightStrID(R.string.new_follow);
            setToolbarRightVisbility(View.VISIBLE);
            setToolbarRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toAddFollowActivity();
                    isToolbarTipsMessage = false;
                    toolbarTipsMessage.setVisibility(View.GONE);
                    menuFriendsTipsMessage.setVisibility(View.GONE);
                }
            });
            if (isToolbarTipsMessage) {
                toolbarTipsMessage.setVisibility(View.VISIBLE);
            }
        } else {
            setToolbarRightVisbility(View.GONE);
            toolbarTipsMessage.setVisibility(View.GONE);
        }
    }

    /**
     * 新的关注
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(PushUserEventType event) {
        isToolbarTipsMessage = true;
        if (currentPosition == FRIENDS) {
            toolbarTipsMessage.setVisibility(View.VISIBLE);
        }
        menuFriendsTipsMessage.setVisibility(View.VISIBLE);
    }

    private void toAddFollowActivity() {
        Intent intent = new Intent(this, AddFollowActivity.class);
        startActivity(intent);
    }

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
        if (count > selectPager) {
            ViewGroup view_select = (ViewGroup) viewPagerTab.getTabAt(selectPager);
            ViewGroup view_position = (ViewGroup) viewPagerTab.getTabAt(position);
            setTabViewBackground(view_select, false);
            setTabViewBackground(view_position, true);
            selectPager = position;
        }
    }

    private void setTabViewBackground(ViewGroup custom_ly, boolean isSelect) {
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
        }
    }

}

package com.mytian.lb.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.adapter.MainViewPagerAdapter;
import com.mytian.lb.enums.BottomMenu;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.Bind;


public class ContentFragment extends AbsFragment {

    public final static int DYNAMIC = 0;
    public final static int AGREEMENT = DYNAMIC+1;
    public final static int HABIT = AGREEMENT+1;
    public final static int KINDLE = HABIT+1;
    public final static int USER = KINDLE+1;

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.viewpager_tab)
    SmartTabLayout viewPagerTab;

    public ArrayList<AbsFragment> fragments;

    private ActionbarSet actionbarSet;

    public void setActionbarSet(ActionbarSet actionbarSet) {
        this.actionbarSet = actionbarSet;
    }

    public interface ActionbarSet {
        void OnTitleSet(int title);

        void OnIndexSet(int position);

        void OnChanger();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public void EInit() {
        init();
    }

    // 初始化资源
    private void init() {
        fragments = new ArrayList<>();
        fragments.add(new DtnameicFragment());
        fragments.add(new DtnameicFragment());
        fragments.add(new HabitFragment());
        fragments.add(new KindleFragment());
        fragments.add(new UserFragment());
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getFragmentManager(), fragments);
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
                if(state==2){
                    if (actionbarSet != null) {
                        actionbarSet.OnChanger();
                    }
                }
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
            if (actionbarSet != null) {
                actionbarSet.OnTitleSet(titleStr);
            }
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
            if (actionbarSet != null) {
                actionbarSet.OnTitleSet(titleStr);
                actionbarSet.OnIndexSet(position);
            }
        }
    }

}

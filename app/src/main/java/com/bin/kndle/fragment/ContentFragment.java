package com.bin.kndle.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.kndle.AbsFragment;
import com.bin.kndle.R;
import com.bin.kndle.adapter.MainViewPagerAdapter;
import com.bin.kndle.enums.BottomMenu;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.Bind;


public class ContentFragment extends AbsFragment {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.viewpager_tab)
    SmartTabLayout viewPagerTab;

    private ArrayList<AbsFragment> fragments;

    private ActionbarSet actionbarSet;

    public void setActionbarSet(ActionbarSet actionbarSet) {
        this.actionbarSet = actionbarSet;
    }

    public interface ActionbarSet{
        void OnTitleSet(int title);
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
        fragments.add(new HabitFragment());
        fragments.add(new UserFragment());
        fragments.add(new DtnameicFragment());
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LinearLayout custom_ly = (LinearLayout) mInflater.inflate(R.layout.tab_main_icon, container, false);
                switch (position) {
                    case 0:
                        setIconInfo(custom_ly, BottomMenu.DYNAMIC, true);
                        break;
                    case 1:
                        setIconInfo(custom_ly, BottomMenu.AGREEMENT);
                        break;
                    case 2:
                        setIconInfo(custom_ly, BottomMenu.HABIT);
                        break;
                    case 3:
                        setIconInfo(custom_ly, BottomMenu.KINDLE);
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
            if(actionbarSet!=null){
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
            setTabViewBackground(view, i == position);
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
            if(actionbarSet!=null){
                actionbarSet.OnTitleSet(titleStr);
            }
        }
    }

}

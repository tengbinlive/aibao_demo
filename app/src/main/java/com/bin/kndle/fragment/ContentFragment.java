package com.bin.kndle.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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


public class ContentFragment extends AbsFragment {

    private View rootView;

    private LayoutInflater mInflater;

    private SmartTabLayout viewPagerTab;
    private SmartTabLayout.TabProvider tabProvider;
    private ArrayList<AbsFragment> fragments;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void resetInit() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = getActivity().getLayoutInflater();
        rootView = mInflater.inflate(R.layout.fragment_main, container, false);
        init(rootView);
        return rootView;
    }

    // 初始化资源
    private void init(View viewGroup) {
        fragments = new ArrayList<AbsFragment>();
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getFragmentManager(), fragments);
        ViewPager viewPager = (ViewPager) viewGroup.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) viewGroup.findViewById(R.id.viewpager_tab);

        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LinearLayout custom_ly = (LinearLayout) mInflater.inflate(R.layout.tab_main_icon, container, false);
                switch (position) {
                    case 0:
                        setIconInfo(custom_ly, BottomMenu.INVESTMENT, true);
                        break;
                    case 1:
                        setIconInfo(custom_ly, BottomMenu.RECOMMEND);
                        break;
                    case 2:
                        setIconInfo(custom_ly, BottomMenu.GUNG);
                        break;
                    case 3:
                        setIconInfo(custom_ly, BottomMenu.ME);
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
                fragments.get(position).resetInit();
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
        title.setText(menu.getTitle());
        if (!isClick) {
            icon.setImageResource(menu.getResid_normal());
            title.setTextColor(menu.getTitle_colos_normal());
        } else {
            icon.setImageResource(menu.getResid_press());
            title.setTextColor(menu.getTitle_colos_press());
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
        title.setText(menu.getTitle());
        if (!isSelect) {
            icon.setImageResource(menu.getResid_normal());
            title.setTextColor(menu.getTitle_colos_normal());
        } else {
            icon.setImageResource(menu.getResid_press());
            title.setTextColor(menu.getTitle_colos_press());
        }
    }

}

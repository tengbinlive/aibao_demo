package com.bin.kndle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.bin.kndle.AbsFragment;

import java.util.ArrayList;


public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<AbsFragment> fragments;

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<AbsFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int num) {
        AbsFragment fragment = fragments.get(num);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public void refresh(int position) {
        fragments.get(position).EResetInit();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

}
package com.mytian.lb.fragment;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;

public class KindleFragment extends AbsFragment {

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

}

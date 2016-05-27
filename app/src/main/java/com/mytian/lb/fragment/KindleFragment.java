package com.mytian.lb.fragment;

import com.bin.AnimationSearchView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;

import butterknife.BindView;

public class KindleFragment extends AbsFragment {


    @BindView(R.id.searchView)
    AnimationSearchView searchView;

    @Override
    public boolean onBackPressed() {
        if (searchView.isAnimationOpen()) {
            searchView.closeAnimation();
            return true;
        }
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

}

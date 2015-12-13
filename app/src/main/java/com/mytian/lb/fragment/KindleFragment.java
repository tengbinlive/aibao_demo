package com.mytian.lb.fragment;

import android.view.View;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.rey.material.widget.FloatingActionButton;

import butterknife.Bind;

public class KindleFragment extends AbsFragment {

    @Bind(R.id.add_bt)
    FloatingActionButton add_bt;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_kndle;
    }

    @Override
    public void EInit() {
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_bt.setLineMorphingState((add_bt.getLineMorphingState() + 1) % 2, true);
            }
        });
    }

}

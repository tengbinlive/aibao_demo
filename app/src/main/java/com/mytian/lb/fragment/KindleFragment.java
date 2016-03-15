package com.mytian.lb.fragment;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.mview.MarkDownView;

import butterknife.Bind;

public class KindleFragment extends AbsFragment {

    @Bind(R.id.markdownview)
    MarkDownView markDownView;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

    @Override
    public void EInit() {
        super.EInit();
        markDownView.setStringSource("#aibao\n" +
                "\n" +
                "[download](http://fir.im/5xv4)");
    }
}

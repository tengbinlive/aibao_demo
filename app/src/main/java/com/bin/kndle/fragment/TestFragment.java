package com.bin.kndle.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bin.kndle.AbsFragment;
import com.bin.kndle.R;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;


public class TestFragment extends AbsFragment {

    private View rootView;

    private LayoutInflater mInflater;

    private LinearLayout action_bar;

    //列表
    private PullToRefreshListView mListView;

    private void initListView(View viewGroup) {

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mListView.onRefreshComplete();
            }
        });

    }

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
        rootView = mInflater.inflate(R.layout.fragment_test, container, false);
        init(rootView);
        return rootView;
    }

    // 初始化资源
    private void init(View viewGroup) {
        mListView = (PullToRefreshListView) viewGroup.findViewById(R.id.pull_refresh_list);
        initListView(viewGroup);
    }

}

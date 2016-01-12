package com.mytian.lb.fragment;

import android.widget.ListView;

import com.dao.UserAction;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.HabitAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.manager.UserActionDOManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class HabitFragment extends AbsFragment {

    @Bind(R.id.listview_pr)
    ListView listview;

    private FollowUser cureentParent;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_habit;
    }

    @Override
    public void EInit() {
        cureentParent = (FollowUser) getArguments().getSerializable(UserDetailActivity.USER);
        initListViewData(UserActionDOManager.getInstance().getArrayListHabit());
    }

    private void initListViewData(ArrayList<UserAction> _arrayList) {

        HabitAdapter mAdapter = new HabitAdapter((AbsActivity) getActivity(), cureentParent, _arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(listview);

        listview.setAdapter(animationAdapter);
    }

}

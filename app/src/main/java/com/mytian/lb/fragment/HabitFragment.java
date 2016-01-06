package com.mytian.lb.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.core.CommonResponse;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.HabitAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.user.UpdateActionResult;
import com.mytian.lb.bean.user.UserAction;
import com.mytian.lb.manager.UserManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class HabitFragment extends AbsFragment {

    @Bind(R.id.listview_pr)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    View llListEmpty;

    private ListView mActualListView;
    private FollowUser cureentParent;

    private ArrayList<UserAction> arrayList = new ArrayList<>();

    private void initListView() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                arrayList = null;
                updateHabit();
            }
        });

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

    }

    /**
     * 获取行为
     */
    private void updateHabit() {
        if (cureentParent == null) {
            setEndState();
            return;
        }
        UserManager manager = new UserManager();
        manager.updateHabit(getActivity(), cureentParent.getUid(), activityHandler, LOAD_HABIT);
    }

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
        initListView();
        updateHabit();
    }

    private static final int LOAD_HABIT = 0x01;//初始化数据处理
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case LOAD_HABIT:
                    loadData((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            UpdateActionResult result = (UpdateActionResult) resposne.getData();
            initListViewData(result.getList());
        }
        setEndState();
    }

    private void initListViewData(ArrayList<UserAction> _arrayList) {

        arrayList = _arrayList;

        HabitAdapter mAdapter = new HabitAdapter(getActivity(), arrayList);

        mAdapter.setIsOFFLINE(FollowUser.OFFLINE.equals(cureentParent.getIs_online()));

        mAdapter = new HabitAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);
    }

    private void setEndState() {
        listview.onRefreshComplete();
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

}

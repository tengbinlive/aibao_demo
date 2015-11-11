package com.mytian.lb.activity;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.adapter.HabitAdapter;
import com.mytian.lb.bean.follow.FollowBabyResult;
import com.mytian.lb.bean.habitUser.HabitResult;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class HabitActivity extends AbsActivity {

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    private ListView mActualListView;
    private HabitAdapter mAdapter;

    private ArrayList<HabitResult> arrayList;

    @Override
    public void EInit() {
        super.EInit();
        initListView();
        dialogShow();
        getListData(INIT_LIST);
    }


    private void initListView() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData(INIT_LIST);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData(LOAD_DATA);
            }
        });

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new HabitAdapter(this, arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        listview.setEmptyView(llListEmpty);
    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            arrayList = null;
        }
        activityHandler.sendEmptyMessageDelayed(state, 3000);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_follow;
    }

    @Override
    public void initActionBar() {
        setToolbarRight(R.mipmap.icon_settings);
    }


    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne) {
        dialogDismiss();
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        for (int i = 0; i < COUNT_MAX; i++) {
            arrayList.add(HabitResult.testData());
        }
        listview.onRefreshComplete();
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

}

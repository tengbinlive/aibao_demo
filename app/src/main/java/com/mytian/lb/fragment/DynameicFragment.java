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
import com.mytian.lb.adapter.DynamicAdapter;
import com.mytian.lb.bean.dymic.Dynamic;
import com.mytian.lb.bean.dymic.DynamicResult;
import com.mytian.lb.manager.DynamicManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 动态界面
 */
public class DynameicFragment extends AbsFragment {

    @BindView(R.id.listview_pr)
    PullToRefreshListView listview;
    @BindView(R.id.ll_listEmpty)
    View llListEmpty;

    private ListView mActualListView;
    private DynamicAdapter mAdapter;

    private ArrayList<Dynamic> arrayList;

    private DynamicManager manager = new DynamicManager();

    private int currentPager = 1;

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

        mAdapter = new DynamicAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            currentPager = 1;
            arrayList = null;
            listview.setMode(PullToRefreshBase.Mode.BOTH);
        }
        manager.dymicList(getActivity(), "" + currentPager, activityHandler, state);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_dynameic;
    }

    @Override
    public void EInit() {
        initListView();
        getListData(INIT_LIST);
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 12;//加载数据最大值
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
        listview.onRefreshComplete();
        if (resposne.isSuccess()) {
            DynamicResult result = (DynamicResult) resposne.getData();
            ArrayList<Dynamic> list = result.getList();
            int size = list == null ? 0 : list.size();
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            if (size > 0) {
                arrayList.addAll(list);
            }
            mAdapter.refresh(arrayList);
            if (size >= COUNT_MAX) {
                currentPager++;
            } else {
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        } else {
            //避免第一次应用启动时 创建fragment加载数据多次提示
        }
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

}

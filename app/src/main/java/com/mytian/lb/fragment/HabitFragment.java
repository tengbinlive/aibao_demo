package com.mytian.lb.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.util.StringUtil;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.adapter.HabitAdapter;
import com.mytian.lb.bean.habitUser.HabitResult;
import com.core.CommonResponse;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.mview.CircleNetworkImageView;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;

import butterknife.Bind;

public class HabitFragment extends AbsFragment {

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;
    @Bind(R.id.add_bt)
    FloatingActionButton add_bt;

    private ListView mActualListView;
    private HabitAdapter mAdapter;

    private ArrayList<HabitResult> arrayList = new ArrayList<>();

    private void initListView() {

        View headView = mInflater.inflate(R.layout.layout_user_inter,null);

        setUserInfo(headView);

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                arrayList = null;
                getListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new HabitAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        mActualListView.addHeaderView(headView);

        listview.setEmptyView(llListEmpty);
    }

    private void getListData() {
        int startIndex = arrayList == null || arrayList.size() <= 0 ? 0 : arrayList.size();
        activityHandler.sendEmptyMessageDelayed(startIndex==0?INIT_LIST:LOAD_DATA, 2500);
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
        initListView();
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_bt.setLineMorphingState((add_bt.getLineMorphingState() + 1) % 2, true);
            }
        });
        activityHandler.sendEmptyMessageDelayed(INIT_LIST, 500);
    }

    private void setUserInfo(View headView) {
        TextView user_name = (TextView) headView.findViewById(R.id.user_name);
        TextView user_phone = (TextView) headView.findViewById(R.id.user_phone);
        CircleNetworkImageView user_icon = (CircleNetworkImageView) headView.findViewById(R.id.user_icon);
        String name = "小明";
        name = StringUtil.isNotBlank(name) ? name : "你猜.";
        String head = "";
        head = StringUtil.isNotBlank(head) ? head : "";
        String phone = "...";
        phone = StringUtil.isNotBlank(phone) ? phone : "...";
        user_name.setText(name);
        user_phone.setText(phone);
//        Glide.with(this).load(R.mipmap.head_default).placeholder(R.mipmap.default_head).centerCrop().crossFade().into(user_icon);
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
                    loadData((CommonResponse) msg.obj, what);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne, int what) {
        dialogDismiss();
        if (what==INIT_LIST) {
            for (int i = 0; i < 6; i++) {
                if(arrayList==null){
                    arrayList = new ArrayList<>();
                }
                arrayList.add(HabitResult.testData(i));
            }
            mAdapter.refresh(arrayList);
        } else {
            //避免第一次应用启动时 创建fragment加载数据多次提示
        }
        listview.onRefreshComplete();
    }

}

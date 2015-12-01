package com.mytian.lb.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.activity.FriendslistActivity;
import com.mytian.lb.activity.MainActivity;
import com.mytian.lb.adapter.HabitAdapter;
import com.mytian.lb.bean.AgreementBean;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.HabitUserType;
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
    private View headView;
    private FollowUser cureentParent;

    private RoundedImageView user_icon;
    private TextView user_phone;
    private TextView user_name;

    private ArrayList<AgreementBean> arrayList = new ArrayList<>();

    private void initListView() {

        headView = mInflater.inflate(R.layout.layout_user_inter, null);
        initHeaderView(headView);

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

        mActualListView.addHeaderView(headView, null, true);

        mActualListView.setAdapter(animationAdapter);

        listview.setEmptyView(llListEmpty);

    }

    private void initHeaderView(View view){
         user_icon = (RoundedImageView) view.findViewById(R.id.user_icon);
         user_name = (TextView) view.findViewById(R.id.user_name);
         user_phone = (TextView) view.findViewById(R.id.user_phone);

        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFriendslist();
            }
        });
    }

    private void getListData() {
        int startIndex = arrayList == null || arrayList.size() <= 0 ? 0 : arrayList.size();
        activityHandler.sendEmptyMessageDelayed(startIndex == 0 ? INIT_LIST : LOAD_DATA, 2500);
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
        setUserInfo(null);
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_bt.setLineMorphingState((add_bt.getLineMorphingState() + 1) % 2, true);
            }
        });
    }

    void toFriendslist(){
        Intent intent = new Intent(getActivity(), FriendslistActivity.class);
        intent.putExtra("TYPE", MainActivity.HABIT);
        startActivity(intent);
    }

    private void setUserInfo(FollowUser userInfo) {
        String head = "";
        if(userInfo!=null) {
            cureentParent = userInfo;
            String name = cureentParent.getAlias();
            head = cureentParent.getHead_thumb();
            String phone = cureentParent.getPhone();
            user_name.setText(name);
            user_phone.setText(phone);
        }
        Glide.with(this).load(head).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_head).centerCrop().into(user_icon);
        arrayList = null;
        mAdapter.refresh(arrayList);
    }

    public void onEvent(HabitUserType event) {
        Message message = new Message();
        message.what = INIT_USER_INFO;
        message.obj = event.cureentParent;
        activityHandler.sendMessage(message);
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private static final int INIT_USER_INFO = 0x03;//设置用户信息
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj, what);
                    break;
                case INIT_USER_INFO:
                    setUserInfo((FollowUser) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne, int what) {
        dialogDismiss();
        listview.onRefreshComplete();
    }

}

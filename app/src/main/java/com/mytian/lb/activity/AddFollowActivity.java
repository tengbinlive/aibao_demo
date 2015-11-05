package com.mytian.lb.activity;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.CommonResponse;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.adapter.UserAdapter;
import com.mytian.lb.bean.follow.FollowListResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.manager.FollowManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class AddFollowActivity extends AbsActivity {

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    private ListView mActualListView;
    private UserAdapter mAdapter;

    private ArrayList<FollowUser> arrayList;
    private FollowManager manager = new FollowManager();
    private int currentPager = 1;

    @Override
    public void EInit() {
        super.EInit();
        initListView();
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

        mAdapter = new UserAdapter(this, arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        listview.setEmptyView(llListEmpty);
    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            currentPager = 1;
            arrayList = null;
        }
        manager.followList(this, "" + currentPager, "0", activityHandler, state);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_follow;
    }

    @Override
    public void initActionBar() {
        setToolbarLeftStrID(R.string.follow_new);
        setToolbarRightVisbility(View.VISIBLE, View.VISIBLE);
        setToolbarRightStrID(R.string.follow_add);
        setToolbarRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddFollow(FollowUser.testData());
            }
        });
    }

    public void dialogAddFollow(FollowUser followUser) {
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.dialog_follow, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView cancel = (TextView) convertView.findViewById(R.id.cancel);
        TextView addfollow = (TextView) convertView.findViewById(R.id.addfollow);
        ImageView background = (ImageView) convertView.findViewById(R.id.background);

        Glide.with(this).load(followUser.getHead_thumb()).crossFade().into(background);
        String nameStr = followUser.getName();
        String phoneStr = followUser.getPhone();
        if(StringUtil.isNotBlank(nameStr)){
            name.setText(nameStr);
        }
        if(StringUtil.isNotBlank(phoneStr)){
            phone.setText(phoneStr);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });
        addfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.show();

    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private static final int SHWO_DIALOG = 0X03;//添加dialog显示
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
        if (resposne.isSuccess()) {
            FollowListResult result = (FollowListResult) resposne.getData();
            ArrayList<FollowUser> list = result.getList();
            int size = list == null ? 0 : list.size();
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            if (size > 0) {
                arrayList.addAll(list);
                mAdapter.refresh(arrayList);
            }
            if (size >= COUNT_MAX) {
                currentPager++;
            } else {
                listview.enablePullLoad();
            }
        } else {
            //避免第一次应用启动时 创建fragment加载数据多次提示
        }
        listview.onRefreshComplete();
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

}

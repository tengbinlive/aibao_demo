package com.mytian.lb.activity;


import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.adapter.FollowAddAdapter;
import com.mytian.lb.bean.follow.FollowBabyResult;
import com.mytian.lb.bean.follow.FollowListResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.manager.FollowManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindDimen;
import cdflynn.android.library.crossview.CrossView;

public class AddFollowActivity extends AbsActivity {

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;
    @Bind(R.id.toolbar_left_btn)
    TextView toolbarLeftBtn;
    @Bind(R.id.add_bt)
    CrossView addBt;
    @Bind(R.id.search_key_aibao)
    TextView searchKeyAibao;
    @Bind(R.id.search_key_maibao)
    TextView searchKeyMaibao;
    @Bind(R.id.search_key)
    EditText searchKey;
    @Bind(R.id.search_layout_aibao)
    LinearLayout search_layout_aibao;
    @Bind(R.id.search_layout_maibao)
    LinearLayout search_layout_maibao;

    private ListView mActualListView;
    private FollowAddAdapter mAdapter;

    private ArrayList<FollowUser> arrayList;
    private FollowManager manager = new FollowManager();
    private int currentPager = 1;

    @Bind(R.id.layout_follow_search)
    LinearLayout layout_follow_search;
    @BindDimen(R.dimen.actionbar_search_height)
    float EDITEXT_OFFER;
    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();

    private boolean isOpenActionbar;

    @Override
    public void EInit() {
        super.EInit();
        initListView();
        iniView();
        dialogShow();
        getListData(INIT_LIST);
    }

    private void iniView() {
        searchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchKeyAibao.setText(s);
                searchKeyMaibao.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        search_layout_aibao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = searchKeyAibao.getText().toString();
                if(StringUtil.isBlank(phone)){
                    AnimationHelper.getInstance().viewAnimationQuiver(searchKey);
                    CommonUtil.showToast(R.string.error_hint_phone);
                    return;
                }
                if (!StringUtil.checkMobile(phone)) {
                    AnimationHelper.getInstance().viewAnimationQuiver(searchKeyAibao);
                    CommonUtil.showToast(R.string.error_hint_phone);
                    return;
                }
                dialogShow();
                manager.followgetbaby(AddFollowActivity.this, phone, activityHandler, FOLLOW_GETBABY);
            }
        });
        search_layout_maibao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = searchKeyMaibao.getText().toString();
                if(StringUtil.isBlank(phone)){
                    AnimationHelper.getInstance().viewAnimationQuiver(searchKey);
                    CommonUtil.showToast(R.string.error_hint_phone);
                    return;
                }
                if (!StringUtil.checkMobile(phone)) {
                    AnimationHelper.getInstance().viewAnimationQuiver(searchKeyMaibao);
                    CommonUtil.showToast(R.string.error_hint_phone);
                    return;
                }
                dialogShow();
                manager.followgetbaby(AddFollowActivity.this, phone, activityHandler, FOLLOW_GETBABY);
            }
        });
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

        mAdapter = new FollowAddAdapter(this, arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        listview.setEmptyView(llListEmpty);
    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            currentPager = 1;
            arrayList = null;
            listview.setMode(PullToRefreshBase.Mode.BOTH);
        }
        manager.followList(this, "" + currentPager, "0", activityHandler, state);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_follow;
    }

    @Override
    public void initActionBar() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_follow_search.getLayoutParams();
        lp.height = 0;
        layout_follow_search.setLayoutParams(lp);
        layout_follow_search.setVisibility(View.VISIBLE);
        toolbarLeftBtn.setText(R.string.follow_new);
        setToolbarLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addBt.setOnClickListener(mCrossViewClickListener);
    }

    private final View.OnClickListener mCrossViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int state = addBt.toggle();
            actionbarAnimation(state == 1);

        }
    };

    public void dialogAddFollow(final FollowBabyResult followUser) {
        if (null == followUser) {
            return;
        }
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.dialog_follow, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView cancel = (TextView) convertView.findViewById(R.id.cancel);
        TextView addfollow = (TextView) convertView.findViewById(R.id.addfollow);
        final EditText desc_et = (EditText) convertView.findViewById(R.id.desc_et);
        ImageView background = (ImageView) convertView.findViewById(R.id.background);

        Glide.with(this).load(followUser.getBaby().getHeadThumb()).crossFade().into(background);
        String nameStr = followUser.getBaby().getAlias();
        String phoneStr = followUser.getBaby().getPhone();
        if (StringUtil.isNotBlank(nameStr)) {
            name.setText(nameStr);
        }
        if (StringUtil.isNotBlank(phoneStr)) {
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
                dialogShow(R.string.hint_add);
                manager.followAdd(AddFollowActivity.this, ""+followUser.getBaby().getUid(),""+followUser.getBaby().getRelationId(),desc_et.getText().toString(),activityHandler, FOLLOW_ADD);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.show();

    }

    /**
     * actionbar user info animation
     */
    private void actionbarAnimation(boolean is) {
        ValueAnimator animation = ValueAnimator.ofFloat(is ? 0 : EDITEXT_OFFER, is ? EDITEXT_OFFER : 0);
        isOpenActionbar = is;
        if (is) {
            animation.setInterpolator(mInterpolator);
            animation.setDuration(450);
        } else {
            animation.setDuration(300);
        }
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_follow_search.getLayoutParams();
                lp.height = (int) value;
                layout_follow_search.setLayoutParams(lp);
            }
        });
        animation.start();
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int FOLLOW_GETBABY = 0x03;//查找麦宝账户信息
    private static final int FOLLOW_ADD = 0x04;//添加关注
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private static final int SHOW_DIALOG = 0X05;//添加dialog显示
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj);
                    break;
                case FOLLOW_GETBABY:
                    loadBabyInfo((CommonResponse) msg.obj);
                    break;
                case FOLLOW_ADD:
                    loadAdd((CommonResponse) msg.obj);
                    break;
                case SHOW_DIALOG:
                    dialogAddFollow((FollowBabyResult) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne) {
        dialogDismiss();
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
            }else{
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
        listview.onRefreshComplete();
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

    private void loadAdd(CommonResponse resposne) {
        dialogDismiss();
        if (resposne.isSuccess()) {
            CommonUtil.showToast(resposne.getMsg());
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void loadBabyInfo(CommonResponse resposne) {
        dialogDismiss();
        if (resposne.isSuccess()) {
            FollowBabyResult result = (FollowBabyResult) resposne.getData();
            Message message = new Message();
            message.what = SHOW_DIALOG;
            message.obj = result;
            activityHandler.sendMessage(message);
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    @Override
    public void onBackPressed() {
        if (isOpenActionbar) {
            addBt.toggle();
            actionbarAnimation(false);
            return;
        }
        super.onBackPressed();
    }

}

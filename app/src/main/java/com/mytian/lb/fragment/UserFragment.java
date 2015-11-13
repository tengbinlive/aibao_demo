package com.mytian.lb.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.StringUtil;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.activity.AddFollowActivity;
import com.mytian.lb.activity.LoginActivity;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.adapter.UserAdapter;
import com.mytian.lb.bean.follow.FollowListResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.SettingEventType;
import com.mytian.lb.event.UserEventType;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.FollowManager;
import com.mytian.lb.mview.ClipRevealFrame;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.OnClick;

public class UserFragment extends AbsFragment {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.name_value)
    TextView nameValue;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.phone_value)
    TextView phoneValue;
    @Bind(R.id.birthday)
    TextView birthday;
    @Bind(R.id.birthday_value)
    TextView birthdayValue;
    @Bind(R.id.gender)
    TextView gender;
    @Bind(R.id.gender_value)
    TextView genderValue;
    @Bind(R.id.integral)
    TextView integral;
    @Bind(R.id.integral_value)
    TextView integralValue;
    @Bind(R.id.layout_setting)
    ClipRevealFrame layoutSetting;

    private static boolean isSettingShow;

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    private ListView mActualListView;
    private UserAdapter mAdapter;

    private ArrayList<FollowUser> arrayList;
    private FollowManager manager = new FollowManager();
    private int currentPager = 1;
    private View settingAchor;

    //user
    private static boolean isOpenUser;//只执行一次动画
    @Bind(R.id.user_phone)
    TextView user_phone;
    @Bind(R.id.user_icon)
    RoundedImageView user_icon;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.layout_user)
    LinearLayout layout_user;
    @BindDimen(R.dimen.actionbar_user_height)
    float EDITEXT_OFFER;
    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();

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

        View headView = mInflater.inflate(R.layout.layout_user_new_messge, null);

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new UserAdapter((AbsActivity) getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        mActualListView.addHeaderView(headView);

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddFollowActivity();
            }
        });

    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            currentPager = 1;
            listview.setMode(PullToRefreshBase.Mode.BOTH);
            arrayList = null;
        }
        manager.followList(getActivity(), "" + currentPager, "1", activityHandler, state);
    }

    private void toAddFollowActivity() {
        Intent intent = new Intent(getActivity(), AddFollowActivity.class);
        startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public void EInit() {
        initListView();
        setUserInfo();
        getListData(INIT_LIST);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sendActionBarAnim();
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }

    private void setUserInfo() {
        String name = App.getInstance().userResult.getParent().getName();
        name = StringUtil.isNotBlank(name) ? name : "你猜.";
        String phone = App.getInstance().userResult.getParent().getPhone();
        phone = StringUtil.isNotBlank(phone) ? phone : "...";
        String head = App.getInstance().userResult.getParent().getHeadThumb();
        user_name.setText(name);
        user_phone.setText(phone);
        user_icon.setVisibility(View.VISIBLE);
        Glide.with(App.getInstance()).load(R.mipmap.head_user_woman).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
    }

    public void onEvent(SettingEventType event) {
        settingAchor = event.mView;
        toggleShowSetting(event.mView);
    }

    public void onEvent(UserEventType event) {
    }

    @OnClick(R.id.exit_bt)
    void exitAccount() {
        SharedPreferencesHelper.setString(getActivity(), Constant.LoginUser.SHARED_PREFERENCES_PHONE, "");
        SharedPreferencesHelper.setString(getActivity(), Constant.LoginUser.SHARED_PREFERENCES_PASSWORD, "");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TL);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();
    }

    /**
     * 设置界面
     */
    public void toggleShowSetting(View v) {
        int x = layoutSetting.getLeft() + layoutSetting.getRight();
        int y = 0;
        float radiusOf = 1f * v.getWidth() / 2f;
        float radiusFromToRoot = (float) Math.hypot(
                Math.max(x, layoutSetting.getWidth() - x),
                Math.max(y, layoutSetting.getHeight() - y));

        if (!isSettingShow) {
            isSettingShow = true;
            showMenu(x, y, radiusOf, radiusFromToRoot);
        } else {
            isSettingShow = false;
            hideMenu(x, y, radiusFromToRoot, radiusOf);
        }
    }

    private void sendActionBarAnim() {
        if (!isOpenUser) {
            isOpenUser = true;
            activityHandler.sendEmptyMessageDelayed(ANIMATION, 1000);
        }
    }

    /**
     * actionbar user info animation
     */
    private void userAnimation(boolean is) {
        ValueAnimator animation = ValueAnimator.ofFloat(is ? 0 : EDITEXT_OFFER, is ? EDITEXT_OFFER : 0);
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
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_user.getLayoutParams();
                lp.height = (int) value;
                layout_user.setLayoutParams(lp);
            }

        });
        animation.start();
    }

    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        layoutSetting.setVisibility(View.VISIBLE);
        Animator revealAnim = createCircularReveal(layoutSetting, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.start();
    }

    private void hideMenu(int cx, int cy, float startRadius, float endRadius) {
        Animator revealAnim = createCircularReveal(layoutSetting, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutSetting.setVisibility(View.INVISIBLE);
            }
        });
        revealAnim.start();
    }

    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,
                                          float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private static final int ANIMATION = 0x03;//user layout
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj);
                    break;
                case ANIMATION:
                    userAnimation(true);
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
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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

    @Override
    public boolean onBackPressed() {
        if (isSettingShow) {
            toggleShowSetting(settingAchor);
            return true;
        }
        return false;
    }

}

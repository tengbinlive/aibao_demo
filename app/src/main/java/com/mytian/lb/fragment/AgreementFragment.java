package com.mytian.lb.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshGridView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.AgreementAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.user.UpdateActionResult;
import com.mytian.lb.bean.user.UserAction;
import com.mytian.lb.event.AgreementStateEventType;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.manager.AgreementManager;
import com.mytian.lb.manager.UserManager;
import com.mytian.lb.mview.ClipRevealFrame;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AgreementFragment extends AbsFragment {

    @Bind(R.id.gridview)
    PullToRefreshGridView gridview;
    @Bind(R.id.ll_listEmpty)
    View llListEmpty;

    @Bind(R.id.layout_agreement)
    ClipRevealFrame layoutClip;

    public static boolean isSettingShow;
    @Bind(R.id.circular_progressbar)
    HoloCircularProgressBar circularProgressbar;
    @Bind(R.id.agreement_title)
    TextView agreementTitle;
    @Bind(R.id.agreement_time)
    TextView agreementTime;

    @Bind(R.id.head)
    RoundedImageView headRIV;
    @Bind(R.id.des)
    TextView desTv;
    @Bind(R.id.agreement_cancle)
    Button agreementCancle;

    private View tempClip;

    private AgreementManager manager = new AgreementManager();
    private ArrayList<UserAction> arrayList;
    private UserAction cureentAction;
    private FollowUser cureentParent;
    private final Timer timer = new Timer();

    private float PROGRESS_MAX = 1.0f;

    private long DKEY_START_TIME = 0;

    private static int MINUTE_TIME = 5;

    private static long DKEY_TIME = MINUTE_TIME * 60 * 1000;

    private final static String STR_GAP= "  ";

    private final static String STR_INIT_TIME= "15  00";

    private static int sendCount = 0; //避免用户快速点击

    private GridView mActualGridView;

    private void initGridView() {

        gridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
                arrayList = null;
                updateAgreement();
            }
        });

        mActualGridView = gridview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualGridView);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (FollowUser.OFFLINE.equals(cureentParent.getIs_online())) {
                    CommonUtil.showToast(R.string.state_offline);
                    return;
                }
                if (!isSettingShow && sendCount <= 0) {
                    sendCount++;
                    AnimationHelper.getInstance().viewAnimationScal(view);
                    tempClip = view;
                    cureentAction = arrayList.get(position);
                    manager.sendAgreement(getActivity(), cureentParent.getUid(), DKEY_TIME, cureentAction.getCfg_id(), activityHandler, AGREEMENT);
                }
            }
        });
    }

    private void initGridViewData(ArrayList<UserAction> _arrayList) {

        arrayList = _arrayList;

        AgreementAdapter mAdapter = new AgreementAdapter(getActivity(), arrayList);

        mAdapter.setIsOFFLINE(FollowUser.OFFLINE.equals(cureentParent.getIs_online()));

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualGridView);

        mActualGridView.setAdapter(animationAdapter);
    }

    @OnClick(R.id.agreement_cancle)
    public void cancleAgreement() {
        manager.cancleAgreement(getActivity(), cureentParent.getUid(), cureentAction.getCfg_id(), activityHandler, AGREEMENT_CANCLE);
    }

    /**
     * 开始计时
     */
    private void startCountTime() {
        resetProgressBar();
        if (System.currentTimeMillis() - DKEY_START_TIME > DKEY_TIME) {
            DKEY_START_TIME = System.currentTimeMillis();
        }
    }

    private void startThread() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHander.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    Handler myHander = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                long TimeUsed = System.currentTimeMillis() - DKEY_START_TIME;
                setTimeText(TimeUsed);
            }
        }
    };

    /**
     * 更新时间&进度
     *
     * @param TimeUsed
     */
    private void setTimeText(long TimeUsed) {
        StringBuffer buffer = new StringBuffer();
        int TimeMinute = ((MINUTE_TIME * 60) - (int) TimeUsed / 1000) / 60;
        int TimeSeconds = ((MINUTE_TIME * 60) - (int) TimeUsed / 1000) % 60;
        if (TimeUsed < DKEY_TIME) {
            buffer.setLength(0);
            buffer.append(TimeMinute).append(STR_GAP);
            if (TimeSeconds == 0) {
                buffer.append("0");
            }
            buffer.append(TimeSeconds);
            agreementTime.setText(buffer.toString());

            float currentProgress = -(TimeUsed * PROGRESS_MAX) / DKEY_TIME;
            if (currentProgress < -PROGRESS_MAX) {
                currentProgress = -PROGRESS_MAX;
            } else if (currentProgress > 0) {
                currentProgress = 0;
            }
            circularProgressbar.setProgress(currentProgress);
        }
    }

    private void endTimeAnimation() {
        DKEY_START_TIME = 0;
    }

    /**
     * 重置计算进度
     */
    private void resetProgressBar() {
        circularProgressbar.setProgress(0f);
        circularProgressbar.setMarkerProgress(0f);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != timer) {
            timer.cancel();
        }
        myHander.removeMessages(0);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_agreement;
    }

    @Override
    public void EInit() {
        cureentParent = (FollowUser) getArguments().getSerializable(UserDetailActivity.USER);
        isSettingShow = false;
        initGridView();
        startThread();
        updateAgreement();
    }

    /**
     * 开始计时动画
     *
     * @param parent
     * @param time
     */
    private void startTimeAnimation(Parent parent, String time) {
        setAgreementObject(parent);
        agreementTime.setText(time);
        agreementTitle.setText(cureentAction.getDes());
    }

    /**
     * 设置约定对象
     *
     * @param parent
     */
    private void setAgreementObject(Parent parent) {
        String babyUid = App.getInstance().getUserResult().getParent().getUid();
        if (babyUid.equals(parent.getUid())) {
            desTv.setVisibility(View.GONE);
            headRIV.setVisibility(View.GONE);
        } else {
            desTv.setVisibility(View.GONE);
            headRIV.setVisibility(View.GONE);
        }
        if (null != parent) {
            String remark = parent.getAlias();
            String name = parent.getAlias();
            String des = StringUtil.isBlank(remark) ? name : remark;
            String head = parent.getHeadThumb();
            desTv.setVisibility(View.VISIBLE);
            headRIV.setVisibility(View.VISIBLE);
            desTv.setText(String.format(mContext.getString(R.string.and_agreement), des));
            Glide.with(mContext).load(head)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.default_head)
                    .centerCrop().into(headRIV);
        }
        setCancleState(cureentParent.getIs_online());
    }

    /**
     * 设置取消按钮状态
     *
     * @param isOline
     */
    private void setCancleState(String isOline) {
        if (!FollowUser.OFFLINE.equals(isOline)) {
            agreementCancle.setBackgroundResource(R.drawable.button_bg_theme);
            agreementCancle.setEnabled(true);
        } else {
            agreementCancle.setBackgroundResource(R.drawable.button_bg_gray);
            agreementCancle.setEnabled(false);
        }
    }

    /**
     * 线上状态更新
     *
     * @param event
     */
    public void onEvent(PushStateEventType event) {
        String babyUid = event.babyUid;
        if (babyUid.equals(cureentParent.getUid())) {
            setCancleState(cureentParent.getIs_online());
        }
    }

    /**
     * 获取约定
     */
    private void updateAgreement() {
        if (cureentParent == null) {
            loadEndState();
            return;
        }
        UserManager manager = new UserManager();
        manager.updateAgreement(getActivity(), cureentParent.getUid(), activityHandler, LOAD_AGREEMENT);
    }

    /**
     * 切换约定界面
     */
    public void toggleShowSetting(View v) {
        if (v == null) {
            return;
        }
        int x = layoutClip.getLeft() + layoutClip.getRight();
        int y = 0;
        float radiusOf = 0.5f * v.getWidth() / 2f;
        float radiusFromToRoot = (float) Math.hypot(
                Math.max(x, layoutClip.getWidth() - x),
                Math.max(y, layoutClip.getHeight() - y));
        if (!isSettingShow) {
            isSettingShow = true;
            showMenu(x, y, radiusOf, radiusFromToRoot);
        } else {
            isSettingShow = false;
            hideMenu(x, y, radiusFromToRoot, radiusOf);
        }
    }

    /**
     * 显示约定界面
     *
     * @param cx
     * @param cy
     * @param startRadius
     * @param endRadius
     */
    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        layoutClip.setVisibility(View.VISIBLE);
        Animator revealAnim = createCircularReveal(layoutClip, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(1000);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startCountTime();
            }
        });
        revealAnim.start();
    }

    /**
     * 隐藏约定界面
     *
     * @param cx
     * @param cy
     * @param startRadius
     * @param endRadius
     */
    private void hideMenu(int cx, int cy, float startRadius, float endRadius) {
        Animator revealAnim = createCircularReveal(layoutClip, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(500);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutClip.setVisibility(View.INVISIBLE);
                endTimeAnimation();
            }
        });
        revealAnim.start();
    }

    /**
     * view animation
     *
     * @param view
     * @param x
     * @param y
     * @param startRadius
     * @param endRadius
     * @return
     */
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

    private static final int AGREEMENT = 0x01;//约定
    private static final int AGREEMENT_CANCLE = 0x02;//取消约定
    private static final int LOAD_AGREEMENT = 0x03;//获取
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case LOAD_AGREEMENT:
                    loadData((CommonResponse) msg.obj);
                    break;
                case AGREEMENT:
                    loadAgreement((CommonResponse) msg.obj);
                    break;
                case AGREEMENT_CANCLE:
                    loadAgreementCancle((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取约定项目
     *
     * @param resposne
     */
    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            UpdateActionResult result = (UpdateActionResult) resposne.getData();
            initGridViewData(result.getList());
        }
        loadEndState();
    }

    /**
     * 开始约定
     *
     * @param parent
     * @param time
     */
    private void startAgreement(Parent parent, String time) {
        if (!isSettingShow) {
            toggleShowSetting(llListEmpty);
            startTimeAnimation(parent, time);
        }
    }

    /**
     * 约定
     *
     * @param resposne
     */
    private void loadAgreement(CommonResponse resposne) {
        dialogDismiss();
        if (resposne.isSuccess()) {
            EventBus.getDefault().post(new AgreementStateEventType(cureentParent.getUid(), AgreementStateEventType.AGREEMENT_ING));
            Parent parent = App.getInstance().getUserResult().getParent();
            startAgreement(parent, STR_INIT_TIME);
        } else {
            sendCount = 0;
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    /**
     * 取消约定
     *
     * @param resposne
     */
    private void loadAgreementCancle(CommonResponse resposne) {
        dialogDismiss();
        sendCount = 0;
        if (resposne.isSuccess()) {
            EventBus.getDefault().post(new AgreementStateEventType(cureentParent.getUid(), AgreementStateEventType.AGREEMENT_END));
            if (isSettingShow) {
                toggleShowSetting(tempClip);
            }
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    /**
     * 数据加载结束
     */
    private void loadEndState() {
        gridview.onRefreshComplete();
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

}

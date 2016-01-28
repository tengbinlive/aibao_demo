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

import com.alibaba.fastjson.JSON;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.dao.UserAction;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.AgreementAdapter;
import com.mytian.lb.bean.action.AgreementResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.AgreementStateEventType;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.UserActionDOManager;
import com.mytian.lb.manager.UserActionManager;
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
    GridView gridview;

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

    private UserActionManager manager = new UserActionManager();
    private ArrayList<UserAction> arrayList;
    private UserAction cureentAction;
    private FollowUser cureentParent;
    private AgreementAdapter mAdapter;
    private final Timer timer = new Timer();

    private float PROGRESS_MAX = 1.0f;

    private static long DKEY_TIME = 15 * 60 * 1000;

    private final static String STR_GAP = "  ";

    private final static String SPLIT_KEY = "%";

    private final static String SHAREDPREFERENCES_TIME = "START_TIME";

    private static int sendCount = 0; //避免用户快速点击

    private long actionEndTime;

    public String appoint_time;

    private void initGridView() {

        initGridViewData(UserActionDOManager.getInstance().getArrayListAgreement());

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnimationHelper.getInstance().viewAnimationScal(view);
                int resid = isSendEffect();
                if (resid != -1) {
                    CommonUtil.showToast(resid);
                    return;
                }
                if (!isSettingShow && sendCount <= 0) {
                    sendCount++;
                    tempClip = view;
                    cureentAction = arrayList.get(position);
                    manager.sendAgreement(getActivity(), cureentParent.getUid(), DKEY_TIME, cureentAction.getAppointId(), activityHandler, AGREEMENT);
                }
            }
        });
    }

    /**
     * -1 表示 可以发送约定
     * 其他 返回 提示用字符串 resid
     *
     * @return
     */
    private int isSendEffect() {
        if (FollowUser.OFFLINE.equals(cureentParent.getIs_online()))
            return R.string.state_offline;
        if (AgreementStateEventType.AGREEMENT_ING.equals(cureentParent.getAppointing()))
            return R.string.state_agreementing;
        return -1;
    }

    private void initGridViewData(ArrayList<UserAction> _arrayList) {

        arrayList = _arrayList;

        mAdapter = new AgreementAdapter(getActivity(), _arrayList);

        mAdapter.setIsOFFLINE(FollowUser.OFFLINE.equals(cureentParent.getIs_online()));

        mAdapter.setIsAGREEMENTING(AgreementStateEventType.AGREEMENT_ING.equals(cureentParent.getAppointing()));

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(gridview);

        gridview.setAdapter(animationAdapter);
    }

    @OnClick(R.id.agreement_cancle)
    public void cancleAgreement() {
        if (cureentAction == null) {
            return;
        }
        manager.cancleAgreement(getActivity(), cureentParent.getUid(), cureentAction.getAppointId(), activityHandler, AGREEMENT_CANCLE);
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
                long TimeUsed = actionEndTime - System.currentTimeMillis();
                if (TimeUsed >= 0) {
                    setTimeText(TimeUsed);
                } else {
                    endTimeAnimation();
                }
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
        int TimeMinute = (int) TimeUsed / 1000 / 60;
        int TimeSeconds = (int) TimeUsed / 1000 % 60;
        buffer.setLength(0);
        if (TimeMinute < 10) {
            buffer.append("0");
        }
        buffer.append(TimeMinute).append(STR_GAP);
        if (TimeSeconds < 10) {
            buffer.append("0");
        }
        buffer.append(TimeSeconds);
        agreementTime.setText(buffer.toString());

        //更新进度
        float currentProgress = PROGRESS_MAX - (TimeUsed * PROGRESS_MAX) / DKEY_TIME;
        circularProgressbar.setProgress(-currentProgress);
    }

    private void endTimeAnimation() {
        if (isSettingShow) {
            String uid = cureentParent.getUid();
            String sharedKey = SHAREDPREFERENCES_TIME + uid;
            SharedPreferencesHelper.setString(mContext, sharedKey, "");
            actionEndTime = 0;
            timer.cancel();
            resetProgressBar();
            toggleShowSetting(tempClip);
        }
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
        if (null != myHander) {
            myHander.removeMessages(0);
        }
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
        String uid = cureentParent.getUid();
        String sharedKey = SHAREDPREFERENCES_TIME + uid;
        String content = SharedPreferencesHelper.getString(mContext, sharedKey, "");
        if (StringUtil.isNotBlank(content)) {
            String[] strings = StringUtil.split(content, SPLIT_KEY);
            actionEndTime = Long.valueOf(strings[0]);
            cureentAction = JSON.parseObject(strings[1], UserAction.class);
            long currentTime = System.currentTimeMillis();
            if (currentTime < actionEndTime) {
                Parent parent = App.getInstance().getUserResult().getParent();
                startAgreement(parent, actionEndTime);
            }
        }
    }

    /**
     * 开始计时动画
     *
     * @param parent
     */
    private void startTimeAnimation(Parent parent) {
        setAgreementObject(parent);
        agreementTitle.setText(cureentAction.getTitle());
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
            setCancleState(cureentParent.getIs_online());
        } else {
            setCancleState(FollowUser.OFFLINE);
            desTv.setVisibility(View.VISIBLE);
            headRIV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置取消按钮状态
     *
     * @param isOline
     */
    private void setCancleState(String isOline) {
        if (FollowUser.ONLINE.equals(isOline)) {
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
            setCancleState(event.is_online);
            cureentParent.setIs_online(event.is_online);
            mAdapter.setIsOFFLINE(FollowUser.OFFLINE.equals(event.is_online));
        }
    }

    /**
     * 约定状态更新
     *
     * @param event
     */
    public void onEventMainThread(AgreementStateEventType event) {
        String babyUid = event.babyUid;
        String time = event.appoint_time;
        if (cureentParent.getUid().equals(babyUid)) {
            cureentParent.setAppointing(event.appointing);
            cureentParent.setAppointer(event.appointer);
            boolean isAgreement = AgreementStateEventType.AGREEMENT_ING.equals(event.appointing);
            mAdapter.setIsAGREEMENTING(isAgreement);
            if (StringUtil.isNotBlank(appoint_time) && time.equals(appoint_time)) {
                if (isAgreement) {
                    Parent parent = App.getInstance().getUserResult().getParent();
                    actionEndTime = System.currentTimeMillis() + DKEY_TIME;
                    startAgreementAni(parent, actionEndTime);
                } else {
                    endTimeAnimation();
                }
            }
        }
    }

    /**
     * 切换约定界面
     */
    public void toggleShowSetting(View v) {
        int viewWidth = 0;
        if (v != null) {
            viewWidth = v.getWidth();
        }
        int x = layoutClip.getLeft() + layoutClip.getRight();
        int y = 0;
        float radiusOf = 0.5f * viewWidth / 2f;
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
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
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
     * 开始约定 带动画
     *
     * @param parent
     */
    private void startAgreementAni(Parent parent, long time) {
        if (!isSettingShow) {
            resetProgressBar();
            toggleShowSetting(tempClip);
            saveTime(time);
            startThread();
            startTimeAnimation(parent);
        }
    }

    /**
     * 开始约定 无动画
     *
     * @param parent
     */
    private void startAgreement(Parent parent, long time) {
        if (!isSettingShow) {
            isSettingShow = true;
            resetProgressBar();
            layoutClip.setVisibility(View.VISIBLE);
            saveTime(time);
            startThread();
            startTimeAnimation(parent);
        }
    }


    /**
     * 开始时间存储
     */
    private void saveTime(long time) {
        StringBuffer contentBf = new StringBuffer();
        String action = JSON.toJSONString(cureentAction);
        contentBf.append(time).append(SPLIT_KEY).append(action);
        String uid = cureentParent.getUid();
        String sharedKey = SHAREDPREFERENCES_TIME + uid;
        SharedPreferencesHelper.setString(mContext, sharedKey, contentBf.toString());
    }

    /**
     * 约定
     *
     * @param resposne
     */
    private void loadAgreement(CommonResponse resposne) {
        dialogDismiss();
        if (resposne.isSuccess()) {
            AgreementResult result = (AgreementResult) resposne.getData();
            Parent parent = App.getInstance().getUserResult().getParent();
            appoint_time = result.getAppoint_time();
            AgreementStateEventType eventType = new AgreementStateEventType(cureentParent.getUid(), AgreementStateEventType.AGREEMENT_ING, parent.getAlias(), appoint_time);
            EventBus.getDefault().postSticky(eventType);
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
            AgreementResult result = (AgreementResult) resposne.getData();
            appoint_time = result.getAppoint_time();
            AgreementStateEventType eventType = new AgreementStateEventType(cureentParent.getUid(), AgreementStateEventType.AGREEMENT_END, null, appoint_time);
            EventBus.getDefault().postSticky(eventType);
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

}

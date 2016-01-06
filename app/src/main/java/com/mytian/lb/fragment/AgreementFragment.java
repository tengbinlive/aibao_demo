package com.mytian.lb.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshGridView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.AgreementAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.user.UpdateActionResult;
import com.mytian.lb.bean.user.UserAction;
import com.mytian.lb.event.TimeEventType;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.manager.AgreementManager;
import com.mytian.lb.manager.UserManager;
import com.mytian.lb.mview.ClipRevealFrame;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

public class AgreementFragment extends AbsFragment {

    @Bind(R.id.gridview)
    PullToRefreshGridView gridview;
    @Bind(R.id.ll_listEmpty)
    View llListEmpty;

    @Bind(R.id.layout_agreement)
    ClipRevealFrame layoutClip;

    public static boolean isSettingShow;
    @Bind(R.id.agreement_animation)
    ImageView agreementAnimation;
    @Bind(R.id.agreement_title)
    TextView agreementTitle;
    @Bind(R.id.agreement_time)
    TextView agreementTime;
    private View tempClip;

    private AgreementManager manager = new AgreementManager();
    private ArrayList<UserAction> arrayList;
    private UserAction cureentAction;
    private FollowUser cureentParent;
    private final Timer timer = new Timer();

    private long DKEY_START_TIME = 0;

    private AnimationDrawable animationDrawable;

    private int animaitonNum;

    private boolean isTimeUsed; //进度条是否开始同步时间

    private static long DKEY_TIME = 15 * 60 * 1000;

    private static int sendCount = 0;

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

    private void startTimeAnimation() {
        agreementAnimation.setImageDrawable(animationDrawable);
        animationDrawable.start();
        isTimeUsed = false;
        agreementTitle.setText(cureentAction.getDes());
        if (System.currentTimeMillis() - DKEY_START_TIME > DKEY_TIME) {
            DKEY_START_TIME = System.currentTimeMillis();
        }
        agreementTime.setText("15分00秒");
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

    private void setTimeText(long TimeUsed) {
        StringBuffer buffer = new StringBuffer();
        int TimeMinute = ((15 * 60) - (int) TimeUsed / 1000) / 60;
        int TimeSeconds = ((15 * 60) - (int) TimeUsed / 1000) % 60;
        if (TimeUsed < DKEY_TIME) {
            buffer.setLength(0);
            buffer.append(TimeMinute).append("分");
            if (TimeSeconds == 0) {
                buffer.append("0");
            }
            buffer.append(TimeSeconds).append("秒");
            agreementTime.setText(buffer.toString());

            int int_time = (int) TimeUsed / 1000;
            int int_dkey_time = (int) DKEY_TIME / 1000;
            int level = animaitonNum - (int_time * animaitonNum) / int_dkey_time;
            if (level >= animaitonNum) {
                level = animaitonNum;
            } else if (level <= 0) {
                level = 0;
            }
            if (level != animaitonNum && !isTimeUsed) {
                isTimeUsed = true;
                agreementAnimation.setImageResource(R.drawable.level_time);
            }
            if (isTimeUsed) {
                agreementAnimation.setImageLevel(level);
            }
        }
    }

    private void endTimeAnimation() {
        animationDrawable.stop();
        DKEY_START_TIME = 0;
        agreementTime.setText("15分00秒");
    }

    @Override
    public boolean onBackPressed() {
        return false;
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
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_time);
        animaitonNum = animationDrawable.getNumberOfFrames();
        updateAgreement();
    }

    /**
     * 获取约定
     */
    private void updateAgreement() {
        if (cureentParent == null) {
            setEndState();
            return;
        }
        UserManager manager = new UserManager();
        manager.updateAgreement(getActivity(), cureentParent.getUid(), activityHandler, LOAD_AGREEMENT);
    }

    public void onEvent(TimeEventType event) {
        toggleShowSetting(tempClip);
    }

    /**
     * 设置界面
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

    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        layoutClip.setVisibility(View.VISIBLE);
        Animator revealAnim = createCircularReveal(layoutClip, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(1000);
        revealAnim.start();
    }

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

    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            UpdateActionResult result = (UpdateActionResult) resposne.getData();
            initGridViewData(result.getList());
        }
        setEndState();
    }


    private void loadAgreement(CommonResponse resposne) {
        dialogDismiss();
        if (resposne.isSuccess()) {
            if (!isSettingShow) {
                toggleShowSetting(tempClip);
            }
            startTimeAnimation();
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void loadAgreementCancle(CommonResponse resposne) {
        dialogDismiss();
        sendCount = 0;
        if (resposne.isSuccess()) {
            if (isSettingShow) {
                toggleShowSetting(tempClip);
            }
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void setEndState() {
        gridview.onRefreshComplete();
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

}

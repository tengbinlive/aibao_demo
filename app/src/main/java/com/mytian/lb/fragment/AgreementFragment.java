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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.adapter.AgreementAdapter;
import com.mytian.lb.bean.AgreementBean;
import com.mytian.lb.bean.DemoUserInfo;
import com.mytian.lb.demodata.DemoManger;
import com.mytian.lb.demodata.DemoUserType;
import com.mytian.lb.event.TimeEventType;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.mview.ClipRevealFrame;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

public class AgreementFragment extends AbsFragment {

    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    @Bind(R.id.user_phone)
    TextView user_phone;
    @Bind(R.id.user_icon)
    RoundedImageView user_icon;
    @Bind(R.id.user_name)
    TextView user_name;

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

    private AgreementAdapter mAdapter;
    private AgreementBean cureentAgreement;
    public static final long DKEY_TIME = 15 * 60 * 1000;// 预约倒计时 15分60 秒
    private final Timer timer = new Timer();

    private long DKEY_START_TIME = 0;

    private ArrayList<AgreementBean> arrayList = new ArrayList<>();

    private AnimationDrawable animationDrawable;

    private int animaitonNum;

    private boolean isTimeUsed; //进度条是否开始同步时间

    private void initGridView() {

        initData();

        mAdapter = new AgreementAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(gridview);

        gridview.setAdapter(animationAdapter);

        gridview.setEmptyView(llListEmpty);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isSettingShow) {
                    AnimationHelper.getInstance().viewAnimationScal(view);
                    tempClip = view;
                    cureentAgreement = arrayList.get(position);
                    toggleShowSetting(tempClip);
                }
            }
        });
    }

    @OnClick(R.id.agreement_cancle)
    public void cancleAgreement() {
        toggleShowSetting(tempClip);
    }

    private String[] argeementTitle = new String[]{"写作业", "看书", "做家务", "吃饭", "出去玩", "睡觉"};

    private void initData() {
        int max = 6;
        for (int i = 0; i < max; i++) {
            String iconStr = "icon_love_hb";
            int imgId = getResources().getIdentifier(iconStr + (i % 6 + 1), "mipmap", App.getInstance().getPackageName());
            AgreementBean bean = new AgreementBean();
            bean.setTitle(argeementTitle[i % max]);
            bean.setIcon(imgId);
            arrayList.add(bean);
        }
    }

    private void setUserInfo(DemoUserInfo demoUserInfo) {
        String name = demoUserInfo.getParent().getName();
        int head = demoUserInfo.getHeadid();
        String phone = demoUserInfo.getParent().getPhone();
        user_name.setText(name);
        user_phone.setText(phone);
        Glide.with(this).load(head)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_head).centerCrop().into(user_icon);
    }

    private void startTimeAnimation() {
        agreementAnimation.setImageDrawable(animationDrawable);
        animationDrawable.start();
        isTimeUsed = false;
        agreementTitle.setText(cureentAgreement.getTitle());
        if (System.currentTimeMillis() - DKEY_START_TIME > DKEY_TIME) {
            DKEY_START_TIME = System.currentTimeMillis();
        }
        agreementTime.setText("15分00秒");
    }

    private void StartThread() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHander.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    Handler myHander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                long TimeUsed = System.currentTimeMillis() - DKEY_START_TIME;
                setTimeText(TimeUsed);
            }
        }

        ;
    };

    StringBuffer buffer = new StringBuffer();

    private void setTimeText(long TimeUsed) {
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
        initGridView();
        StartThread();
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_time);
        animaitonNum = animationDrawable.getNumberOfFrames();
        Message message = new Message();
        message.what = INIT_USER_INFO;
        message.obj = "0";
        activityHandler.sendMessage(message);
    }

    public void onEvent(DemoUserType event) {
        Message message = new Message();
        message.what = INIT_USER_INFO;
        message.obj = event.index;
        activityHandler.sendMessage(message);
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
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startTimeAnimation();
            }

        });
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

    private static final int INIT_USER_INFO = 0x03;//设置用户信息
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_USER_INFO:
                    setUserInfo(DemoManger.getInstance().getDemoUserInfo(msg.obj.toString()));
                    break;
                default:
                    break;
            }
        }
    };

}

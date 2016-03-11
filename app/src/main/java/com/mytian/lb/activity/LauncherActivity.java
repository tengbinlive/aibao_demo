package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;

import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.debug.ChannelUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.helper.CookieThumperSample;
import com.mytian.lb.helper.SlideSample;
import com.mytian.lb.manager.AppManager;
import com.mytian.lb.push.PushHelper;

import butterknife.Bind;
import butterknife.OnClick;
import su.levenetc.android.textsurface.TextSurface;

/**
 * 启动界面
 */
public class LauncherActivity extends AbsActivity {

    private final static int TO_LOGIN = 0;
    private final static int TO_GUIDE = 1;

    private static int statue;

    @Bind(R.id.launcher_ly)
    RelativeLayout launcherLy;

    @Bind(R.id.text_surface)
    TextSurface textSurface;

    @Override
    public void EInit() {
        if (BuildConfig.CHANNEL_DEBUG) {
            String channel_id = ChannelUtil.getChannel(this);
            if (StringUtil.isNotBlank(channel_id)) {
                CommonUtil.showToast(channel_id);
            }
        }
        PushHelper.getInstance().initPush();
        super.EInit();
        setSwipeBackEnable(false);
        statue = App.isFirstLunch() ? TO_GUIDE : TO_LOGIN;
        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                showGuideText();
            }
        }, 2000);
//        long time = statue == TO_GUIDE ? 14000 : 13000;
//        activityHandler.sendEmptyMessageDelayed(statue, time);
    }

    private void showGuideText() {
        textSurface.reset();
        CookieThumperSample.play(textSurface, getAssets());
//        SlideSample.play(textSurface);
    }

    @Override
    public void onResume() {
        super.onResume();
        launcherLy.setEnabled(true);
    }

    @OnClick(R.id.launcher_ly)
    void OnClickActivity() {
        launcherLy.setEnabled(false);
        activityHandler.removeMessages(statue);
        statue = App.isFirstLunch() ? TO_GUIDE : TO_LOGIN;
        activityHandler.sendEmptyMessage(statue);
    }

    @Override
    public void onBackPressed() {
        activityHandler.removeMessages(statue);
        App.getInstance().exit();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }

    private void toLogining() {
        Parent parent = App.getInstance().getUserResult().getParent();
        if (null != parent) {
            toMain();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    private void toGuide() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void toMain() {
        if (AppManager.isOUT) {
            return;
        }
        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            toActivity(msg);
        }
    };

    private void toActivity(Message msg) {
        int statue = msg.what;
        switch (statue) {
            case TO_LOGIN:
                toLogining();
                break;
            case TO_GUIDE:
                toGuide();
                break;
            default:
                break;
        }
    }

}

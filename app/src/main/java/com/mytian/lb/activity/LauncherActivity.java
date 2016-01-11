package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.debug.ChannelUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.push.PushHelper;

import butterknife.Bind;
import butterknife.OnClick;

public class LauncherActivity extends AbsActivity {

    private final static int TO_LOGIN = 0;
    private final static int TO_GUIDE = 1;

    private static int statue;

    @Bind(R.id.launcher_ly)
    LinearLayout launcherLy;

    @Override
    public void EInit() {
        if (BuildConfig.CHANNEL_DEBUG) {
            String channel_id = ChannelUtil.getChannel(this);
            if (StringUtil.isNotBlank(channel_id)) {
                CommonUtil.showToast(channel_id);
            }
        }
        PushHelper.getInstance().initPush(getApplicationContext());
        super.EInit();
        setSwipeBackEnable(false);
        statue = App.isFirstLunch() ? TO_GUIDE : TO_LOGIN;
        long time = statue == TO_GUIDE ? 4000 : 3000;
        activityHandler.sendEmptyMessageDelayed(statue, time);
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

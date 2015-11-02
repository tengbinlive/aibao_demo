package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;

import butterknife.OnClick;

public class LauncherActivity extends AbsActivity {

    private final static int TO_LOGIN = 0;
    private final static int TO_GUIDE = 1;
    private boolean isTo = false;
    private static int statue;

    @Override
    public void EInit() {
        super.EInit();
        setSwipeBackEnable(false);
        if (!isTo) {
            statue = App.isFirstLunch() ? TO_GUIDE : TO_LOGIN;
            activityHandler.sendEmptyMessageDelayed(statue, 4000);
        }
    }

    @OnClick(R.id.launcher_ly)
    void OnClickActivity() {
        activityHandler.removeMessages(statue);
        toActivity(statue);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }

    private void toLogining() {
        isTo = true;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void toGuide() {
        isTo = true;
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            toActivity(msg.what);
        }
    };

    private void toActivity(int statue) {
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

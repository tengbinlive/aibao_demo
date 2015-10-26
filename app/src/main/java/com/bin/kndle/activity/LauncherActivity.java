package com.bin.kndle.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bin.kndle.AbsActivity;
import com.bin.kndle.App;
import com.bin.kndle.R;

public class LauncherActivity extends AbsActivity {

    private final static int TO_LOGIN = 0;
    private final static int TO_GUIDE = 1;
    private boolean isTo = false;

    @Override
    public void EInit() {
        super.EInit();
        setSwipeBackEnable(false);
        if (!isTo) {
            activityHandler.sendEmptyMessageDelayed(App.isFirstLunch() ? TO_GUIDE : TO_LOGIN, 1500);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }

    private void toLogining() {
        isTo = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade, R.anim.launcher_out);
    }

    private void toGuide() {
        isTo = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade, R.anim.launcher_out);
    }

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            dialogDismiss();
            switch (msg.what) {
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
    };

}

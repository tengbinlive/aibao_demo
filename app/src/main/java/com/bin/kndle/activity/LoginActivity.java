package com.bin.kndle.activity;


import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bin.kndle.AbsActivity;
import com.bin.kndle.R;
import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.Bind;

public class LoginActivity extends AbsActivity {

    @Bind(R.id.image)
    KenBurnsView image;

    @Override
    public void EInit() {
        super.EInit();
        setSwipeBackEnable(false);
        activityHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            dialogDismiss();
            switch (msg.what) {
                case 0:
                    image.restart();
                     break;
                default:
                    break;
            }
        }
    };

}

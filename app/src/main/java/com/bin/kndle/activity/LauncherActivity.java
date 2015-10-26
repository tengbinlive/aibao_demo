package com.bin.kndle.activity;


import com.bin.kndle.AbsActivity;
import com.bin.kndle.R;

public class LauncherActivity extends AbsActivity {

    @Override
    public void EInit() {
        super.EInit();
        setSwipeBackEnable(false);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }

}

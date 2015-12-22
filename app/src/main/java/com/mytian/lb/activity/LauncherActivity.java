package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.dao.Parent;
import com.dao.ParentDao;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;

import java.util.List;

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
        PushManager.getInstance().initialize(this.getApplicationContext());
        Tag[] tags = new Tag[1];
        Tag tag = new Tag();
        tag.setName("parent");
        tags[0] = tag;
        PushManager.getInstance().setTag(this,tags);
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
        ParentDao parentDao = App.getDaoSession().getParentDao();
        List<Parent> parents = parentDao.loadAll();
        int size = parents == null ? 0 : parents.size();
        if (size > 0) {
            Parent parent = parents.get(0);
            App.getInstance().userResult.setParent(parent);
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

package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.LoginManager;

import butterknife.OnClick;

public class LauncherActivity extends AbsActivity {

    private final static int TO_LOGIN = 0;
    private final static int TO_GUIDE = 1;
    private final static int LOGIN_ING = 2;
    private final static int TO_MAIN = 3;
    private boolean isTo = false;
    private static int statue;
    private LoginManager loginManager = new LoginManager();

    private String phone;
    private String password;

    @Override
    public void EInit() {
        super.EInit();
        setSwipeBackEnable(false);
        if (!isTo) {
            statue = App.isFirstLunch() ? TO_GUIDE : TO_LOGIN;
            long time = statue == TO_GUIDE ? 4000 : 3000;
            activityHandler.sendEmptyMessageDelayed(statue, time);
        }
    }

    @OnClick(R.id.launcher_ly)
    void OnClickActivity() {
        activityHandler.removeMessages(statue);
        if(App.getInstance().userResult==null) {
            activityHandler.sendEmptyMessage(TO_LOGIN);
        }else{
            activityHandler.sendEmptyMessage(TO_MAIN);
        }
    }

    void login(String phone, String password) {
        if (StringUtil.isNotBlank(phone) && StringUtil.isNotBlank(password)) {
            loginManager.login(this, phone, password, activityHandler, LOGIN_ING);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }

    private void toLogining() {
        isTo = true;
        phone = SharedPreferencesHelper.getString(this, Constant.LoginUser.SHARED_PREFERENCES_PHONE, "");
        password = SharedPreferencesHelper.getString(this, Constant.LoginUser.SHARED_PREFERENCES_PASSWORD, "");
        login(phone, password);
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
            case LOGIN_ING:
                loadLogin((CommonResponse) msg.obj);
                break;
            case TO_MAIN:
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void loadLogin(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            SharedPreferencesHelper.setString(this, Constant.LoginUser.SHARED_PREFERENCES_PHONE, phone);
            SharedPreferencesHelper.setString(this, Constant.LoginUser.SHARED_PREFERENCES_PASSWORD, password);
            App.getInstance().userResult = (UserResult) resposne.getData();
            App.getInstance().userResult.getParent().setPhone(phone);
            activityHandler.sendEmptyMessage(TO_MAIN);
        } else {
            SharedPreferencesHelper.setString(this, Constant.LoginUser.SHARED_PREFERENCES_PHONE, "");
            SharedPreferencesHelper.setString(this, Constant.LoginUser.SHARED_PREFERENCES_PASSWORD, "");
            activityHandler.sendEmptyMessage(TO_LOGIN);
        }
    }

}

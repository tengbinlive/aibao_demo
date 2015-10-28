package com.bin.kndle.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.bin.kndle.AbsActivity;
import com.bin.kndle.R;
import com.bin.kndle.helper.SMSContentObserver;
import com.bin.kndle.mview.EditTextWithDelete;
import com.core.CommonResponse;
import com.core.util.CommonUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.OnClick;

public class RegisterActivity extends AbsActivity {

    public static final long DKEY_TIME = 60 * 1000;// 获取动态验证码的时间间隔 60 秒

    private final Timer timer = new Timer();

    private long DKEY_START_TIME = 0;

    private SMSContentObserver smsContentObserver;

    private boolean isVer = false;

    /**
     * 填写验证码
     */
    public static final int LOAD_AUTHCODE_FILL = 3;

    private final static int AUTH_CODE = 0; //获取验证码

    private final static int REGISTER = 1; //用户注册

    private final static int LOGIN_DATA = 2; //登录

    @BindColor(R.color.white)
    int whiteColos;
    @BindString(R.string.get_verification)
    String login_verification_title;
    @BindString(R.string.reget_verification)
    String reget_verification;
    @Bind(R.id.register_et)
    EditTextWithDelete register_et;
    @Bind(R.id.verification_bt)
    Button verification_bt;

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            dialogDismiss();
            switch (msg.what) {
                case AUTH_CODE:
                    loadAuthCode((CommonResponse) msg.obj);
                    break;
                case REGISTER:
                    loadRegister((CommonResponse) msg.obj);
                    break;
                case LOGIN_DATA:
                    toMainActivity();
                    break;
                case LOAD_AUTHCODE_FILL:
                    String securityCode = msg.obj.toString();
                    register_et.setText(securityCode);
                    register_et.setSelection(securityCode.length());
                    break;
                default:
                    break;
            }
        }
    };

    private void loadAuthCode(CommonResponse resposne) {
        if (true) {
            CommonUtil.showToast("验证码已发送，请注意查收。");
        } else {
            CommonUtil.showToast(resposne.getErrorTip());
        }
    }

    private void loadRegister(CommonResponse resposne) {
        dialogDismiss();
        if (true) {
            Login(null, null);
        } else {
            CommonUtil.showToast(resposne.getErrorTip());
        }
    }

    /**
     * 登录
     */
    private void Login(String phone, String password) {
        dialogShow(R.string.logining);
        activityHandler.sendEmptyMessageDelayed(LOGIN_DATA, 4000);
    }

    @OnClick(R.id.register_bt)
    void register() {
        dialogShow(R.string.register_ing);
        activityHandler.sendEmptyMessageDelayed(REGISTER, 4000);
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.verification_bt)
    void getAuthCode() {
        if (!isVer && System.currentTimeMillis() - DKEY_START_TIME > DKEY_TIME) {
            isVer = true;
            activityHandler.sendEmptyMessageDelayed(AUTH_CODE, 4000);
            DKEY_START_TIME = System.currentTimeMillis();
        }
    }

    private void initSMSContentObserver() {
        smsContentObserver = new SMSContentObserver(this, activityHandler);
        //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContentObserver);
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
                setDKeyBtnText(TimeUsed);
            }
        }

        ;
    };

    private void setDKeyBtnText(long TimeUsed) {
        int TimeSeconds = (59) - (int) TimeUsed / 1000;
        if (TimeUsed < DKEY_TIME) {
            verification_bt.setText(reget_verification + TimeSeconds + "'");
            verification_bt.setTextColor(whiteColos);
        } else {
            isVer = false;
            verification_bt.setText(login_verification_title);
            verification_bt.setTextColor(whiteColos);
        }
    }

    @Override
    public void EInit() {
        super.EInit();
        StartThread();
        initSMSContentObserver();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void initActionBar() {
        setToolbarIntermediateStrID(R.string.register);
    }

    @Override
    public void finish() {
        super.finish();
        if (smsContentObserver != null) {
            this.getContentResolver().unregisterContentObserver(smsContentObserver);
            smsContentObserver = null;
        }
    }
}

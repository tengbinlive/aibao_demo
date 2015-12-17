package com.mytian.lb.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dao.Parent;
import com.dao.ParentDao;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.helper.SMSContentObserver;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.LoginManager;
import com.mytian.lb.mview.EditTextWithDelete;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.rey.material.widget.CheckBox;

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

    private LoginManager loginManager = new LoginManager();

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
    @Bind(R.id.password_et)
    EditTextWithDelete password_et;
    @Bind(R.id.verification_et)
    EditTextWithDelete verification_et;
    @Bind(R.id.phone_et)
    EditTextWithDelete phone_et;
    @Bind(R.id.verification_bt)
    Button verification_bt;
    @Bind(R.id.agree_cb)
    CheckBox agree_cb;

    private String phone;
    private String password;

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
                    loadLogin((CommonResponse) msg.obj);
                    break;
                case LOAD_AUTHCODE_FILL:
                    String securityCode = msg.obj.toString();
                    verification_et.setText(securityCode);
                    verification_et.setSelection(securityCode.length());
                    break;
                default:
                    break;
            }
        }
    };

    private void loadAuthCode(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            CommonUtil.showToast("验证码已发送，请注意查收。");
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void loadRegister(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            phone = phone_et.getText().toString();
            password = password_et.getText().toString();
            Login(phone, password);
        } else {
            CommonUtil.showToast(resposne.getErrorTip());
        }
    }

    private void loadLogin(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            App.getInstance().userResult = (UserResult) resposne.getData();
            ParentDao dao = App.getDaoSession().getParentDao();
            dao.deleteAll();
            dao.insertInTx(App.getInstance().userResult.getParent());
            toMainActivity();
        } else {
            CommonUtil.showToast(resposne.getMsg());
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    /**
     * 登录
     */
    private void Login(String phone, String password) {
        dialogShow(R.string.logining);
        loginManager.login(this, phone, password, activityHandler, LOGIN_DATA);
    }

    @OnClick(R.id.register_bt)
    void register() {
        phone = phone_et.getText().toString();
        if (StringUtil.isBlank(phone) || !StringUtil.checkMobile(phone)) {
            AnimationHelper.getInstance().viewAnimationQuiver(phone_et);
            return;
        }

        String verificationCode = verification_et.getText().toString();
        if (StringUtil.isBlank(verificationCode)) {
            AnimationHelper.getInstance().viewAnimationQuiver(verification_et);
            return;
        }

        password = password_et.getText().toString();
        if (StringUtil.isBlank(password)) {
            AnimationHelper.getInstance().viewAnimationQuiver(password_et);
            return;
        }

        if (!agree_cb.isChecked()) {
            AnimationHelper.getInstance().viewAnimationQuiver(agree_cb);
            return;
        }
        dialogShow(R.string.register_ing);
        loginManager.register(this,phone,verificationCode,password,activityHandler,REGISTER);
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.verification_bt)
    void getAuthCode() {
        String phone = phone_et.getText().toString();
        if (StringUtil.isBlank(phone) || !StringUtil.checkMobile(phone)) {
            AnimationHelper.getInstance().viewAnimationQuiver(phone_et);
            return;
        }
        if (!isVer && System.currentTimeMillis() - DKEY_START_TIME > DKEY_TIME) {
            isVer = true;
            loginManager.authCode(this, phone, activityHandler, AUTH_CODE);
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
        agree_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    dialogAgreeValue("《用户使用协议》");
                }
            }
        });
        phone = getIntent().getStringExtra("phone");
        if(StringUtil.isNotBlank(phone)) {
            phone_et.setText(phone);
            phone_et.setSelection(phone.length());
        }
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

    public void dialogAgreeValue(String value) {
        if (StringUtil.isBlank(value)) {
            return;
        }
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.dialog_agree, null);
        TextView valueTv = (TextView) convertView.findViewById(R.id.value);
        valueTv.setText(value);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(true) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.show();
    }
}

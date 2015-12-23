package com.mytian.lb.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.core.openapi.OpenApi;
import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.manager.AppManager;

import butterknife.Bind;
import butterknife.OnClick;

public class SysSettingActivity extends AbsActivity {

    @Bind(R.id.api_state)
    Button apiState;
    @Bind(R.id.version_tv)
    TextView versionTv;

    @Override
    public void EInit() {
        super.EInit();
        versionTv.setText("version ：" + App.getInstance().getAppVersionName());
        initApiState();
    }

    private void initApiState() {
        if (Constant.DEBUG) {
            apiState.setVisibility(View.VISIBLE);
        } else {
            apiState.setVisibility(View.GONE);
        }
        setApiState(OpenApi.isDEBUG());
    }

    private void setApiState(boolean state) {
        if (state) {
            apiState.setText("API：test ,ip：10.0.1.15");
        } else {
            apiState.setText("API：official ,ip：114.215.108.49");
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_sys_setting;
    }

    @Override
    public void initActionBar() {
        setToolbarIntermediateStrID(R.string.setting_sys);
    }

    @OnClick(R.id.exit_bt)
    void exitAccount() {
        App.getInstance().changeAccount(true);
    }

    @OnClick(R.id.api_state)
    void apistate() {
        if (Constant.DEBUG) {
            boolean state = OpenApi.isDEBUG();
            OpenApi.init(!state); // 设置OpenAPI的调试状态
            setApiState(OpenApi.isDEBUG());
        }
    }

    @OnClick(R.id.reset_password_bt)
    void toResetPassword() {
        Intent intent = new Intent(this, ResetPassWordActivity.class);
        String phone = App.getInstance().userResult.getParent().getPhone();
        if (StringUtil.isNotBlank(phone) && StringUtil.checkMobile(phone)) {
            intent.putExtra("phone", phone);
        }
        startActivity(intent);
    }

    @OnClick(R.id.update_bt)
    void updateVersion(View view) {
        AppManager manager = new AppManager();
        manager.updateVersion();
    }

}

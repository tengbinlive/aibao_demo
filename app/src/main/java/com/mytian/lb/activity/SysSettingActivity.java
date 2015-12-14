package com.mytian.lb.activity;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.openapi.OpenApi;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;

import butterknife.Bind;
import butterknife.OnClick;
import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

public class SysSettingActivity extends AbsActivity {

    @Bind(R.id.update_bt)
    Button updateBt;
    @Bind(R.id.api_state)
    Button apiState;

    private AppVersion firAppVersion;

    @Override
    public void EInit() {
        super.EInit();
        updateBt.setText("当前版本 ：" + App.getInstance().getAppVersionName());
        initApiState();
        updateDetect();
    }

    private void initApiState(){
        if(Constant.DEBUG){
            apiState.setVisibility(View.VISIBLE);
        }else{
            apiState.setVisibility(View.GONE);
        }
        setApiState(OpenApi.isDEBUG());
    }

    private void setApiState(boolean state){
        if(state){
            apiState.setText("API：test ,ip：10.0.1.15");
        }else{
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
         if(Constant.DEBUG){
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

    private void updateDetect() {
        FIR.checkForUpdateInFIR(Constant.FIR_API_TOKEN, new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion appVersion, boolean b) {
                firAppVersion = appVersion;
                if (firAppVersion.getVersionCode() > App.getAppVersionCode()) {
                    updateBt.setText("最新版本 ：" + firAppVersion.getVersionName());
                }
            }

            @Override
            public void onFail(String s, int i) {
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @OnClick(R.id.update_bt)
    void updateVersion(View view) {
        if (firAppVersion==null||firAppVersion.getVersionCode() <= App.getAppVersionCode()) {
            CommonUtil.showToast("已是最新版拉");
            return;
        }
        StringBuffer versionInfo = new StringBuffer();
        versionInfo.append("version :   ").append(firAppVersion.getVersionName()).append("\n")
                .append("log :   ").append(firAppVersion.getChangeLog()).append("\n\n")
                .append("download...");
        dialogAddFollow(versionInfo.toString(), firAppVersion.getUpdateUrl());
    }

    private void toDownload(String download) {
        Uri uri = Uri.parse(download);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void dialogAddFollow(String value, final String download) {
        if (StringUtil.isBlank(value)) {
            return;
        }
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.dialog_prompt, null);
        TextView valueTv = (TextView) convertView.findViewById(R.id.value);
        valueTv.setText(value);
        valueTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDownload(download);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(true) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.show();
    }

}

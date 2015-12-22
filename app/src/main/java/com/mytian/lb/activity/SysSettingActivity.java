package com.mytian.lb.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.CommonResponse;
import com.core.openapi.OpenApi;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.bean.user.SysAppUpgradeResult;
import com.mytian.lb.manager.UserManager;

import butterknife.Bind;
import butterknife.OnClick;

public class SysSettingActivity extends AbsActivity {

    @Bind(R.id.update_bt)
    Button updateBt;
    @Bind(R.id.api_state)
    Button apiState;
    @Bind(R.id.version_tv)
    TextView versionTv;

    private UserManager manager = new UserManager();

    @Override
    public void EInit() {
        super.EInit();
        versionTv.setText("version ：" + App.getInstance().getAppVersionName());
        updateBt.setText("点击更新");
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
        manager.checkNewVersion(this, activityHandler, APP_UPDATE);
    }

    private void toDownload(String download) {
        Uri uri = Uri.parse(download);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void dialogUpdate(String value, final String download) {
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


    private final static int APP_UPDATE = 0;

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APP_UPDATE:
                    loadUpdate((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadUpdate(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            SysAppUpgradeResult result = (SysAppUpgradeResult) resposne.getData();
            if (result.getVersion() > App.getAppVersionCode()) {
                StringBuffer versionInfo = new StringBuffer();
                versionInfo.append("爱宝.").append("\n\n")
                        .append("点击下载最新版").append("\n\n")
                        .append("......");
                dialogUpdate(versionInfo.toString(), result.getUrl());
            } else {
                updateBt.setText("已是最新版");
            }
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

}

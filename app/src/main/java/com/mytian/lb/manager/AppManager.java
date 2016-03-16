package com.mytian.lb.manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.core.util.CommonUtil;
import com.core.util.DateUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.bean.user.SysAppUpgradeResult;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * app 业务类  （更新&退出）.
 *
 * @author bin.teng
 */
public class AppManager {

    private static boolean isChecking; //版本更新对话框 是否显示

    private boolean isOUT; //重新登录对话框 是否显示

    private NiftyDialogBuilder dialogBuilder;

    private SysAppUpgradeResult sysAppUpgradeResult;

    private static AppManager instance;

    public static AppManager getInstance() {
        if (null == instance) {
            instance = new AppManager();
        }
        return instance;
    }

    public boolean isOUT() {
        return isOUT;
    }

    /**
     * 版本更新
     */
    public void updateVersion(final NiftyDialogBuilder _dialogBuilder) {
        if (!isChecking) {
            this.dialogBuilder = _dialogBuilder;
            isChecking = true;
            FIR.checkForUpdateInFIR(Constant.FIR_API_TOKEN, new VersionCheckCallback() {
                @Override
                public void onSuccess(String versionJson) {
                    sysAppUpgradeResult = JSON.parseObject(versionJson, SysAppUpgradeResult.class);
                    if (sysAppUpgradeResult.getVersion() > BuildConfig.VERSION_CODE) {
                        activityHandler.sendEmptyMessage(APP_DOWNLOAD);
                    } else {
                        CommonUtil.showToast(R.string.version_new);
                    }
                }

                @Override
                public void onFail(Exception exception) {
                    CommonUtil.showToast(R.string.version_fail);
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                    isChecking = false;
                    dialogDismiss();
                }
            });
        }
    }

    /**
     * 重新登录
     */
    public void reLoginApp() {
        if (!isOUT) {
            isOUT = true;
            activityHandler.sendEmptyMessageDelayed(APP_RELOGIN, 1000);
        }
    }

    private final static int APP_RELOGIN = 1;
    private final static int APP_DOWNLOAD = 4;
    private final static int DIALOGSHOW = 2;
    private final static int DIALOGDISMISS = 3;

    private Handler activityHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APP_RELOGIN:
                    dialogOUT();
                    break;
                case APP_DOWNLOAD:
                    dialogDownload();
                    break;
                case DIALOGSHOW:
                    if (null != dialogBuilder) {
                        dialogBuilder.show();
                    }
                    break;
                case DIALOGDISMISS:
                    if (null != dialogBuilder && dialogBuilder.isShowing()) {
                        dialogBuilder.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void dialogDownload() {
        StringBuffer versionInfo = new StringBuffer();
        String timeStr = sysAppUpgradeResult.getUpdated_at();
        if (timeStr.length() < 13) {
            timeStr = timeStr + "000";
        }
        versionInfo.append(sysAppUpgradeResult.getName()).append("\n\n")
                .append("更新时间：").append("\n").append(DateUtil.ConverToString(Long.parseLong(timeStr), DateUtil.YYYY_MM_DD_HH_MM_SS)).append("\n\n")
                .append("更新日志：").append("\n").append(sysAppUpgradeResult.getChangelog()).append("\n\n")
                .append("版本编号：" + sysAppUpgradeResult.getVersionShort());
        dialogUpdate(versionInfo.toString(), sysAppUpgradeResult.getInstallUrl());
    }

    private void dialogOUT() {
        dialogDismiss();
        Activity activity = App.getInstance().getCurrentActivity();
        LinearLayout convertView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_out, null);
        TextView ok = (TextView) convertView.findViewById(R.id.tv_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOUT = false;
                App.getInstance().changeAccount(false);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, activity); // .setCustomView(View
        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
        activityHandler.sendEmptyMessage(DIALOGSHOW);
    }

    private void dialogUpdate(String value, final String download) {
        if (StringUtil.isBlank(value)) {
            return;
        }
        final Activity activity = App.getInstance().getCurrentActivity();
        LinearLayout convertView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_prompt, null);
        TextView valueTv = (TextView) convertView.findViewById(R.id.value);
        Button downloadBt = (Button) convertView.findViewById(R.id.download);
        valueTv.setText(value);
        downloadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
                toDownload(activity, download);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(true) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, activity); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);
    }

    private void toDownload(Activity activity, String download) {
        Uri uri = Uri.parse(download);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    private void dialogDismiss() {
        activityHandler.sendEmptyMessage(DIALOGDISMISS);
    }
}

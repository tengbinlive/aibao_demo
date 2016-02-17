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

import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.bean.user.SysAppUpgradeResult;

/**
 * app 业务类  （更新&退出）.
 *
 * @author bin.teng
 */
public class AppManager {

    private boolean isChecking; //版本更新对话框 是否显示

    private boolean isOUT; //重新登录对话框 是否显示

    private UserManager manager = new UserManager();

    private NiftyDialogBuilder dialogBuilder;

    private SysAppUpgradeResult sysAppUpgradeResult;

    /**
     * 版本更新
     */
    public void updateVersion(NiftyDialogBuilder dialogBuilder) {
        if (!isChecking) {
            this.dialogBuilder = dialogBuilder;
            isChecking = true;
            manager.checkNewVersion(App.getInstance(), activityHandler, APP_UPDATE);
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

    private final static int APP_UPDATE = 0;
    private final static int APP_RELOGIN = 1;
    private final static int APP_DOWNLOAD = 4;
    private final static int DIALOGSHOW = 2;
    private final static int DIALOGDISMISS = 3;

    private Handler activityHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APP_UPDATE:
                    loadUpdate((CommonResponse) msg.obj);
                    break;
                case APP_RELOGIN:
                    dialogOUT();
                    break;
                case APP_DOWNLOAD:
                    dialogDownload();
                    break;
                case DIALOGSHOW:
                    dialogBuilder.show();
                    break;
                case DIALOGDISMISS:
                    dialogBuilder.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void loadUpdate(CommonResponse resposne) {
        isChecking = false;
        dialogDismiss();
        if (resposne.isSuccess()) {
            sysAppUpgradeResult = (SysAppUpgradeResult) resposne.getData();
            int versioncode = CommonUtil.getAppVersionCode(App.getInstance());
            if (sysAppUpgradeResult.getVersion() > versioncode) {
                activityHandler.sendEmptyMessageDelayed(APP_DOWNLOAD, 1000);
            } else {
                CommonUtil.showToast("已是最新版");
            }
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void dialogDownload() {
        StringBuffer versionInfo = new StringBuffer();
        versionInfo.append("爱宝.").append("\n\n")
                .append("发现新的版本").append("\n\n")
                .append("版本编号："+sysAppUpgradeResult.getVersion());
        dialogUpdate(versionInfo.toString(), sysAppUpgradeResult.getUrl());
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
        if (null != dialogBuilder && dialogBuilder.isShowing()) {
            activityHandler.sendEmptyMessage(DIALOGDISMISS);
        }
    }
}

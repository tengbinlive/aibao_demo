package com.mytian.lb.manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

    private String downloadUrl;

    /**
     * 版本更新
     */
    public void updateVersion() {
        if(!isChecking) {
            isChecking = true;
            manager.checkNewVersion(App.getInstance(), activityHandler, APP_UPDATE);
        }
    }

    /**
     * 重新登录
     */
    public void reLoginApp() {
        if(!isOUT) {
            isOUT = true;
            activityHandler.sendEmptyMessage(APP_RELOGIN);
        }
    }

    private final static int APP_UPDATE = 0;
    private final static int APP_RELOGIN = 1;
    private final static int APP_DOWNLOAD = 4;
    private final static int DIALOGSHOW = 2;
    private final static int DIALOGDISMISS = 3;

    private Handler activityHandler = new Handler() {
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
        if (resposne.isSuccess()) {
            SysAppUpgradeResult result = (SysAppUpgradeResult) resposne.getData();
            if (result.getVersion() > App.getAppVersionCode()) {
                downloadUrl = result.getUrl();
                activityHandler.sendEmptyMessage(APP_DOWNLOAD);
            } else {
                CommonUtil.showToast("已是最新版");
            }
        } else {
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void dialogDownload(){
        if(StringUtil.isNotBlank(downloadUrl)) {
            StringBuffer versionInfo = new StringBuffer();
            versionInfo.append("爱宝.").append("\n\n")
                    .append("点击下载最新版").append("\n\n")
                    .append("......");
            dialogUpdate(versionInfo.toString(), downloadUrl);
        }
    }

    private void dialogOUT() {
        dialogDismiss();
        Activity activity = App.getInstance().activityManager.currentActivity();
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
        dialogDismiss();
        final Activity activity = App.getInstance().activityManager.currentActivity();
        LinearLayout convertView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_prompt, null);
        TextView valueTv = (TextView) convertView.findViewById(R.id.value);
        valueTv.setText(value);
        valueTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

package com.mytian.lb.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.core.CommonResponse;
import com.core.util.StringUtil;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.bean.push.PushOnBindResult;
import com.mytian.lb.manager.PushMManager;

/**
 * Created by Administrator on 2015/10/30.
 */
public class PushHelper {

    private static PushHelper instance;

    public static PushHelper getInstance() {
        if (instance == null) {
            instance = new PushHelper();
        }
        return instance;
    }

    public final static int STATE_DEF = 0;//初始 默认
    public final static int STATE_NO = STATE_DEF + 1;//不可用
    public final static int STATE_YES = STATE_NO + 1;//可用
    public final static int STATE_UPDATE = STATE_YES + 1;//上传id 上传 用于延迟
    public final static int STATE_UPDATE_RE = STATE_UPDATE + 1;//上传id 返回数据
    public final static int STATE_UPDATE_NO = STATE_UPDATE_RE + 1;//上传id 上传失败
    public final static int STATE_UPDATE_YES = STATE_UPDATE_NO + 1;//上传id 上传成功

    private final static String APP_KEY = "XqXiOUPbeYEAGaOz1IfDIpKK";//test:PSszk83CkivESkUMxWAegvKZ //XqXiOUPbeYEAGaOz1IfDIpKK

    public int pushState = STATE_NO;

    private PushMManager manager = new PushMManager();

    private Context mContext;

    private PushOnBindResult bindResult;

    private long delayedTime = 5 * 60 * 1000;

    public void initPush(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (STATE_UPDATE_NO == pushState) {
            sendPushState(STATE_UPDATE);
        } else if (STATE_NO == pushState) {
            pushState =STATE_DEF;
            PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, APP_KEY);
        }
    }

    /**
     * 绑定推送成功数据设置
     * @param bindResult
     */
    public void updateChannelid(PushOnBindResult bindResult) {
        if (this.bindResult == null) {
            this.bindResult = bindResult;
            if(StringUtil.isNotBlank(bindResult.getChannelId())) {
                pushState = PushHelper.STATE_YES;
                activityHandler.sendEmptyMessage(STATE_UPDATE);
            }
        }
    }

    private void sendPushState(int state) {
        pushState = state;
        if (state == STATE_UPDATE_NO) {//上传id失败 再次上传
            if (App.isNetworkAvailable()) {
                activityHandler.sendEmptyMessageDelayed(STATE_UPDATE, delayedTime);
            }
        }else if (state == STATE_UPDATE) {//首次id上传
            if (App.isNetworkAvailable()) {
                activityHandler.sendEmptyMessage(STATE_UPDATE);
            }
        }
    }

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_UPDATE:
                    manager.updateChannelId(mContext, bindResult.getChannelId(), activityHandler, STATE_UPDATE_RE);
                    break;
                case STATE_UPDATE_RE:
                    loadData((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 上传channelid返回结果
     * @param resposne
     */
    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            pushState = STATE_UPDATE_YES;
        } else {
            sendPushState(STATE_UPDATE_NO);
        }
    }

    public void setNotification(String content){
        String ns = Context.NOTIFICATION_SERVICE;
        CharSequence tickerText = "aibao..";
        CharSequence contentTitle = "aibao..";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        mBuilder.setContentTitle(contentTitle);
        mBuilder.setTicker(tickerText);
        mBuilder.setContentText(content);
        // mBuilder.setNumber(notificationNum);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(ns);

        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
    }
}

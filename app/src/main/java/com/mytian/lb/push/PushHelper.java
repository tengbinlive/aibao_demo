package com.mytian.lb.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.core.CommonResponse;
import com.core.util.StringUtil;
import com.mytian.lb.App;
import com.mytian.lb.bean.push.PushOnBindResult;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.PushMManager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

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

    public final static int LOAD_DATA = 0;//上传id 上传成功
    public final static int STATE_ONBIND_NO = LOAD_DATA + 1;//未绑定
    public final static int STATE_ONBIND_FAILURE = STATE_ONBIND_NO + 1;//绑定失败
    public final static int STATE_UPLOAD_ID_NO = STATE_ONBIND_FAILURE + 1;//未上传
    public final static int STATE_UPLOAD_ID_FAILURE = STATE_UPLOAD_ID_NO + 1;//上传id 失败
    public final static int STATE_NORMAL = STATE_UPLOAD_ID_FAILURE + 1;//


    private final static String APP_KEY = "XqXiOUPbeYEAGaOz1IfDIpKK";//test:PSszk83CkivESkUMxWAegvKZ //XqXiOUPbeYEAGaOz1IfDIpKK

    public static final String CHANNEL_STATE = "CHANNEL_STATE";

    public int pushState = STATE_ONBIND_NO;

    public boolean UPLOAD_ID_SUCCESS;

    private PushMManager manager = new PushMManager();

    private Context mContext;

    private PushOnBindResult bindResult;

    private long delayedTime = 60 * 1000;

    public void initPush(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        sendPushState(pushState);
    }

    private void onBind() {
        PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_API_KEY, APP_KEY);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("parent");
        PushManager.setTags(mContext, tags);
    }

    /**
     * 绑定推送成功数据设置
     *
     * @param bindResult
     */
    public void updateChannelid(PushOnBindResult bindResult) {
        if (bindResult == null && StringUtil.isNotBlank(bindResult.getChannelId())) {
            this.bindResult = bindResult;
            sendPushState(STATE_UPLOAD_ID_NO);
            return;
        }
        sendPushState(STATE_ONBIND_FAILURE);
    }

    public void sendPushState(int state) {
        pushState = state;
        if (!App.isNetworkAvailable()) {
            return;
        }
        if (state == STATE_ONBIND_NO) {
            activityHandler.sendEmptyMessage(STATE_ONBIND_NO);
        } else if (state == STATE_ONBIND_FAILURE) {//绑定失败 1分钟后再次绑定
            activityHandler.sendEmptyMessageDelayed(STATE_ONBIND_NO, delayedTime);
        } else if (state == STATE_UPLOAD_ID_NO) {
            if (UPLOAD_ID_SUCCESS) {
                pushState = STATE_NORMAL;
                return;
            }
            activityHandler.sendEmptyMessage(STATE_UPLOAD_ID_NO);
        } else if (state == STATE_UPLOAD_ID_FAILURE) {//上传失败 1分钟后再次上传
            if (UPLOAD_ID_SUCCESS) {
                pushState = STATE_NORMAL;
                return;
            }
            activityHandler.sendEmptyMessageDelayed(STATE_UPLOAD_ID_NO, delayedTime);
        }
    }

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_UPLOAD_ID_NO:
                    if (App.getInstance().userResult.getParent() == null) {
                        sendPushState(STATE_UPLOAD_ID_FAILURE);
                        return;
                    }
                    manager.updateChannelId(mContext, bindResult.getChannelId(), activityHandler, LOAD_DATA);
                    break;
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj);
                    break;
                case STATE_ONBIND_NO:
                    onBind();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 上传channelid返回结果
     *
     * @param resposne
     */
    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            Logger.d("上传成功");
            UPLOAD_ID_SUCCESS = true;
            pushState = STATE_NORMAL;
            SharedPreferencesHelper.setBoolean(mContext, CHANNEL_STATE, UPLOAD_ID_SUCCESS);
        } else {
            sendPushState(STATE_UPLOAD_ID_FAILURE);
        }
    }

    public void setNotification(String content) {
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

package com.mytian.lb.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.push.PushResult;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.event.AgreementStateEventType;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.event.PushUserEventType;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.AppManager;
import com.mytian.lb.manager.PushMManager;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

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

    public final static int LOAD_DATA = 0;//上传id 返回数据
    public final static int STATE_UPLOAD_ID_NO = LOAD_DATA + 1;//未上传
    public final static int STATE_UPLOAD_ID_FAILURE = STATE_UPLOAD_ID_NO + 1;//上传id 失败
    public final static int STATE_NORMAL = STATE_UPLOAD_ID_FAILURE + 1;//

    public static final String CHANNEL_STATE = "CHANNEL_STATE";

    public int pushState = STATE_NORMAL;

    public boolean UPLOAD_ID_SUCCESS;

    public boolean UPLOAD_ING;

    private PushMManager manager = new PushMManager();

    private Context mContext;

    private String channelId;

    private long delayedTime = 60 * 1000;

    public void initPush(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        PushManager.getInstance().initialize(context);
        Tag[] tags = new Tag[1];
        Tag tag = new Tag();
        tag.setName("parent");
        tags[0] = tag;
        PushManager.getInstance().setTag(context, tags);
    }

    public void bindPush(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        sendPushState(pushState);
    }


    /**
     * 绑定推送成功数据设置
     *
     * @param _channelId 上传id
     */
    public void updateChannelid(String _channelId) {
        this.channelId = _channelId;
        sendPushState(STATE_UPLOAD_ID_NO);
    }

    public void sendPushState(int state) {
        pushState = state;
        if (!App.isNetworkAvailable() || StringUtil.isBlank(channelId)) {
            return;
        }
        activityHandler.removeMessages(pushState);
        if (state == STATE_UPLOAD_ID_NO) {
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
                    if (App.getInstance().getUserResult().getParent() == null) {
                        sendPushState(STATE_UPLOAD_ID_FAILURE);
                        return;
                    }
                    if (!UPLOAD_ING) {
                        UPLOAD_ING = true;
                        manager.updateChannelId(mContext, channelId, activityHandler, LOAD_DATA);
                    }
                    break;
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj);
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
        UPLOAD_ING = false;
        if (resposne.isSuccess()) {
            Logger.d("上传成功");
            UPLOAD_ID_SUCCESS = true;
            pushState = STATE_NORMAL;
            SharedPreferencesHelper.setBoolean(App.getInstance(), CHANNEL_STATE, UPLOAD_ID_SUCCESS);
        } else {
            sendPushState(STATE_UPLOAD_ID_FAILURE);
        }
    }

    private void showNotification(String content) {
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

    public void updateContent(String content) {
        if (Constant.DEBUG) {
            CommonUtil.showToast(content);
        }
        PushResult result = null;
        try {
            result = JSON.parseObject(content, PushResult.class);
        } catch (Exception e) {
        }
        UserResult userResult = App.getInstance().getUserResult();
        if(null == userResult){
            return;
        }
        Parent parent = App.getInstance().getUserResult().getParent();
        if(null == parent){
            return;
        }
        if (null != result && isSend(parent, result)) {
            if (PushCode.FOLLOW_NOTICE.equals(result.getCmd())) {
                String info = result.getInfo();
                FollowUser user = JSON.parseObject(info, FollowUser.class);
                if (FollowUser.MB.equals(user.getFocus_from())) {
                    EventBus.getDefault().postSticky(new PushUserEventType(user));
                }
                showNotification(result.getDescription());
            } else if (PushCode.FOLLOW_ONLINE.equals(result.getCmd()) || PushCode.FOLLOW_OFFLINE.equals(result.getCmd())) {
                String info = result.getInfo();
                String babyUid = "";
                String is_online = PushCode.FOLLOW_ONLINE.equals(result.getCmd()) ? FollowUser.ONLINE : FollowUser.OFFLINE;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    babyUid = jsonObject.optString("babyUid");
                } catch (JSONException e) {
                }
                if (StringUtil.isNotBlank(babyUid)) {
                    EventBus.getDefault().post(new PushStateEventType(babyUid, is_online));
                }
                String des = result.getDescription();
                if (StringUtil.isNotBlank(des)) {
                    showNotification(result.getDescription());
                }
            } else if (PushCode.AIBAO_UPDATE.equals(result.getCmd())) {
                AppManager manager = new AppManager();
                manager.updateVersion();
            } else if (PushCode.APPOINT_STATUS.equals(result.getCmd())) {
                String info = result.getInfo();
                String babyUid = "";
                String appointStatus = "";
                String appointer = "";
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    babyUid = jsonObject.optString("babyUid");
                    appointStatus = jsonObject.optString("appointStatus");
                    appointer = jsonObject.optString("appointer");
                } catch (JSONException e) {
                }
                if (StringUtil.isNotBlank(babyUid)) {
                    EventBus.getDefault().postSticky(new AgreementStateEventType(babyUid, appointStatus,appointer));
                }
            }
        }
    }

    private boolean isSend(Parent parent, PushResult result) {
        String uid = parent.getUid();
        return "*".equals(result.getUid()) || uid.equals(result.getUid());
    }

}

package com.mytian.lb.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import com.mytian.lb.R;
import com.mytian.lb.activity.MainActivity;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2015/10/30.
 */

public class PushHelper {

    public static final String BABY_UID = "babyUid";
    private static PushHelper instance = new PushHelper();

    public static PushHelper getInstance() {
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

    private boolean isToast;

    public void setIsToast(boolean isToast) {
        this.isToast = isToast;
    }

    public void clearChannelid() {
        UPLOAD_ID_SUCCESS = false;
        pushState = STATE_NORMAL;
        UPLOAD_ING = false;
        SharedPreferencesHelper.setBoolean(App.getInstance(), PushHelper.CHANNEL_STATE, false);
    }

    public void initPush() {
        if (mContext == null) {
            mContext = App.getInstance();
        }
        PushManager.getInstance().initialize(mContext);
        Tag[] tags = new Tag[1];
        Tag tag = new Tag();
        tag.setName("parent");
        tags[0] = tag;
        PushManager.getInstance().setTag(mContext, tags);
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
            Logger.i("上传成功");
            UPLOAD_ID_SUCCESS = true;
            pushState = STATE_NORMAL;
            SharedPreferencesHelper.setBoolean(App.getInstance(), CHANNEL_STATE, UPLOAD_ID_SUCCESS);
        } else {
            sendPushState(STATE_UPLOAD_ID_FAILURE);
        }
    }

    /**
     * 推送通知显示
     *
     * @param content
     */
    public void showNotification(String content, String type) {
        String ns = Context.NOTIFICATION_SERVICE;
        Context mContext = App.getInstance();
        CharSequence tickerText = mContext.getString(R.string.app_name);
        CharSequence contentTitle = mContext.getString(R.string.app_name);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PushCode.NOTICE_TYPE, type);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, content.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);// 设置通知栏点击意图
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//悬挂式Notification，5.0后显示
            mBuilder.setContentText(content).setFullScreenIntent(pendingIntent, true);
            mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
            mBuilder.setSmallIcon(R.drawable.notification_icon);// 设置通知小ICON（5.0必须采用白色透明图片）
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);// 设置通知小ICON
            mBuilder.setContentText(content);
        }

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));// 设置通知大ICON

        mBuilder.setTicker(tickerText);
        mBuilder.setContentTitle(contentTitle);// 设置通知栏标题
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);

        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX); // 设置该通知优先级

        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);//在任何情况下都显示，不受锁屏影响。

        Notification notification = mBuilder.build();

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(ns);

        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
    }

    /**
     * 推送数据更新
     *
     * @param content
     */
    public void updateContent(String content) {
        if (Constant.DEBUG&&isToast) {
            CommonUtil.showToast(content);
        }
        PushResult result = null;
        try {
            result = JSON.parseObject(content, PushResult.class);
        } catch (Exception e) {
        }
        UserResult userResult = App.getInstance().getUserResult();
        if (null == userResult) {
            return;
        }
        Parent parent = App.getInstance().getUserResult().getParent();
        if (null == parent) {
            return;
        }
        if (null != result && isSend(parent, result)) {
            if (PushCode.FOLLOW_NOTICE.equals(result.getCmd())) {
                String info = result.getInfo();
                FollowUser user = JSON.parseObject(info, FollowUser.class);
                if (FollowUser.MB.equals(user.getFocus_from())) {
                    EventBus.getDefault().postSticky(new PushUserEventType(user));
                }
                showNotification(result.getDescription(), result.getCmd());
            } else if (PushCode.FOLLOW_ONLINE.equals(result.getCmd()) || PushCode.FOLLOW_OFFLINE.equals(result.getCmd())) {
                String info = result.getInfo();
                String babyUid = "";
                String is_online = PushCode.FOLLOW_ONLINE.equals(result.getCmd()) ? FollowUser.ONLINE : FollowUser.OFFLINE;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    babyUid = jsonObject.optString(BABY_UID);
                } catch (JSONException e) {
                }
                if (StringUtil.isNotBlank(babyUid)) {
                    EventBus.getDefault().post(new PushStateEventType(babyUid, is_online));
                }
                String des = result.getDescription();
                if (StringUtil.isNotBlank(des)) {
                    showNotification(result.getDescription(), result.getCmd());
                }
            } else if (PushCode.AIBAO_UPDATE.equals(result.getCmd())) {
                AppManager.getInstance().updateVersion(null);
            } else if (PushCode.APPOINT_STATUS.equals(result.getCmd())) {
                String info = result.getInfo();
                String babyUid = "";
                String appointStatus = "";
                String appointer = "";
                String appoint_time = "";
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    babyUid = jsonObject.optString(BABY_UID);
                    appointStatus = jsonObject.optString("appointStatus");
                    appointer = jsonObject.optString("appointer");
                } catch (JSONException e) {
                }
                if (StringUtil.isNotBlank(babyUid)) {
                    EventBus.getDefault().postSticky(new AgreementStateEventType(babyUid, appointStatus, appointer, appoint_time));
                }
            } else if (PushCode.APPOINT_CANCEL.equals(result.getCmd())) {
                String info = result.getInfo();
                String babyUid = "";
                String appoint_time = "";
                String appointStatus = AgreementStateEventType.AGREEMENT_END;
                String appointer = "";
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    babyUid = jsonObject.optString(BABY_UID);
                    appoint_time = jsonObject.optString("appoint_time");
                } catch (JSONException e) {
                }
                if (StringUtil.isNotBlank(babyUid)) {
                    AgreementStateEventType eventType = new AgreementStateEventType(babyUid, appointStatus, appointer, appoint_time);
                    EventBus.getDefault().postSticky(eventType);
                }
            }
        }
    }

    private boolean isSend(Parent parent, PushResult result) {
        String uid = parent.getUid();
        return "*".equals(result.getUid()) || uid.equals(result.getUid());
    }

}

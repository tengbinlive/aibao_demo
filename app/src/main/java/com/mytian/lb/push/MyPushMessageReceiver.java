package com.mytian.lb.push;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.core.util.StringUtil;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.push.PushOnBindResult;
import com.mytian.lb.bean.push.PushResult;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.event.PushUserEventType;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class MyPushMessageReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Logger.d(responseString);
        if (errorCode == 0) {
            // 绑定成功
            Logger.d("push 绑定成功");
            PushOnBindResult bindResult = new PushOnBindResult();
            bindResult.setErrorCode(errorCode);
            bindResult.setAppid(appid);
            bindResult.setUserId(userId);
            bindResult.setChannelId(channelId);
            bindResult.setRequestId(requestId);
            PushHelper.getInstance().updateChannelid(bindResult);
        } else {
            PushHelper.getInstance().pushState = PushHelper.STATE_NO;
        }
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Logger.d(messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, message);
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Logger.d(notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, notifyString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Logger.d(notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
        updateNotificationContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param sucessTags 设置成功的tag
     * @param failTags   设置失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Logger.d(responseString);
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑

    }

    /**
     * delTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param sucessTags 成功删除的tag
     * @param failTags   删除失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Logger.d(responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑

    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Logger.d(responseString);

        // 更新界面展示代码，应用请在这里加入自己的处理逻辑

    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Logger.d(responseString);

        if (errorCode == 0) {
            // 解绑定成功
            Logger.d("解绑成功");
        }
        //  更新界面展示代码，应用请在这里加入自己的处理逻辑

    }

    private void updateContent(Context context, String content) {
        PushResult result = JSON.parseObject(content, PushResult.class);
        UserResult userResult = App.getInstance().userResult;
        if (null != result && null != userResult && isSend(userResult, result)) {
            if (PushCode.FOLLOW_NOTICE.equals(result.getCmd())) {
                String info = result.getInfo();
                FollowUser user = JSON.parseObject(info, FollowUser.class);
                if (FollowUser.MB.equals(user.getFocus_from())) {
                    EventBus.getDefault().postSticky(new PushUserEventType(user));
                }
                PushHelper.getInstance().setNotification(result.getDescription());
            } else if (PushCode.FOLLOW_ONLINE.equals(result.getCmd()) || PushCode.FOLLOW_OFFLINE.equals(result.getCmd())) {
                String info = result.getInfo();
                String babyUid = "";
                String is_online = PushCode.FOLLOW_ONLINE.equals(result.getCmd())?FollowUser.ONLINE:FollowUser.OFFLINE;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    babyUid = jsonObject.optString("babyUid");
                } catch (JSONException e) {
                }
                if (StringUtil.isNotBlank(babyUid)) {
                    EventBus.getDefault().postSticky(new PushStateEventType(babyUid,is_online));
                }
                if(Constant.DEBUG){
                    PushHelper.getInstance().setNotification("uid:"+babyUid+ "\nstate:"+is_online);
                }
            }
        }
    }

    private boolean isSend(UserResult userResult, PushResult result) {
        String uid = userResult.getParent().getUid();
        return "*".equals(result.getUid()) || uid.equals(result.getUid());
    }

    private void updateNotificationContent(Context context, String content) {
    }

}

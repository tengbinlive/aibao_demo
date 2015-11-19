package com.mytian.lb.bean.push;

/**
 * push 绑定成功返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class PushOnBindResult {

    private int errorCode;
    private String appid;
    private String userId;
    private String channelId;
    private String requestId;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "PushOnBindResult{" +
                "errorCode=" + errorCode +
                ", appid='" + appid + '\'' +
                ", userId='" + userId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}

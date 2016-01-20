package com.mytian.lb.event;

/**
 * 推送状态
 * Created by bin on 15/7/23.
 */
public class PushStateEventType {
    public String is_online;
    public String babyUid;

    public PushStateEventType(String babyUid,String is_online) {
        this.babyUid = babyUid;
        this.is_online = is_online;
    }
}

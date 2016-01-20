package com.mytian.lb.event;

import com.mytian.lb.bean.follow.FollowUser;

/**
 * 推送关注的新用户
 * Created by bin on 15/7/23.
 */
public class PushUserEventType {
    public FollowUser user;
    public PushUserEventType(FollowUser user) {
        this.user = user;
    }
}

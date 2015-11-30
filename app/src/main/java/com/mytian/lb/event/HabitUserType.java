package com.mytian.lb.event;

import com.mytian.lb.bean.follow.FollowUser;

/**
 * Created by bin on 15/7/23.
 */
public class HabitUserType {
    public FollowUser cureentParent;

    public HabitUserType(FollowUser cureentParent) {
        this.cureentParent = cureentParent;
    }
}

package com.mytian.lb.event;

import com.mytian.lb.bean.follow.FollowUser;

/**
 * Created by bin on 15/7/23.
 */
public class AgreementUserType {
    public FollowUser cureentParent;

    public AgreementUserType(FollowUser cureentParent) {
        this.cureentParent = cureentParent;
    }
}

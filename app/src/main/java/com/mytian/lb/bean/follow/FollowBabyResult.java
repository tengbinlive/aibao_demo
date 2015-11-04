package com.mytian.lb.bean.follow;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class FollowBabyResult extends OpenApiSimpleResult {

    /**
     * uid : 10001
     * token : 45e94dfc-9d87-4f57-a41b-69604e9fae83
     * name : 爱宝
     * sex : 1
     * phone : 13120888888
     * birthday : 1444827966973
     * head_thumb : xxx.png
     * sys_thumb_id : 10001
     * thumb_type : 1
     */

    private BabyBean baby;
    /**
     * is_focus : 1
     * focus_from : 1
     */

    private RalationBean ralation;

    public void setBaby(BabyBean baby) {
        this.baby = baby;
    }

    public void setRalation(RalationBean ralation) {
        this.ralation = ralation;
    }

    public BabyBean getBaby() {
        return baby;
    }

    public RalationBean getRalation() {
        return ralation;
    }

}

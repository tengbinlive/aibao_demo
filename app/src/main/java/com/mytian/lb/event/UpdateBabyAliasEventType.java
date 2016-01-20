package com.mytian.lb.event;

/**
 * 麦宝的备注状态
 * Created by bin on 15/7/23.
 */
public class UpdateBabyAliasEventType {
    public String baby_alias;
    public String babyUid;

    public UpdateBabyAliasEventType(String babyUid, String baby_alias) {
        this.babyUid = babyUid;
        this.baby_alias = baby_alias;
    }
}

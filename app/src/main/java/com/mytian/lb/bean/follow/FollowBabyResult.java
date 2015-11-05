package com.mytian.lb.bean.follow;

import com.core.openapi.OpenApiSimpleResult;


/**
 * Created by bin.teng on 2015/10/28.
 */
public class FollowBabyResult extends OpenApiSimpleResult {

    /**
     * end_time : 1446700037507
     * relation : null
     * start_time : 1446700037428
     * baby : {"uid":10001,"createTime":1446195667561,"birthday":0,"sex":0,"phone":"18507051259","otherRelation":"","password":"","sysThumbId":0,"isOnline":1,"id":57,"obj3":"","obj4":"","thumbType":0,"obj5":"","obj6":"","token":"1684f562-1f8f-43fa-8960-8518e4274007","obj1":"","name":"abc","obj2":"","verificationCode":"","headThumb":"","relationId":0,"idCard":"","updateTime":0,"status":1,"channelId":"4523610452366070158","alias":"","obj7":"","category":0,"coins":0,"realName":""}
     */

    private long end_time;
    private RalationBean relation;
    private long start_time;
    /**
     * uid : 10001
     * createTime : 1446195667561
     * birthday : 0
     * sex : 0
     * phone : 18507051259
     * otherRelation :
     * password :
     * sysThumbId : 0
     * isOnline : 1
     * id : 57
     * obj3 :
     * obj4 :
     * thumbType : 0
     * obj5 :
     * obj6 :
     * token : 1684f562-1f8f-43fa-8960-8518e4274007
     * obj1 :
     * name : abc
     * obj2 :
     * verificationCode :
     * headThumb :
     * relationId : 0
     * idCard :
     * updateTime : 0
     * status : 1
     * channelId : 4523610452366070158
     * alias :
     * obj7 :
     * category : 0
     * coins : 0
     * realName :
     */

    private BabyBean baby;

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public void setRelation(RalationBean relation) {
        this.relation = relation;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public void setBaby(BabyBean baby) {
        this.baby = baby;
    }

    public long getEnd_time() {
        return end_time;
    }

    public RalationBean getRelation() {
        return relation;
    }

    public long getStart_time() {
        return start_time;
    }

    public BabyBean getBaby() {
        return baby;
    }


}

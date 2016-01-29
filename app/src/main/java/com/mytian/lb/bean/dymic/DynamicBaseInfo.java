package com.mytian.lb.bean.dymic;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class DynamicBaseInfo extends OpenApiSimpleResult {

    //0：麦宝（需解析uid等） 1：系统.
    public final static String TYPE_MB = "0";
    public final static String TYPE_SYS = "1";
    private String fromType;
    private String showType;
    private String fromName;
    private String uid;
    private String alias;
    private String head_thumb;
    private String sys_thumb_id;
    private String thumb_type;
    private String time;

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHead_thumb() {
        return head_thumb;
    }

    public void setHead_thumb(String head_thumb) {
        this.head_thumb = head_thumb;
    }

    public String getSys_thumb_id() {
        return sys_thumb_id;
    }

    public void setSys_thumb_id(String sys_thumb_id) {
        this.sys_thumb_id = sys_thumb_id;
    }

    public String getThumb_type() {
        return thumb_type;
    }

    public void setThumb_type(String thumb_type) {
        this.thumb_type = thumb_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DynamicBaseInfo{" +
                "fromType='" + fromType + '\'' +
                ", showType='" + showType + '\'' +
                ", fromName='" + fromName + '\'' +
                ", uid='" + uid + '\'' +
                ", alias='" + alias + '\'' +
                ", head_thumb='" + head_thumb + '\'' +
                ", sys_thumb_id='" + sys_thumb_id + '\'' +
                ", thumb_type='" + thumb_type + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

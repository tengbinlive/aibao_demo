package com.mytian.lb.bean.action;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class AgreementParam extends OpenApiBaseRequestAdapter {

    private String uid;
    private String token;
    private String babyUid;
    private long time;
    private String appointId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBabyUid() {
        return babyUid;
    }

    public void setBabyUid(String babyUid) {
        this.babyUid = babyUid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAppointId() {
        return appointId;
    }

    public void setAppointId(String appointId) {
        this.appointId = appointId;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.token)) return false;
        if (StringUtil.isBlank(this.babyUid)) return false;
        if (StringUtil.isBlank(this.time)) return false;
        if (StringUtil.isBlank(this.appointId)) return false;
        return true;
    }

    @Override
    public void fill2Map(Map<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(babyUid)))
            param.put("parent.babyUid", babyUid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(time)))
            param.put("parent.time", time);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(appointId)))
            param.put("parent.appointId", appointId);
    }

    @Override
    public String toString() {
        return "AgreementParam{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", babyUid='" + babyUid + '\'' +
                ", time='" + time + '\'' +
                ", appointId='" + appointId + '\'' +
                '}';
    }
}

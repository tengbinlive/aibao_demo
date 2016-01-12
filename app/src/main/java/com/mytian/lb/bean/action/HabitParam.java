package com.mytian.lb.bean.action;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;

public class HabitParam extends OpenApiBaseRequestAdapter {

    private String uid;
    private String token;
    private String isPraise;
    private String habitId;
    private String babyUid;
    private String client_type;

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

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.token)) return false;
        if (StringUtil.isBlank(this.babyUid)) return false;
        if (StringUtil.isBlank(this.isPraise)) return false;
        if (StringUtil.isBlank(this.habitId)) return false;
        if (StringUtil.isBlank(this.client_type)) return false;
        return true;
    }

    @Override
    public void fill2Map(HashMap<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(isPraise)))
            param.put("isPraise", isPraise);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(habitId)))
            param.put("habitId", habitId);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(babyUid)))
            param.put("babyUid", babyUid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(client_type)))
            param.put("client_type", client_type);
    }

    @Override
    public String toString() {
        return "HabitParam{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", isPraise='" + isPraise + '\'' +
                ", habitId='" + habitId + '\'' +
                ", babyUid='" + babyUid + '\'' +
                ", client_type='" + client_type + '\'' +
                '}';
    }
}

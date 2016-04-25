package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class updateRemarkNameParam extends OpenApiBaseRequestAdapter {

    private String uid;

    private String token;

    private String client_type;

    private String babyId;

    private String parentId;

    private String babyAlias;

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

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getBabyAlias() {
        return babyAlias;
    }

    public void setBabyAlias(String babyAlias) {
        this.babyAlias = babyAlias;
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
        if (StringUtil.isBlank(this.client_type)) return false;
        if (StringUtil.isBlank(this.babyId)) return false;
        if (StringUtil.isBlank(this.parentId)) return false;
        return true;
    }

    @Override
    public void fill2Map(Map<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(client_type)))
            param.put("client_type", client_type);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(babyId)))
            param.put("babyParentFocus.babyId", babyId);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(parentId)))
            param.put("babyParentFocus.parentId", parentId);
        if (includeEmptyAttr || (!includeEmptyAttr))
            param.put("babyParentFocus.babyAlias", babyAlias);
    }

    @Override
    public String toString() {
        return "updateRemarkNameParam{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", client_type='" + client_type + '\'' +
                ", babyId='" + babyId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", babyAlias='" + babyAlias + '\'' +
                '}';
    }
}

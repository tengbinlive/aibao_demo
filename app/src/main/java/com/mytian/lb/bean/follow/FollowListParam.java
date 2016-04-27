package com.mytian.lb.bean.follow;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class FollowListParam extends OpenApiBaseRequestAdapter {

    private String token;

    private String uid;

    private String client_type;

    private String currentPage;

    private String parentId;

    private String isFocus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.token)) return false;
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.client_type)) return false;
        if (StringUtil.isBlank(this.currentPage)) return false;
        if (StringUtil.isBlank(this.parentId)) return false;
        if (StringUtil.isBlank(this.isFocus)) return false;
        return true;
    }

    @Override
    public void fill2Map(Map<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(client_type)))
            param.put("client_type", client_type);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(currentPage)))
            param.put("currentPage", currentPage);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(parentId)))
            param.put("babyParentFocus.parentId", parentId);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(isFocus)))
            param.put("babyParentFocus.isFocus", isFocus);
    }

    @Override
    public String toString() {
        return "FollowListParam{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", client_type='" + client_type + '\'' +
                ", currentPage='" + currentPage + '\'' +
                ", parentId='" + parentId + '\'' +
                ", isFocus='" + isFocus + '\'' +
                '}';
    }
}

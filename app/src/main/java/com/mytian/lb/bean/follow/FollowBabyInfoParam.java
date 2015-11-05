package com.mytian.lb.bean.follow;

import com.core.openapi.OpenApiBaseRequest;
import com.core.openapi.OpenApiRequestInterface;
import com.core.util.StringUtil;

import java.util.HashMap;

public class FollowBabyInfoParam extends OpenApiBaseRequest implements OpenApiRequestInterface {

    private String token;

    private String uid;

    private String client_type;

    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.token)) return false;
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.client_type)) return false;
        if (StringUtil.isBlank(this.phone)) return false;
        return true;
    }

    @Override
    public void fill2Map(HashMap<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(client_type)))
            param.put("client_type", client_type);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(phone)))
            param.put("baby.phone", phone);
    }

    @Override
    public String toString() {
        return "FollowBabyInfoParam{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", client_type='" + client_type + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

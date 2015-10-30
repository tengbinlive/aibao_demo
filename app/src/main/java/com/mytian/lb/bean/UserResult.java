package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class UserResult extends OpenApiSimpleResult {
    private String uid;
    private String token;

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

    @Override
    public String toString() {
        return "UserResult{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

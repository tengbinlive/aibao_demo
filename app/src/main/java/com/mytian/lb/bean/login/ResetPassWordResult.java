package com.mytian.lb.bean.login;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class ResetPassWordResult extends OpenApiSimpleResult {
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
        return "RegisterResult{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

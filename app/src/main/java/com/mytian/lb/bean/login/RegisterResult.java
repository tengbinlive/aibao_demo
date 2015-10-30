package com.mytian.lb.bean.login;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class RegisterResult extends OpenApiSimpleResult {
    private String uid;
    private String is_init_account;
    private String token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_init_account() {
        return is_init_account;
    }

    public void setIs_init_account(String is_init_account) {
        this.is_init_account = is_init_account;
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
                ", is_init_account='" + is_init_account + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class UserResult extends OpenApiSimpleResult {
    private String uid;
    private String token;
    private String phone;
    private String name;
    private String head;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}

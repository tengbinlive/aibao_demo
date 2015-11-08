package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 用户信息
 * Created by bin.teng on 2015/10/28.
 */
public class ParentBean extends OpenApiSimpleResult {
    private String uid;
    private String token;
    private String phone;
    private String name;
    private String sex;
    private String birthday;
    private String headThumb;
    private String sysThumbId;
    private String thumbType;


    public String getThumbType() {
        return thumbType;
    }

    public void setThumbType(String thumbType) {
        this.thumbType = thumbType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadThumb() {
        return headThumb;
    }

    public void setHeadThumb(String headThumb) {
        this.headThumb = headThumb;
    }

    public String getSysThumbId() {
        return sysThumbId;
    }

    public void setSysThumbId(String sysThumbId) {
        this.sysThumbId = sysThumbId;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", headThumb='" + headThumb + '\'' +
                ", sysThumbId='" + sysThumbId + '\'' +
                ", thumbType='" + thumbType + '\'' +
                '}';
    }
}

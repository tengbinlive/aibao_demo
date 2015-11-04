package com.mytian.lb.bean.follow;

/**
 * Created by bin.teng on 2015/10/28.
 */

public class BabyBean {
    private String uid;
    private String token;
    private String name;
    private String sex;
    private String phone;
    private String birthday;
    private String head_thumb;
    private String sys_thumb_id;
    private String thumb_type;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setHead_thumb(String head_thumb) {
        this.head_thumb = head_thumb;
    }

    public void setSys_thumb_id(String sys_thumb_id) {
        this.sys_thumb_id = sys_thumb_id;
    }

    public void setThumb_type(String thumb_type) {
        this.thumb_type = thumb_type;
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getHead_thumb() {
        return head_thumb;
    }

    public String getSys_thumb_id() {
        return sys_thumb_id;
    }

    public String getThumb_type() {
        return thumb_type;
    }
}

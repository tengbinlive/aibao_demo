package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 关注用户信息
 * Created by bin.teng on 2015/10/28.
 */
public class ParentFocusBean extends OpenApiSimpleResult {
    private String uid;
    private String phone;
    private String alias;
    private String sex;
    private String birthday;
    private String head_thumb;
    private String sys_thumb_id;
    private String thumb_type;
    private String is_online;
    private String create_time;
    private String focus_from;
    private String focus_time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getHead_thumb() {
        return head_thumb;
    }

    public void setHead_thumb(String head_thumb) {
        this.head_thumb = head_thumb;
    }

    public String getSys_thumb_id() {
        return sys_thumb_id;
    }

    public void setSys_thumb_id(String sys_thumb_id) {
        this.sys_thumb_id = sys_thumb_id;
    }

    public String getThumb_type() {
        return thumb_type;
    }

    public void setThumb_type(String thumb_type) {
        this.thumb_type = thumb_type;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFocus_from() {
        return focus_from;
    }

    public void setFocus_from(String focus_from) {
        this.focus_from = focus_from;
    }

    public String getFocus_time() {
        return focus_time;
    }

    public void setFocus_time(String focus_time) {
        this.focus_time = focus_time;
    }

    @Override
    public String toString() {
        return "ParentFocusBean{" +
                "uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", alias='" + alias + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", head_thumb='" + head_thumb + '\'' +
                ", sys_thumb_id='" + sys_thumb_id + '\'' +
                ", thumb_type='" + thumb_type + '\'' +
                ", is_online='" + is_online + '\'' +
                ", create_time='" + create_time + '\'' +
                ", focus_from='" + focus_from + '\'' +
                ", focus_time='" + focus_time + '\'' +
                '}';
    }
}

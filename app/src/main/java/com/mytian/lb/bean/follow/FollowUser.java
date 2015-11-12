package com.mytian.lb.bean.follow;

import com.mytian.lb.R;

/**
 * Created by bin.teng on 2015/10/28.
 */

public class FollowUser {
    private String uid;
    private String name;
    private String sex;
    private String category;
    private String phone;
    private String is_online;
    private String create_time;
    private String birthday;
    private String head_thumb;
    private String sys_thumb_id;
    private String thumb_type;
    private String focus_from;
    private String focus_time;
    private String relation_id;
    private String relation_name;
    private String other_relation;
    private String searchKey;
    private boolean focus;
    private int head_id;

    public int getHead_id() {
        return head_id;
    }

    public void setHead_id(int head_id) {
        this.head_id = head_id;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

    public String getOther_relation() {
        return other_relation;
    }

    public void setOther_relation(String other_relation) {
        this.other_relation = other_relation;
    }

    public static FollowUser testData(int index) {
        FollowUser result = new FollowUser();
        if(index == 0) {
            result.name = "韩梅梅";
            result.phone = "18217612155";
            result.head_thumb = "http://img1.pcgames.com.cn/pcgames/1101/30/2125889_1.gif";
            result.head_id = R.mipmap.head_1;
        }else if(index ==1){
            result.name = "李雷";
            result.phone = "18812187512";
            result.head_thumb = "http://img1.pcgames.com.cn/pcgames/1101/30/2125889_1.gif";
            result.head_id = R.mipmap.head_2;
        }else if(index ==2){
            result.name = "安徒生";
            result.phone = "18812127521";
            result.head_thumb = "http://img1.pcgames.com.cn/pcgames/1101/30/2125889_1.gif";
            result.head_id = R.mipmap.head_3;
        }else if(index ==3){
            result.name = "契柯夫";
            result.phone = "18812127111";
            result.head_thumb = "http://img1.pcgames.com.cn/pcgames/1101/30/2125889_1.gif";
            result.head_id = R.mipmap.head_4;
        }
        return result;
    }

    @Override
    public String toString() {
        return "FollowUser{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", category='" + category + '\'' +
                ", phone='" + phone + '\'' +
                ", is_online='" + is_online + '\'' +
                ", create_time='" + create_time + '\'' +
                ", birthday='" + birthday + '\'' +
                ", head_thumb='" + head_thumb + '\'' +
                ", sys_thumb_id='" + sys_thumb_id + '\'' +
                ", thumb_type='" + thumb_type + '\'' +
                ", focus_from='" + focus_from + '\'' +
                ", focus_time='" + focus_time + '\'' +
                ", relation_id='" + relation_id + '\'' +
                ", relation_name='" + relation_name + '\'' +
                ", focus='" + focus + '\'' +
                ", other_relation='" + other_relation + '\'' +
                ", searchKey='" + searchKey + '\'' +
                '}';
    }
}

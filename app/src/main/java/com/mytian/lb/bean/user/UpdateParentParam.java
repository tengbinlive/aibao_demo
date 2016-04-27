package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateParentParam extends OpenApiBaseRequestAdapter {

    private String uid;

    private String token;

    private String alias;

    private String realName;

    private int sex;//性别(0女，1男)

    private long birthday;

    private String headThumb;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getHeadThumb() {
        return headThumb;
    }

    public void setHeadThumb(String headThumb) {
        this.headThumb = headThumb;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.token)) return false;
        return true;
    }

    @Override
    public void fill2Map(Map<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(alias)))
            param.put("parent.alias", alias);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(realName)))
            param.put("parent.realName", realName);
        if (includeEmptyAttr || (!includeEmptyAttr && sex>=0))
            param.put("parent.sex", sex);
        if (includeEmptyAttr || (!includeEmptyAttr && birthday>0))
            param.put("parent.birthday", birthday);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(headThumb)))
            param.put("parent.headThumb", headThumb);
    }

    @Override
    public String toString() {
        return "UpdateParentParam{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", alias='" + alias + '\'' +
                ", realName='" + realName + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", headThumb='" + headThumb + '\'' +
                '}';
    }
}

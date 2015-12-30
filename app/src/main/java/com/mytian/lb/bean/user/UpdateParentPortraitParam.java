package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.io.File;
import java.util.HashMap;

public class UpdateParentPortraitParam extends OpenApiBaseRequestAdapter {

    private String uid;

    private String token;

    private File headPortrait;

    private String client_type;

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

    public File getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(File headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.token)) return false;
        return true;
    }

    @Override
    public void fill2Map(HashMap<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(client_type)))
            param.put("client_type", client_type);
    }

    @Override
    public void fill2FileMap(HashMap<String, File> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && null != headPortrait))
            param.put("headPortrait", headPortrait);
    }

    @Override
    public String toString() {
        return "UpdateParentPortraitParam{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", headPortrait=" + headPortrait +
                ", client_type='" + client_type + '\'' +
                '}';
    }
}

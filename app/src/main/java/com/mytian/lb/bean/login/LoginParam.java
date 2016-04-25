package com.mytian.lb.bean.login;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginParam extends OpenApiBaseRequestAdapter {

    private String phone;

    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.phone)) return false;
        if (StringUtil.isBlank(this.password)) return false;
        return true;
    }

    @Override
    public void fill2Map(Map<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(password)))
            param.put("parent.password", password);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(phone)))
            param.put("parent.phone", phone);
    }

    @Override
    public String toString() {
        return "LoginParam{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

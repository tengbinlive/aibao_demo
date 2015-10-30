package com.mytian.lb.bean.login;

import com.core.openapi.OpenApiBaseRequest;
import com.core.openapi.OpenApiRequestInterface;
import com.core.util.StringUtil;

import java.util.HashMap;

public class RegisterParam extends OpenApiBaseRequest implements OpenApiRequestInterface {

    private String phone;

    private String verification;

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

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.phone)) return false;
        if (StringUtil.isBlank(this.verification)) return false;
        if (StringUtil.isBlank(this.password)) return false;
        return true;
    }

    @Override
    public void fill2Map(HashMap<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(password)))
            param.put("parent.password", password);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(phone)))
            param.put("parent.phone", phone);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(phone)))
            param.put("parent.verificationCode", verification);
    }

    @Override
    public String toString() {
        return "RegisterParam{" +
                "phone='" + phone + '\'' +
                ", verification='" + verification + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

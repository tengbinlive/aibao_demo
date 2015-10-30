package com.bin.kndle.bean.login;

import com.core.openapi.OpenApiBaseRequest;
import com.core.openapi.OpenApiRequestInterface;
import com.core.util.StringUtil;

import java.util.HashMap;

public class AuthCodeParam extends OpenApiBaseRequest implements OpenApiRequestInterface {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.phone)) return false;
        return true;
    }

    @Override
    public void fill2Map(HashMap<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(phone)))
            param.put("phone", phone);
    }

    @Override
    public String toString() {
        return "AuthCodeParam{" +
                "phone='" + phone + '\'' +
                '}';
    }
}

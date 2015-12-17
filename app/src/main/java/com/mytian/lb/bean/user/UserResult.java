package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;
import com.dao.Parent;

/**
 * 登录返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class UserResult extends OpenApiSimpleResult {

    private Parent parent;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "parent=" + parent +
                '}';
    }
}

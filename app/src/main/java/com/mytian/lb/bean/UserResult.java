package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 登录返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class UserResult extends OpenApiSimpleResult {
    private ParentBean parent;

    public ParentBean getParent() {
        return parent;
    }

    public void setParent(ParentBean parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "parent=" + parent +
                '}';
    }
}

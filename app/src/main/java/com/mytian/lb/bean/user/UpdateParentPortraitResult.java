package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 上传头像返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class UpdateParentPortraitResult extends OpenApiSimpleResult {

    private String headPortraitUrl;

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    @Override
    public String toString() {
        return "UpdateParentPortraitResult{" +
                "headPortraitUrl='" + headPortraitUrl + '\'' +
                '}';
    }
}

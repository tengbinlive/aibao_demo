package com.mytian.lb.bean.dymic;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class Dynamic extends OpenApiSimpleResult {
    private String id;
    private DynamicBaseInfo baseInfo;
    private DynamicContent content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DynamicBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(DynamicBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public DynamicContent getContent() {
        return content;
    }

    public void setContent(DynamicContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "id='" + id + '\'' +
                ", baseInfo=" + baseInfo +
                ", content=" + content +
                '}';
    }
}

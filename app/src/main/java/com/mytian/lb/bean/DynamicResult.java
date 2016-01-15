package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class DynamicResult extends OpenApiSimpleResult {
    private String uid;
    private String alias;
    private String head;
    private String date;
    private String desc;
    private String content;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DynamicResult{" +
                "alias='" + alias + '\'' +
                ", head='" + head + '\'' +
                ", date='" + date + '\'' +
                ", desc='" + desc + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

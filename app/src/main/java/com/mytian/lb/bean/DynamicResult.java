package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class DynamicResult extends OpenApiSimpleResult {
    private String name;
    private String head;
    private String date;
    private String desc;
    private String content;
    private int headid;

    public int getHeadid() {
        return headid;
    }

    public void setHeadid(int headid) {
        this.headid = headid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", date='" + date + '\'' +
                ", desc='" + desc + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 执行项目
 * Created by bin.teng on 2015/10/28.
 */
public class AgreementBean extends OpenApiSimpleResult {

    public final static int GREAT = 1;
    public final static int BAD = GREAT + 1;
    private String alias;
    private String head;
    private String date;
    private int record;
    private String title;
    private int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "HabitResult{" +
                "alias='" + alias + '\'' +
                ", head='" + head + '\'' +
                ", date='" + date + '\'' +
                ", record=" + record +
                ", title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}

package com.mytian.lb.bean.habitUser;

import com.core.openapi.OpenApiSimpleResult;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class HabitResult extends OpenApiSimpleResult {
    public final static int GREAT = 1;
    public final static int BAD = GREAT+1;
    private String name;
    private String head;
    private String date;
    private int record;

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

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public static HabitResult testData() {
        HabitResult result = new HabitResult();
        result.name = "宝宝";
        result.head = "http://img1.pcgames.com.cn/pcgames/1101/30/2125889_1.gif";
        result.date = "五分钟前";
        return result;
    }

    @Override
    public String toString() {
        return "DynamicResult{" +
                "name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

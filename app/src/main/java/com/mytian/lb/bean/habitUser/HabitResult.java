package com.mytian.lb.bean.habitUser;

import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.R;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class HabitResult extends OpenApiSimpleResult {
    public final static int GREAT = 1;
    public final static int BAD = GREAT+1;
    private String name;
    private String head;
    private int headIcon;
    private String date;
    private int record;

    public int getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(int headIcon) {
        this.headIcon = headIcon;
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

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public static HabitResult testData(int index) {
        HabitResult result = new HabitResult();
        if(index == 4 ) {
            result.name = "写作业";
            result.headIcon = R.mipmap.icon_love_hb1;
        }else if(index == 3 ){
            result.name = "看书";
            result.headIcon = R.mipmap.icon_love_hb2;
        }else if(index == 1 ){
            result.name = "做家务";
            result.headIcon = R.mipmap.icon_love_hb3;
        }else if(index == 2 ){
            result.name = "吃饭";
            result.headIcon = R.mipmap.icon_love_hb4;
        }else if(index == 0 ){
            result.name = "出去玩";
            result.headIcon = R.mipmap.icon_love_hb5;
        }else if(index == 5 ){
            result.name = "睡觉";
            result.headIcon = R.mipmap.icon_love_hb6;
        }
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

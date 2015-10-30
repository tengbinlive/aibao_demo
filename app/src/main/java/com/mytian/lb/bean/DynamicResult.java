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

    public static DynamicResult testData(){
        DynamicResult result= new DynamicResult();
        result.name = "宝宝";
        result.head = "http://img1.pcgames.com.cn/pcgames/1101/30/2125889_1.gif";
        result.date = "五分钟前";
        result.desc = "麦宝客户端";
        result.content ="       现在DotA的节奏越来越快，不像以前那样可以太安心打钱了，所以前期推荐升满一技能，推进和防推进都有相当的好处，当然对防gank也有一定用处，然后主加射程可以让火枪有更多的输出空间。后面点满爆头让火枪Dps更高，跟人对点也能拼得过。";
        return result;
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

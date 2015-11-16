package com.mytian.lb.bean;

import com.amulyakhare.textdrawable.TextDrawable;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.App;
import com.mytian.lb.R;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class DynamicResult extends OpenApiSimpleResult {
    public final static int TYPE_SYS = 2;
    public final static int TYPE_MAIBAO = 3;
    public final static int TYPE_AIBAO = 4;
    private String alias;
    private String head;
    private String date;
    private String desc;
    private String content;
    private int headid;
    private int type;
    private TextDrawable drawable;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TextDrawable getDrawable() {
        if (drawable == null) {
            int radius = (int) App.getInstance().getResources().getDimension(R.dimen.rounded_size);
            drawable = TextDrawable.builder()
                    .buildRoundRect("sys", 0xffffca0d,radius);
        }
        return drawable;
    }

    public void setDrawable(TextDrawable drawable) {
        this.drawable = drawable;
    }

    public int getHeadid() {
        return headid;
    }

    public void setHeadid(int headid) {
        this.headid = headid;
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
                ", headid=" + headid +
                ", type=" + type +
                ", drawable=" + drawable +
                '}';
    }
}

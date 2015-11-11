package com.mytian.lb.bean;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 执行项目
 * Created by bin.teng on 2015/10/28.
 */
public class AgreementBean extends OpenApiSimpleResult {

    private String title;

    private int icon;

    public AgreementBean(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AgreementBean{" +
                "title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}

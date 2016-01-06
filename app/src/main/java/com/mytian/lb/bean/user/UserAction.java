package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 动作
 * Created by bin.teng on 2015/10/28.
 */
public class UserAction extends OpenApiSimpleResult {

    public final static int GREAT = 1;
    public final static int BAD = GREAT + 1;

    private String url;
    private String cfg_id;
    private String des;
    private int record;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCfg_id() {
        return cfg_id;
    }

    public void setCfg_id(String cfg_id) {
        this.cfg_id = cfg_id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "url='" + url + '\'' +
                ", cfg_id='" + cfg_id + '\'' +
                ", des='" + des + '\'' +
                ", record=" + record +
                '}';
    }
}

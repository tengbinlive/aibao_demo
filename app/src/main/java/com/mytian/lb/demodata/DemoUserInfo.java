package com.mytian.lb.demodata;

import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.bean.AgreementBean;
import com.mytian.lb.bean.follow.FollowUser;

import java.util.ArrayList;

/**
 * demo数据
 * Created by bin.teng on 2015/10/28.
 */
public class DemoUserInfo extends OpenApiSimpleResult {

    private FollowUser parent;

    private ArrayList<AgreementBean> beans;

    private String id;

    private int headid;

    public int getHeadid() {
        return headid;
    }

    public void setHeadid(int headid) {
        this.headid = headid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AgreementBean> getBeans() {
        return beans;
    }

    public void setBeans(ArrayList<AgreementBean> beans) {
        this.beans = beans;
    }

    public FollowUser getParent() {
        return parent;
    }

    public void setParent(FollowUser parent) {
        this.parent = parent;
    }

}

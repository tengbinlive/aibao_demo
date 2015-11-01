package com.mytian.lb.bean.follow;

import com.core.openapi.OpenApiSimpleResult;

import java.util.ArrayList;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class FollowListResult extends OpenApiSimpleResult {
    private ArrayList<FollowUser> list;

    public ArrayList<FollowUser> getList() {
        return list;
    }

    public void setList(ArrayList<FollowUser> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FollowListResult{" +
                "list=" + list +
                '}';
    }
}

package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;

import java.util.ArrayList;

/**
 * 动作列表
 * Created by bin.teng on 2015/10/28.
 */
public class UpdateActionResult extends OpenApiSimpleResult {

    private ArrayList<UserAction> list;

    public ArrayList<UserAction> getList() {
        return list;
    }

    public void setList(ArrayList<UserAction> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "UpdateActionResult{" +
                "list=" + list +
                '}';
    }
}

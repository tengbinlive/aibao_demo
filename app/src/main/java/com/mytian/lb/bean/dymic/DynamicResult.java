package com.mytian.lb.bean.dymic;

import com.core.openapi.OpenApiSimpleResult;

import java.util.ArrayList;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class DynamicResult extends OpenApiSimpleResult {
    private ArrayList<Dynamic> list;

    public ArrayList<Dynamic> getList() {
        return list;
    }

    public void setList(ArrayList<Dynamic> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DynamicResult{" +
                "list=" + list +
                '}';
    }
}

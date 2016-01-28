package com.mytian.lb.bean.action;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 约定数据
 * Created by bin.teng on 2015/10/28.
 */
public class AgreementResult extends OpenApiSimpleResult {

    private String appoint_time;

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    @Override
    public String toString() {
        return "AgreementResult{" +
                "appoint_time='" + appoint_time + '\'' +
                '}';
    }
}

package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;

/**
 * 更新返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class SysAppUpgradeResult extends OpenApiSimpleResult {

    private int version;
    private String url;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SysAppUpgradeResult{" +
                "version=" + version +
                ", url='" + url + '\'' +
                '}';
    }
}

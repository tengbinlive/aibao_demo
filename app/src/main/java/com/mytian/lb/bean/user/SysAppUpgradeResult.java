package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;


/**
 * 更新返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class SysAppUpgradeResult extends OpenApiSimpleResult {

    private int version;
    private String versionShort;
    private String build;
    private String installUrl;
    private String name;
    private String changelog;
    private String updated_at;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "SysAppUpgradeResult{" +
                "version=" + version +
                ", versionShort='" + versionShort + '\'' +
                ", build='" + build + '\'' +
                ", installUrl='" + installUrl + '\'' +
                ", name='" + name + '\'' +
                ", changelog='" + changelog + '\'' +
                ", updated_at=" + updated_at +
                '}';
    }
}

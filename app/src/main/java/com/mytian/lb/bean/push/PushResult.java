package com.mytian.lb.bean.push;


import java.io.Serializable;

/**
 * 推送数据.
 *
 * @author bin.teng
 */
public class PushResult implements Serializable {

    /**
     * 指令码
     */
    private String cmd;

    /**
     * "当前用户uid", //用于百度推送区分
     */
    private String uid;

    /**
     * 返回结果
     */
    private String info;

    /**
     * 描述
     */
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "PushResult{" +
                "cmd='" + cmd + '\'' +
                ", uid='" + uid + '\'' +
                ", info='" + info + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
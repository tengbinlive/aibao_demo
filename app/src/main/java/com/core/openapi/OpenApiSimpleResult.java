package com.core.openapi;


import java.io.Serializable;

/**
 * OpenAPI返回的简单结果类.
 *
 * @author bin.teng
 */
public class OpenApiSimpleResult implements Serializable {

    /**
     * 返回代码
     */
    private String CODE;

    /**
     * 错误结果
     */
    private String error;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 描述
     */
    private String description;

    /**
     * 扩展属性
     */
    private String MESG;

    /**
     * 扩展属性 信息提示
     */
    private String CAUSE;

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMESG() {
        return MESG;
    }

    public void setMESG(String MESG) {
        this.MESG = MESG;
    }

    public String getCAUSE() {
        return CAUSE;
    }

    public void setCAUSE(String CAUSE) {
        this.CAUSE = CAUSE;
    }

    @Override
    public String toString() {
        return "OpenApiSimpleResult{" +
                "CODE='" + CODE + '\'' +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", description='" + description + '\'' +
                ", MESG='" + MESG + '\'' +
                ", CAUSE='" + CAUSE + '\'' +
                '}';
    }
}
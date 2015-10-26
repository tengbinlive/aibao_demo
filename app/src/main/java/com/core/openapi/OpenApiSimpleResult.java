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
     * 扩展属性
     */
    private String MESG;

    /**
     * 扩展属性 信息提示
     */
    private String CAUSE;

    public String getCAUSE() {
        return CAUSE;
    }

    public void setCAUSE(String CAUSE) {
        this.CAUSE = CAUSE;
    }

    /**
     * @return 返回代码
     */
    public String getCode() {
        return CODE;
    }

    /**
     * @param code 返回代码
     */
    public void setCode(String code) {
        this.CODE = code;
    }

    /**
     * @return 返回错误结果
     */
    public String getError() {
        return error;
    }

    /**
     * @param error 错误结果
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return 返回结果
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result 返回结果
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return 返回扩展属性
     */
    public String getExtMsg() {
        return MESG;
    }

    /**
     * @param extMsg 扩展属性
     */
    public void setExtMsg(String extMsg) {
        this.MESG = extMsg;
    }

    @Override
    public String toString() {
        return "OpenApiSimpleResult{" +
                "CODE='" + CODE + '\'' +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", MESG='" + MESG + '\'' +
                ", CAUSE='" + CAUSE + '\'' +
                '}';
    }
}
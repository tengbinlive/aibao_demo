package com.core.enums;

/**
 * 返回码枚举类.
 *
 * @author HuangChao
 */
public enum CodeEnum {


    //---------http默认的错误代码---------//
    /**
     * 请求时返回404
     */
    _404("404", "服务暂不可用"),

    //---------OpenAPI定义的错误代码---------//
    //---------本应用自定义的错误代码---------//
    /** 还没有登录 */
    LOGIN_REQUIRED("9001", "还没有登录"),
    /**
     * 缺少必要的参数
     */
    PARAM_REQUIRED("9100", "缺少必要的参数"),

    /**
     * 数据转换错误
     */
    DATA_PARSE_ERROR("9103", "数据转换错误"),
    /**
     * 网络连接超时
     */
    CONNECT_UNAVAILABLE("9104", "服务器繁忙"),
    /**
     * 网络连接超时
     */
    CONNECT_TIMEOUT("9105", "网络连接超时"),
    /**
     * 无法连接网络
     */
    UNKNOWN_HOST("9106", "无法连接网络"),
    /**
     * 网络异常
     */
    NETWORK_EXCEPTION("9107", "糟糕，网络不可用"),
    /**
     * 未知的数据格式
     */
    UNKNOWN_DATA_FORMAT("9108", "未知的数据格式"),
    /**
     * 未知错误
     */
    UNKNOWN("9998", "未知错误"),
    /**
     * 自定义异常(描述一般为Exception的getLocalizedMessage())
     */
    EXCEPTION("9999", "未知错误"),
    /**
     * 成功
     */
    SUCCESS("1", "成功");

    private String code;
    private String desc;

    CodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 根据编号值获得对应枚举对象.
     *
     * @param code 编号值
     * @return 对应枚举对象
     */
    public static CodeEnum getEnumByCode(String code) {
        for (CodeEnum item : values()) {
            if (item.code.equals(code)) return item;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(",code=").append(this.getCode());
        buf.append(",desc=").append(this.getDesc());
        return buf.toString();
    }
}

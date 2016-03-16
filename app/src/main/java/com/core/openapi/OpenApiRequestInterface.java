package com.core.openapi;

import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;

/**
 * OpenAPI参数对象接口.
 *
 * @author bin.teng
 */
public interface OpenApiRequestInterface {

    /**
     * 获得OpenAPI接口方法枚举
     *
     * @return OpenAPI接口方法枚举
     */
    OpenApiMethodEnum getMethod();

    /**
     * 获得OpenAPI接口返回数据解析目标TypeToken
     *
     * @return 数据解析目标TypeToken
     */
    TypeReference<?> getParseTypeToken();

    /**
     * 检查OpenAPI接口接口需要的参数是否有效.
     *
     * @return 参数正确返回true, 否则返回false
     */
    boolean validate();

    /**
     * 获取OpenAPI接口的参数MAP对象.
     *
     * @return OpenAPI接口的参数MAP对象
     */
    HashMap<String, Object> getParamMap();


}

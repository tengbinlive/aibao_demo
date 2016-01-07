package com.core.openapi;

import com.mytian.lb.BuildConfig;

/**
 * OpenAPI管理类
 *
 * @author bin.teng
 */
public class OpenApi {

    public final static String URL_TYPE_IMAGE = "URL_TYPE_IMAGE";

    public final static String URL_TYPE_DATA = "URL_TYPE_DATA";

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String MD5 = "md5";

    public static final String FORMAT_JSON = "openapi_json";

    public static final String FORMAT_XML = "openapi_xml";

    /**
     * 根据OpenAPI方法名称枚举给出对应的API地址.
     *
     * @param method OpenAPI方法名称枚举
     * @return
     */
    public static String getApiPath(OpenApiMethodEnum method) {
        // 根据是否为DEBUG环境返回对应的URL
        String url = BuildConfig.API_HOST;
        return url + method.getCode();
    }

}
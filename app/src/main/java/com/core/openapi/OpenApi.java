package com.core.openapi;

/**
 * OpenAPI管理类
 * 
 * @author  bin.teng
 */
public class OpenApi {

	private static boolean DEBUG = true;

	public final static String URL_TYPE_IMAGE = "URL_TYPE_IMAGE";

	public final static String URL_TYPE_DATA = "URL_TYPE_DATA";

	/** 调试环境OpenAPI地址 */
//	private static final String DEBUG_API_PATH = "http://120.26.68.22:8080/investors";
	private static final String DEBUG_API_PATH = "http://121.40.50.223:8080/investors";

	/** 正式环境OpenAPI地址 */
	private static final String PROD_API_PATH = "http://120.26.68.22:8080/investors";

	private static final String CHARSET_UTF8 = "UTF-8";

	private static final String MD5 = "md5";

	public static final String FORMAT_JSON = "openapi_json";

	public static final String FORMAT_XML = "openapi_xml";

	/**
	 * 初始化OpenAPI
	 * 
	 * @param debug
	 */
	public static void init(boolean debug) {
		OpenApi.DEBUG = debug;
	}

	/**
	 * 根据OpenAPI方法名称枚举给出对应的API地址.
	 *
	 * @param method OpenAPI方法名称枚举
	 * @return
	 */
	public static String getApiPath(OpenApiMethodEnum method) {
		// 根据是否为DEBUG环境返回对应的URL
		String url = (DEBUG) ? DEBUG_API_PATH : PROD_API_PATH;
		// 正式环境中, 如果为特殊方法名称,需要替换http为https
		if (!DEBUG) {
			switch (method) {
			default:
				break;
			}
		}
		return url+method.getCode();
	}

}
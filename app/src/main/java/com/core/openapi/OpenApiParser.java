package com.core.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.core.CommonResponse;
import com.core.enums.CodeEnum;
import com.core.util.StringUtil;
import com.orhanobut.logger.Logger;


/**
 * OpenApi数据解析工具类.
 *
 * @author bin.teng
 */
public class OpenApiParser {

	private static final String TAG = OpenApiParser.class.getSimpleName();

	private static final String JSON_ELEMENT_CODE = "CODE";
	private static final String JSON_ELEMENT_MESG = "MESG";
	private static final String JSON_ELEMENT_CAUSE = "CAUSE";

	private static final String JSON_VALUE_SUCCESS_CODE = "1000";

	/**
	 * 从JSON数据中解析为指定对象.
	 *
	 * @param str JSON格式的字符串
	 * @param typeToken 目标类型
	 * @param response 通用返回对象
	 * @return 解析后的对象
	 */
	public static Object parseFromJson(String str, TypeReference<?> typeToken, CommonResponse response, boolean rawData) {
		Object obj = null;
		if (str != null) {
			try {
				if (rawData) response.setRawData(str);

				JSONObject jsonObject= JSON.parseObject(str);

				String code = jsonObject.getString(JSON_ELEMENT_CODE);
				String mesg = jsonObject.getString(JSON_ELEMENT_MESG);
				String cause = jsonObject.getString(JSON_ELEMENT_CAUSE);

				// 先判断code
				if (StringUtil.isBlank(code) || !JSON_VALUE_SUCCESS_CODE.equals(code)) {
					response.setData(null);
					response.setCode(code);
					response.setMsg(StringUtil.isBlank(cause)?mesg:cause);
				}
				// 返回的结果为成功数据
				else {
					obj = JSON.parseObject(str, typeToken);
					response.setData(obj);
					response.setMsg(StringUtil.isBlank(cause) ? mesg : cause);
					response.setCodeEnum(CodeEnum.SUCCESS);
				}
			} catch (Exception e) {
				response.setData(null);
				response.setCode(CodeEnum.EXCEPTION.getCode());
				response.setMsg(e.getLocalizedMessage());
				Logger.d(str);
			} catch (OutOfMemoryError e) {
				System.gc();
				response.setData(null);
				response.setCode(CodeEnum.EXCEPTION.getCode());
				response.setMsg(e.getLocalizedMessage());
				Logger.d(str);
			}
		}
		return obj;
	}

}

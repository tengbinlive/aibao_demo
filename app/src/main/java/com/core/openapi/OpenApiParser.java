package com.core.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.core.CommonResponse;
import com.core.enums.CodeEnum;
import com.core.util.StringUtil;
import com.mytian.lb.manager.AppManager;
import com.orhanobut.logger.Logger;


/**
 * OpenApi数据解析工具类.
 *
 * @author bin.teng
 */
public class OpenApiParser {

    private static final String TAG = OpenApiParser.class.getSimpleName();

    private static final String JSON_ELEMENT_CODE = "result";
    private static final String JSON_ELEMENT_MESG = "description";

    private static final String JSON_VALUE_SUCCESS_CODE = "1";
    private static final String JSON_VALUE_OUT_CODE = "-1";// 账号退出

    /**
     * 从JSON数据中解析为指定对象.
     *
     * @param str       JSON格式的字符串
     * @param typeToken 目标类型
     * @param response  通用返回对象
     * @return 解析后的对象
     */
    public static Object parseFromJson(String str, TypeReference<?> typeToken, CommonResponse response, boolean rawData) {
        Object obj = null;
        String mesg = "";
        if (str != null) {
            try {
                if (rawData) response.setRawData(str);

                JSONObject jsonObject = JSON.parseObject(str);

                String code = jsonObject.getString(JSON_ELEMENT_CODE);
                mesg = jsonObject.getString(JSON_ELEMENT_MESG);

                // 先判断code
                if (StringUtil.isNotBlank(code) && JSON_VALUE_OUT_CODE.equals(code)) {
                    AppManager manager = new AppManager();
                    manager.reLoginApp();
                    response.setData(null);
                    response.setCodeEnum(CodeEnum.LOGIN_REQUIRED);
                } else if (StringUtil.isBlank(code) || !JSON_VALUE_SUCCESS_CODE.equals(code)) {
                    response.setData(null);
                    response.setCode(code);
                    response.setMsg(StringUtil.isBlank(mesg) ? mesg : mesg);
                }
                // 返回的结果为成功数据
                else {
                    obj = JSON.parseObject(str, typeToken);
                    response.setData(obj);
                    response.setMsg(StringUtil.isBlank(mesg) ? mesg : mesg);
                    response.setCodeEnum(CodeEnum.SUCCESS);
                }
            } catch (Exception e) {
                response.setData(null);
                response.setCode(CodeEnum.EXCEPTION.getCode());
                String mes = StringUtil.isBlank(mesg)?e.getLocalizedMessage():mesg;
                response.setMsg(mes);
                Logger.d(str);
            } catch (OutOfMemoryError e) {
                System.gc();
                response.setData(null);
                response.setCode(CodeEnum.EXCEPTION.getCode());
                String mes = StringUtil.isBlank(mesg)?e.getLocalizedMessage():mesg;
                response.setMsg(mes);
                Logger.d(str);
            }
        }
        return obj;
    }

}

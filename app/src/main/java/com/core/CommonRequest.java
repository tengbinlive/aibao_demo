package com.core;

import android.os.Handler;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.*;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.core.enums.CodeEnum;
import com.core.openapi.OpenApi;
import com.core.openapi.OpenApiParamHelper;
import com.core.openapi.OpenApiParser;
import com.core.openapi.OpenApiRequestInterface;
import com.core.util.CommonUtil;
import com.orhanobut.logger.Logger;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 通用请求对象.
 *
 * @author bin.teng
 */
public class CommonRequest extends Request<CommonResponse> {

    private static final String TAG = CommonRequest.class.getSimpleName();

    /**
     * 本次请求的参数(进行处理后,已生成签名)
     */
    private boolean mValid = false;

    /**
     * 是否保留为转换的原始返回数据
     */
    private boolean mRawData = false;

    /**
     * 本次请求的参数(进行处理后,已生成签名)
     */
    private HashMap<String, String> mParam;

    /**
     * 返回的数据格式
     */
    private String mFormat;

    private CommonCallback mCallback;

    private Handler mHandler;

    private Integer mHandlerMsgCode;

    /**
     * 返回的数据需要转换的目标类型
     */
    private TypeReference<?> mTypeToken;

    /**
     * 请求正常返回的监听器
     */
    private Listener<CommonResponse> mListener;

    public CommonCallback getCallback() {
        return mCallback;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public int getHandlerMsgCode() {
        return mHandlerMsgCode;
    }

    /**
     * @return 返回该Request是否正确创建
     */
    public boolean isValid() {
        return mValid;
    }

    /**
     * 根据Volley错误对象返回CommonResponse对象并写入错误信息.
     *
     * @param error Volley错误对象
     * @return 返回CommonResponse对象并写入错误信息s
     */
    private static CommonResponse getErrorCommonResponse(VolleyError error) {
        CommonResponse response = null;
        Throwable cause = error.getCause();
        if (cause == null) {
            cause = error;
        }
        if (cause instanceof TimeoutException) {
            response = new CommonResponse(CodeEnum._404);
        } else if (cause instanceof TimeoutException) {
            response = new CommonResponse(CodeEnum.CONNECT_TIMEOUT);
        } else if (cause instanceof ConnectTimeoutException) {
            response = new CommonResponse(CodeEnum.CONNECT_TIMEOUT);
        } else if (cause instanceof TimeoutError) {
            response = new CommonResponse(CodeEnum.CONNECT_TIMEOUT);
        } else if (cause instanceof UnknownHostException) {
            response = new CommonResponse(CodeEnum.UNKNOWN_HOST);
        } else if (cause instanceof IOException) {
            response = new CommonResponse(CodeEnum.NETWORK_EXCEPTION);
        } else {
            response = new CommonResponse(CodeEnum.EXCEPTION.getCode(), cause.getLocalizedMessage());
        }
        return response;
    }

    public CommonRequest(OpenApiRequestInterface paramObj, final CommonCallback callback) {
        // 调用父类构造方法
        super(Method.POST, OpenApi.getApiPath(paramObj.getMethod()), new Response.ErrorListener() {
            // 通讯错误时的执行代码
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonDataLoader.callback(callback, getErrorCommonResponse(error));
            }
        });
        // 检查参数是否正确
        if (!paramObj.validate()) {
            CommonDataLoader.callback(callback, new CommonResponse(CodeEnum.PARAM_REQUIRED));
            return;
        }
        // 保存要传的参数, 发送时会调用getParams()方法获取
        mParam = OpenApiParamHelper.PrepareParam2API(paramObj);
        // 通讯正确的执行代码
        mListener = new Response.Listener<CommonResponse>() {
            @Override
            public void onResponse(CommonResponse response) {
                CommonDataLoader.callback(callback, response);
            }
        };

        mCallback = callback;
        mHandler = null;
        mHandlerMsgCode = 0;

        // 返回的数据格式
        mFormat = paramObj.getMethod().getFormat();

        // 数据转换目标类型
        mTypeToken = paramObj.getParseTypeToken();

        if (Constant.DEBUG) {
            StringBuffer buf = new StringBuffer();
            StringBuffer buf2 = new StringBuffer();
            buf2.append(OpenApi.getApiPath(paramObj.getMethod()));
            buf2.append("?");
            for (String key : mParam.keySet()) {
                buf.append(key).append("=").append(mParam.get(key)).append("\n");
                buf2.append(key).append("=").append(mParam.get(key)).append("&");
            }
            Logger.d(buf.toString());
            Logger.d(buf2.toString());
        }

        // 设置该Request正确创建
        mValid = true;
    }

    /**
     * 构造方法(针对OpenAPI的构造方法)
     *
     * @param paramObj       OpenAPI请求参数对象(未进行预处理,没有生成签名)
     * @param handler        返回后的回调Handler
     * @param handlerMsgCode 返回后的回调Handler所需要的what参数值
     */
    public CommonRequest(OpenApiRequestInterface paramObj, final Handler handler, final int handlerMsgCode) {
        // 调用父类构造方法
        super(Method.POST, OpenApi.getApiPath(paramObj.getMethod()), new Response.ErrorListener() {
            // 通讯错误时的执行代码
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtil.delivery2Handler(handler, handlerMsgCode, getErrorCommonResponse(error));
            }
        });
        // 检查参数是否正确
        if (!paramObj.validate()) {
            CommonUtil.delivery2Handler(handler, handlerMsgCode, new CommonResponse(CodeEnum.PARAM_REQUIRED));
            return;
        }
        // 保存要传的参数, 发送时会调用getParams()方法获取
        mParam = OpenApiParamHelper.PrepareParam2API(paramObj);
        // 通讯正确的执行代码
        mListener = new Response.Listener<CommonResponse>() {
            @Override
            public void onResponse(CommonResponse response) {
                CommonUtil.delivery2Handler(handler, handlerMsgCode, response);
            }
        };

        mHandler = handler;
        mHandlerMsgCode = handlerMsgCode;

        // 返回的数据格式
        mFormat = paramObj.getMethod().getFormat();

        // 数据转换目标类型
        mTypeToken = paramObj.getParseTypeToken();

        if (Constant.DEBUG) {
            StringBuffer buf = new StringBuffer();
            StringBuffer buf2 = new StringBuffer();
            buf2.append(OpenApi.getApiPath(paramObj.getMethod()));
            buf2.append("?");
            for (String key : mParam.keySet()) {
                buf.append(key).append("=").append(mParam.get(key)).append("\n");
                buf2.append(key).append("=").append(mParam.get(key)).append("&");
            }
            Logger.d(buf.toString());
            Logger.d(buf2.toString());
        }

        // 设置该Request正确创建
        mValid = true;
    }


    /**
     * 设置自定义Listenter
     */
    public void setListener(Response.Listener<CommonResponse> listener) {
        if (listener != null) {
            mListener = listener;
        }
    }

    /**
     * 设置是否保留为转换的原始返回数据
     */
    public void setRawData(boolean b) {
        mRawData = b;
    }

    /**
     * 通信结束后返回的回调方法.
     *
     * @param networkResponse 返回的响应结果对象
     */
    @Override
    protected Response<CommonResponse> parseNetworkResponse(NetworkResponse networkResponse) {
        CommonResponse response = new CommonResponse();
        // 获得字符串返回结果
        String jsonString;
        try {
            App.getInstance().cookie  = networkResponse.headers.get(SET_COOKIE_KEY);
            jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,"UTF-8"));
            Logger.d(jsonString);
            // 转换返回结果为指定对象
            this.doParse(jsonString, mFormat, mTypeToken, response, mRawData);
        } catch (UnsupportedEncodingException e) {
            response.setCodeEnum(CodeEnum.DATA_PARSE_ERROR);
        }
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    /**
     * 解析返回数据.
     *
     * @param str       返回的字符串数据
     * @param format    返回的数据类型
     * @param typeToken 转换的目标类型
     * @param response  通用返回对象
     */
    private void doParse(String str, String format, TypeReference<?> typeToken, CommonResponse response, boolean rawData) {
        // 如果来自OpenApi且是JSON格式
        if (OpenApi.FORMAT_JSON.equals(format)) {
            OpenApiParser.parseFromJson(str, typeToken, response, rawData);
        }
        // 其他未知格式
        else {
            response.setCodeEnum(CodeEnum.UNKNOWN_DATA_FORMAT);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParam;
    }

    @Override
    protected void deliverResponse(CommonResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    private static final String SET_COOKIE_KEY = "Set-Cookie";

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> headers = super.getHeaders();
        if (headers == null) {
            headers = new HashMap<>();
        }
        String sessionId = App.getInstance().cookie;
        if(sessionId!=null&&!sessionId.equals("")&&sessionId.length()>0){
            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pairs = it.next();
                if(pairs.getValue()==null){
                    headers.put(SET_COOKIE_KEY, sessionId);                }
            }
        }
        return headers;
    }


}

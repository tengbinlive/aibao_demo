package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApi;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.core.util.MultipartRequest;
import com.mytian.lb.App;
import com.mytian.lb.bean.user.SysAppUpgradeParam;
import com.mytian.lb.bean.user.SysAppUpgradeResult;
import com.mytian.lb.bean.user.UpdateParentParam;
import com.mytian.lb.bean.user.UpdateParentPortraitParam;
import com.mytian.lb.helper.MultiEntityRequest;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户中心业务类.
 *
 * @author bin.teng
 */
public class UserManager {

    private static final String TAG = UserManager.class.getSimpleName();

    /**
     * 信息补全
     *
     * @param context        上下文
     * @param param          补全信息
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void updateParent(Context context, UpdateParentParam param, final Handler handler, final int handlerMsgCode) {

        param.setUid(App.getInstance().getUserResult().getParent().getUid());
        param.setToken(App.getInstance().getUserResult().getParent().getToken());
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_UPDATEPARENT);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 应用更新
     */
    public void checkNewVersion(final Context context, final Handler handler, final Integer handlerMsgCode) {

        // 接口参数
        SysAppUpgradeParam param = new SysAppUpgradeParam();
        param.setUid(App.getInstance().getUserResult().getParent().getUid());
        param.setToken(App.getInstance().getUserResult().getParent().getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        // 接口参数
        param.setMethod(OpenApiMethodEnum.APP_UPGRADE);
        param.setParseTokenType(new TypeReference<SysAppUpgradeResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 头像上传
     *
     * @param context        上下文
     * @param headPortrait   头像
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void updateParentPortrait(Context context, File headPortrait, final Handler handler, final int handlerMsgCode) {
        UpdateParentPortraitParam param = new UpdateParentPortraitParam();
        param.setUid(App.getInstance().getUserResult().getParent().getUid());
        param.setToken(App.getInstance().getUserResult().getParent().getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setHeadPortrait(headPortrait);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_UPDATEPARENTPORTRAIT);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 头像上传
     *
     * @param context        上下文
     * @param headPortrait   头像
     */
    public void updateParentPortrait(Context context, String headPortrait) {
        String url  =  "http://172.16.77.73:1337";
        HashMap<String, String> params = new HashMap<>();
        params.put("uid",App.getInstance().getUserResult().getParent().getUid());
        params.put("token",App.getInstance().getUserResult().getParent().getToken());
        params.put("client_type","1");
        String fileKey = "headPortrait";
        ArrayList<String> fileUrlParams = new ArrayList<>();
        fileUrlParams.add(headPortrait);
        MultipartRequest request = new MultipartRequest(Request.Method.POST,url, params,fileUrlParams,fileKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String responseStr = "";
                try {
                    responseStr = new String(response.getBytes("gbk"),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                Logger.d(responseStr);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.getMessage());
            }
        });

        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

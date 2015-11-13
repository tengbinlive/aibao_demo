package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.App;
import com.mytian.lb.bean.user.UpdateParentParam;

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

        param.setUid(App.getInstance().userResult.getParent().getUid());
        param.setToken(App.getInstance().userResult.getParent().getToken());
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_UPDATEPARENT);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }



}

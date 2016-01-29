package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.dao.Parent;
import com.mytian.lb.App;
import com.mytian.lb.bean.dymic.DynamicParam;
import com.mytian.lb.bean.dymic.DynamicResult;

/**
 * 动态业务类.
 *
 * @author bin.teng
 */
public class DynamicManager {

    private static final String TAG = DynamicManager.class.getSimpleName();

    /**
     * 获取最新动态
     *
     * @param context        上下文
     * @param currentPage    分页，请求第N页数据
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void dymicList(Context context, String currentPage, final Handler handler, final int handlerMsgCode) {

        DynamicParam param = new DynamicParam();
        Parent parent = App.getInstance().getUserResult().getParent();
        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setPage(currentPage);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_DYMICLIST);
        param.setParseTokenType(new TypeReference<DynamicResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

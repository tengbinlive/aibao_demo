package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.mytian.lb.App;
import com.mytian.lb.bean.follow.FollowListParam;
import com.mytian.lb.bean.follow.FollowListResult;

/**
 * 用户中心业务类.
 *
 * @author bin.teng
 */
public class FollowManager {

    private static final String TAG = FollowManager.class.getSimpleName();

    /**
     * 获取关注列表
     *
     * @param context        上下文
     * @param client_type    0 MB端, 1 LB端
     * @param currentPage    分页，请求第N页数据
     * @param parentId       爱宝uid
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followList(Context context, String client_type, String currentPage, String parentId, final Handler handler, final int handlerMsgCode) {

        FollowListParam param = new FollowListParam();
        param.setUid(App.getInstance().userResult.getUid());
        param.setToken(App.getInstance().userResult.getToken());
        param.setClient_type(client_type);
        param.setCurrentPage(currentPage);
        param.setParentId(parentId);
        param.setIsFocus("1");//0 获取待处理列表,1 获取好友的列表.
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_LIST);
        param.setParseTokenType(new TypeReference<FollowListResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

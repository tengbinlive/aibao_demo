package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.App;
import com.mytian.lb.bean.follow.FollowAddParam;
import com.mytian.lb.bean.follow.FollowAgreeParam;
import com.mytian.lb.bean.follow.FollowListParam;
import com.mytian.lb.bean.follow.FollowListResult;

/**
 * 关注业务类.
 *
 * @author bin.teng
 */
public class FollowManager {

    private static final String TAG = FollowManager.class.getSimpleName();

    /**
     * 获取关注列表
     *
     * @param context        上下文
     * @param currentPage    分页，请求第N页数据
     * @param isFocus        0 获取待处理列表,1 获取好友的列表
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followList(Context context, String currentPage, String isFocus, final Handler handler, final int handlerMsgCode) {

        FollowListParam param = new FollowListParam();
        param.setUid(App.getInstance().userResult.getUid());
        param.setToken(App.getInstance().userResult.getToken());
        param.setClient_type("1");// 0 MB端, 1 LB端
        param.setCurrentPage(currentPage);
        param.setParentId(App.getInstance().userResult.getUid());
        param.setIsFocus(isFocus);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_LIST);
        param.setParseTokenType(new TypeReference<FollowListResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 接受关注
     *
     * @param context        上下文
     * @param babyId         麦宝uid
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followAgree(Context context, String babyId, final Handler handler, final int handlerMsgCode) {

        FollowAgreeParam param = new FollowAgreeParam();
        param.setUid(App.getInstance().userResult.getUid());
        param.setToken(App.getInstance().userResult.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setBabyId(babyId);
        param.setParentId(App.getInstance().userResult.getUid());
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_AGREE);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 添加关注
     *
     * @param context        上下文
     * @param babyId         麦宝uid
     * @param relationId     爱宝和麦宝的关系
     * @param other_relation 关系选择其他时的描述
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followAdd(Context context, String babyId, String relationId, String other_relation, final Handler handler, final int handlerMsgCode) {

        FollowAddParam param = new FollowAddParam();
        param.setUid(App.getInstance().userResult.getUid());
        param.setToken(App.getInstance().userResult.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setBabyId(babyId);
        param.setParentId(App.getInstance().userResult.getUid());
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_ADD);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

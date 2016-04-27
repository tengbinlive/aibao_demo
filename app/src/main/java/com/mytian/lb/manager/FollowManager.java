package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.mytian.lb.App;
import com.mytian.lb.bean.follow.FollowAddParam;
import com.mytian.lb.bean.follow.FollowAgreeParam;
import com.mytian.lb.bean.follow.FollowBabyInfoParam;
import com.mytian.lb.bean.follow.FollowBabyResult;
import com.mytian.lb.bean.follow.FollowCanCelParam;
import com.mytian.lb.bean.follow.FollowListParam;
import com.mytian.lb.bean.follow.FollowListResult;

/**
 * 关注业务类.
 *
 * @author bin.teng
 */
public class FollowManager {


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
        Parent parent = App.getInstance().getUserResult().getParent();

        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setCurrentPage(currentPage);
        param.setParentId(parent.getUid());
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
     * 爱宝同意麦宝关注
     *
     * @param context        上下文
     * @param babyId         麦宝uid
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followAgree(Context context, String babyId, final Handler handler, final int handlerMsgCode) {

        FollowAgreeParam param = new FollowAgreeParam();
        Parent parent = App.getInstance().getUserResult().getParent();

        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setBabyId(babyId);
        param.setParentId(parent.getUid());
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
     * 爱宝关注列表
     *
     * @param context        上下文
     * @param babyId         麦宝uid
     * @param other_relation 关系选择其他时的描述
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followAdd(Context context, String babyId, String other_relation, String relationId, final Handler handler, final int handlerMsgCode) {

        FollowAddParam param = new FollowAddParam();
        Parent parent = App.getInstance().getUserResult().getParent();

        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setBabyId(babyId);
        param.setRelationId(relationId);
        if (StringUtil.isNotBlank(other_relation)) {
            param.setOther_relation(other_relation);
        }
        param.setParentId(parent.getUid());
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_ADD);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 查找麦宝账户信息
     *
     * @param context        上下文
     * @param phone          手机号码
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followgetbaby(Context context, String phone, final Handler handler, final int handlerMsgCode) {

        FollowBabyInfoParam param = new FollowBabyInfoParam();
        Parent parent = App.getInstance().getUserResult().getParent();

        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setPhone(phone);

        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_BABY);
        param.setParseTokenType(new TypeReference<FollowBabyResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 爱宝解除关注麦宝
     *
     * @param context        上下文
     * @param babyId         麦宝uid
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void followcancel(Context context, String babyId, final Handler handler, final int handlerMsgCode) {

        FollowCanCelParam param = new FollowCanCelParam();
        Parent parent = App.getInstance().getUserResult().getParent();

        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setBabyId(babyId);
        param.setParentId(parent.getUid());

        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_FOLLOW_CANCEL);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }


}

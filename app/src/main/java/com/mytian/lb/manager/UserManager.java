package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.dao.Parent;
import com.mytian.lb.App;
import com.mytian.lb.bean.user.SysAppUpgradeParam;
import com.mytian.lb.bean.user.SysAppUpgradeResult;
import com.mytian.lb.bean.user.UpdateParentParam;
import com.mytian.lb.bean.user.UpdateParentPortraitParam;
import com.mytian.lb.bean.user.UpdateParentPortraitResult;
import com.mytian.lb.bean.user.updateRemarkNameParam;

import java.io.File;

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
        Parent parent = App.getInstance().getUserResult().getParent();
        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
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
        Parent parent = App.getInstance().getUserResult().getParent();
        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
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
        Parent parent = App.getInstance().getUserResult().getParent();
        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setHeadPortrait(headPortrait);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_UPDATEPARENTPORTRAIT);
        param.setParseTokenType(new TypeReference<UpdateParentPortraitResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 修改麦宝备注名称
     *
     * @param context        上下文
     * @param remarkName     备注名称
     * @param babyid         麦宝id
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void updateRemarkName(Context context, String babyid, String remarkName, final Handler handler, final int handlerMsgCode) {
        updateRemarkNameParam param = new updateRemarkNameParam();
        Parent parent = App.getInstance().getUserResult().getParent();
        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setClient_type("1");//0 MB端, 1 LB端
        param.setBabyAlias(remarkName);
        param.setBabyId(babyid);
        param.setParentId(parent.getUid());
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_REMARK_NAME);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

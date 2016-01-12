package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.App;
import com.mytian.lb.bean.action.AgreementParam;
import com.mytian.lb.bean.action.HabitParam;

/**
 *  约定业务类.
 *
 * @author bin.teng
 */
public class UserActionManager {

    private static final String TAG = UserActionManager.class.getSimpleName();

    /**
     * 爱的约定
     *
     * @param context
     * @param babyUid
     * @param time
     * @param appointId
     * @param handler
     * @param handlerMsgCode
     */
    public void sendAgreement(Context context, String babyUid, long time, String appointId, final Handler handler, final int handlerMsgCode) {

        AgreementParam param = new AgreementParam();
        param.setUid(App.getInstance().getUserResult().getParent().getUid());
        param.setToken(App.getInstance().getUserResult().getParent().getToken());
        param.setBabyUid(babyUid);
        param.setTime(time);
        param.setAppointId(appointId);

        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_LOVEAPPOINT);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 取消爱的约定
     *
     * @param context
     * @param babyUid
     * @param appointId
     * @param handler
     * @param handlerMsgCode
     */
    public void cancleAgreement(Context context, String babyUid, String appointId, final Handler handler, final int handlerMsgCode) {

        AgreementParam param = new AgreementParam();
        param.setUid(App.getInstance().getUserResult().getParent().getUid());
        param.setToken(App.getInstance().getUserResult().getParent().getToken());
        param.setBabyUid(babyUid);
        param.setAppointId(appointId);

        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_CANCELLOVEAPPOINT);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 取消爱的约定
     *
     * @param context
     * @param babyUid
     * @param isPraise 0 贬,1 赞
     * @param handler
     * @param handlerMsgCode
     */
    public void sendHabit(Context context, String babyUid,String appointId, String isPraise, final Handler handler, final int handlerMsgCode) {

        HabitParam param = new HabitParam();
        param.setUid(App.getInstance().getUserResult().getParent().getUid());
        param.setToken(App.getInstance().getUserResult().getParent().getToken());
        param.setBabyUid(babyUid);
        param.setHabitId(appointId);
        param.setIsPraise(isPraise);
        param.setClient_type("1");//0 MB端, 1 LB端

        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_MBHABIT);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }


}

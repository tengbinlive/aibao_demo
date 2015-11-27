package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.App;
import com.mytian.lb.bean.other.AgreementParam;

/**
 *  约定业务类.
 *
 * @author bin.teng
 */
public class AgreementManager {

    private static final String TAG = AgreementManager.class.getSimpleName();

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
        param.setUid(App.getInstance().userResult.getParent().getUid());
        param.setToken(App.getInstance().userResult.getParent().getToken());
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
        param.setUid(App.getInstance().userResult.getParent().getUid());
        param.setToken(App.getInstance().userResult.getParent().getToken());
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


}

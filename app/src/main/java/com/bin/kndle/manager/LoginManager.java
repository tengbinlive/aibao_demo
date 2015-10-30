package com.bin.kndle.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.bin.kndle.bean.UserResult;
import com.bin.kndle.bean.login.AuthCodeParam;
import com.bin.kndle.bean.login.LoginParam;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;

/**
 * 用户中心业务类.
 *
 * @author bin.teng
 */
public class LoginManager {

    private static final String TAG = LoginManager.class.getSimpleName();

    /**
     * 获取验证码
     *
     * @param context        上下文
     * @param phone          手机号码
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void authCode(Context context, String phone, final Handler handler, final int handlerMsgCode) {

        AuthCodeParam param = new AuthCodeParam();
        param.setPhone(phone);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_GET_CODE);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 登录
     * @param context
     * @param phone 手机号码
     * @param password 密码
     * @param handler
     * @param handlerMsgCode
     */
    public void login(Context context, String phone,String password, final Handler handler, final int handlerMsgCode) {

        LoginParam param = new LoginParam();
        param.setPhone(phone);
        param.setPassword(password);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_LOGIN);
        param.setParseTokenType(new TypeReference<UserResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

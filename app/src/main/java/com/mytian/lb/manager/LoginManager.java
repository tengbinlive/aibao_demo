package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.bean.login.AuthCodeParam;
import com.mytian.lb.bean.login.LoginParam;
import com.mytian.lb.bean.login.RegisterParam;
import com.mytian.lb.bean.login.RegisterResult;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.bean.login.ResetPassWordParam;
import com.mytian.lb.bean.login.ResetPassWordResult;

/**
 * 登录业务类.
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

    /**
     * 注册
     * @param context
     * @param phone 手机号码
     * @param verification 验证码
     * @param password 密码
     * @param handler
     * @param handlerMsgCode
     */
    public void register(Context context, String phone,String verification,String password, final Handler handler, final int handlerMsgCode) {

        RegisterParam param = new RegisterParam();
        param.setPhone(phone);
        param.setVerification(verification);
        param.setPassword(password);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_REGISTER);
        param.setParseTokenType(new TypeReference<RegisterResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

    /**
     * 注册
     * @param context
     * @param phone 手机号码
     * @param verification 验证码
     * @param password 密码
     * @param handler
     * @param handlerMsgCode
     */
    public void resetpassword(Context context, String phone,String verification,String password, final Handler handler, final int handlerMsgCode) {

        ResetPassWordParam param = new ResetPassWordParam();
        param.setPhone(phone);
        param.setVerification(verification);
        param.setPassword(password);
        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_RESET_PASSWORD);
        param.setParseTokenType(new TypeReference<ResetPassWordResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }

}

package com.core.openapi;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.core.CommonResponse;
import com.core.enums.CodeEnum;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.orhanobut.logger.Logger;


/**
 * OpenApi数据解析工具类.
 *
 * @author bin.teng
 */
public class OpenApiParser {

    private static final String TAG = OpenApiParser.class.getSimpleName();

    private static final String JSON_ELEMENT_CODE = "result";
    private static final String JSON_ELEMENT_MESG = "description";

    private static final String JSON_VALUE_SUCCESS_CODE = "1";
    private static final String JSON_VALUE_OUT_CODE = "-1";// 账号退出

    /**
     * 从JSON数据中解析为指定对象.
     *
     * @param str       JSON格式的字符串
     * @param typeToken 目标类型
     * @param response  通用返回对象
     * @return 解析后的对象
     */
    public static Object parseFromJson(String str, TypeReference<?> typeToken, CommonResponse response, boolean rawData) {
        Object obj = null;
        if (str != null) {
            try {
                if (rawData) response.setRawData(str);

                JSONObject jsonObject = JSON.parseObject(str);

                String code = jsonObject.getString(JSON_ELEMENT_CODE);
                String mesg = jsonObject.getString(JSON_ELEMENT_MESG);

                // 先判断code
                if (StringUtil.isNotBlank(code) && JSON_VALUE_OUT_CODE.equals(code)) {
                    dialogOUT();
                    return null;
                } else if (StringUtil.isBlank(code) || !JSON_VALUE_SUCCESS_CODE.equals(code)) {
                    response.setData(null);
                    response.setCode(code);
                    response.setMsg(StringUtil.isBlank(mesg) ? mesg : mesg);
                }
                // 返回的结果为成功数据
                else {
                    obj = JSON.parseObject(str, typeToken);
                    response.setData(obj);
                    response.setMsg(StringUtil.isBlank(mesg) ? mesg : mesg);
                    response.setCodeEnum(CodeEnum.SUCCESS);
                }
            } catch (Exception e) {
                response.setData(null);
                response.setCode(CodeEnum.EXCEPTION.getCode());
                response.setMsg(e.getLocalizedMessage());
                Logger.d(str);
            } catch (OutOfMemoryError e) {
                System.gc();
                response.setData(null);
                response.setCode(CodeEnum.EXCEPTION.getCode());
                response.setMsg(e.getLocalizedMessage());
                Logger.d(str);
            }
        }
        return obj;
    }

    private static boolean isOUT;

    private static void dialogOUT() {
        if (isOUT) {
            return;
        }
        isOUT = true;
        Activity activity = App.getInstance().activityManager.currentActivity();
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        if (dialogBuilder.isShowing()) {
            dialogBuilder.dismiss();
        }
        LinearLayout convertView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_out, null);
        TextView ok = (TextView) convertView.findViewById(R.id.tv_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOUT = false;
                App.getInstance().changeAccount(false);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, activity); // .setCustomView(View
        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
        dialogBuilder.show();
    }

}

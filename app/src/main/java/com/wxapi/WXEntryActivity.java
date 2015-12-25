package com.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.core.util.CommonUtil;
import com.mytian.lb.manager.ShareManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareManager.getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ShareManager.getWXApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp == null) {
            finish();
            return;
        }
        String result = "";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                toPaySuccessActivity();
            } else if (resp.errCode == -1) {
                result = "支付失败";
            } else if (resp.errCode == -2) {
                result = "支付取消";
            }
        } else
            // 如果是分享
            if (resp instanceof SendMessageToWX.Resp) {
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        result = "分享成功";
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        result = "分享取消";
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        result = "认证失败";
                        break;
                    default:
                        result = "未知错误";
                        break;
                }
            } else {
                result = "类型错误";
            }
        CommonUtil.showToast(result);
        finish();
    }

    private void toPaySuccessActivity() {
        finish();
    }

}

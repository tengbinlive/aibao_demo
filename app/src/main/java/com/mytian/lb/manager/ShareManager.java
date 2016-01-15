package com.mytian.lb.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.enums.PlatformEnum;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

public final class ShareManager {

    private static ShareManager instance;

    private static IWXAPI wxApi;

    public NiftyDialogBuilder dialogBuilder;

    private Platform.ShareParams shareParams;

    public static ShareManager getInstance() {
        if (instance == null) {
            instance = new ShareManager();
        }
        return instance;
    }

    public void initShare() {
        ShareSDK.initSDK(App.getInstance());
    }

    /**
     * 获得微信API对象.
     *
     * @return 微信API对象
     */
    public static IWXAPI getWXApi() {
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(App.getInstance(), null);
            wxApi.registerApp(Constant.WeiXin.APP_ID);
        }
        return wxApi;
    }

    /**
     * 分享到第三方
     */
    public void share(final Platform.ShareParams module) {
        if (module == null) {
            return;
        }
        shareParams = module;
        activityHandler.sendEmptyMessage(SHOW_SHAER);
    }

    public Platform.ShareParams getParams(String title, String contents, String url, String urlIcon, Bitmap icon) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        if (StringUtil.isNotBlank(title)) {
            shareParams.setTitle(title);
        }
        if (StringUtil.isNotBlank(contents)) {
            shareParams.setText(contents);
        }
        if (StringUtil.isNotBlank(urlIcon)) {
            shareParams.setImageUrl(urlIcon);
        }
        if (null != icon) {
            shareParams.setImageData(icon);
        }
        if (StringUtil.isNotBlank(url)) {
            shareParams.setUrl(url);
        }
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        return shareParams;
    }

    private void showShareView() {
        dialogDismiss();
        final Activity activity = App.getInstance().getCurrentActivity();
        LinearLayout convertView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        ImageView share_qq = (ImageView) convertView.findViewById(R.id.share_qq);
        ImageView share_weibo = (ImageView) convertView.findViewById(R.id.share_weibo);
        ImageView share_weixin = (ImageView) convertView.findViewById(R.id.share_weixin);
        ImageView share_weixin_friend = (ImageView) convertView.findViewById(R.id.share_weixin_friend);
        TextView cancel = (TextView) convertView.findViewById(R.id.tv_cancel);
        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
                sendShare(activity, PlatformEnum.QQ_TENCENT);
            }
        });
        share_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
                sendShare(activity, PlatformEnum.SINA);
            }
        });
        share_weixin_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
                sendShare(activity, PlatformEnum.WEIXIN_TIMELINE);
            }
        });
        share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
                sendShare(activity, PlatformEnum.WEIXIN);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
            }
        });

        dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        dialogBuilder.withDuration(700) // def
                .withEffect(Effectstype.Slidetop) // def Effectstype.Slidetop
                .setCustomView(convertView, activity); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);
    }

    public void dialogDismiss() {
        if (null != dialogBuilder && dialogBuilder.isShowing()) {
            activityHandler.sendEmptyMessage(DIALOGDISMISS);
        }
    }

    private final static int SHOW_SHAER = 0;
    private final static int DIALOGSHOW = 2;
    private final static int DIALOGDISMISS = 1;

    private Handler activityHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_SHAER:
                    showShareView();
                    break;
                case DIALOGSHOW:
                    dialogBuilder.show();
                    break;
                case DIALOGDISMISS:
                    dialogBuilder.dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 调用第三方
     *
     * @param type
     */
    private void sendShare(Context context, PlatformEnum type) {
        if (shareParams == null) {
            return;
        }
        Platform platform = ShareSDK.getPlatform(context, type.getCode());
        platform.share(shareParams);
    }
}

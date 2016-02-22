package com.mytian.lb.manager;

import android.app.Activity;
import android.content.Context;
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
import com.mytian.lb.bean.dymic.DynamicContent;
import com.mytian.lb.enums.PlatformEnum;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public final class ShareManager {

    private static ShareManager instance;

    private static IWXAPI wxApi;

    public NiftyDialogBuilder dialogBuilder;

    private DynamicContent dynamicContent;

    private final static String TYPE_SHARE_WEBPAGE = "0";
    private final static String TYPE_SHARE_MUSIC = "1";
    private final static String TYPE_SHARE_VIDEO = "2";

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
    public void share(DynamicContent dynamicContent) {
        if (null == dynamicContent) {
            return;
        }
        this.dynamicContent = dynamicContent;
        activityHandler.sendEmptyMessage(SHOW_SHAER);
    }


    /**
     * 初始分享
     * @param dynamicContent
     * @param name
     * @return
     */
    public ShareParams getParams(DynamicContent dynamicContent, String name) {
        int titleConut = titleConut(name);
        int contentConut = contentConut(name);
        ShareParams shareParams = new ShareParams();
        String title = titleStr(dynamicContent, name);
        String contents = contentStr(dynamicContent,name);
        String urlImage = dynamicContent.getImageUrl();
        if (StringUtil.isNotBlank(title) && titleConut != -1) {
            if (title.length() > titleConut) {
                title = title.substring(0, titleConut);
            }
            shareParams.setTitle(title);
        }
        if (StringUtil.isNotBlank(contents) && contentConut != -1) {
            if (contents.length() > contentConut) {
                contents = contents.substring(0, contentConut);
            }
            shareParams.setText(contents);
        }
        if (StringUtil.isNotBlank(urlImage)) {
            shareParams.setImageUrl(urlImage);
        }
        if (PlatformEnum.WEIXIN.getCode().equals(name)||PlatformEnum.WEIXIN_TIMELINE.getCode().equals(name)) {
            String shareType = dynamicContent.getShareType();
            if (TYPE_SHARE_WEBPAGE.equals(shareType)) {
                shareParams.setUrl(dynamicContent.getUrl());
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
            } else if (TYPE_SHARE_MUSIC.equals(shareType)) {
                shareParams.setMusicUrl(dynamicContent.getMusicUrl());
                shareParams.setShareType(Platform.SHARE_MUSIC);
            } else if (TYPE_SHARE_VIDEO.equals(shareType)) {
                shareParams.setUrl(dynamicContent.getUrl());
                shareParams.setShareType(Platform.SHARE_VIDEO);
            } else {
                shareParams.setShareType(Platform.SHARE_IMAGE);
            }
        }else if (PlatformEnum.QQ_TENCENT.getCode().equals(name)){
            shareParams.setTitleUrl(dynamicContent.getTitleUrl());
            shareParams.setSite(dynamicContent.getSite());
            shareParams.setSiteUrl(dynamicContent.getUrl());
        }
        return shareParams;
    }

    /**
     * 分享标题 各个平台限制大小
     * @param name
     * @return
     */
    private int titleConut(String name) {
        if (PlatformEnum.WEIXIN.getCode().equals(name)) {
            return 200;
        } else if (PlatformEnum.WEIXIN_TIMELINE.getCode().equals(name)) {
            return 200;
        } else if (PlatformEnum.QQ_TENCENT.getCode().equals(name)) {
            return 30;
        } else if (PlatformEnum.SINA.getCode().equals(name)) {
            return -1;
        }
        return -1;
    }

    /**
     * 分享内容 各个平台限制大小
     * @param name
     * @return
     */
    private int contentConut(String name) {
        if (PlatformEnum.WEIXIN.getCode().equals(name)) {
            return 450;
        } else if (PlatformEnum.WEIXIN_TIMELINE.getCode().equals(name)) {
            return 450;
        } else if (PlatformEnum.QQ_TENCENT.getCode().equals(name)) {
            return 40;
        } else if (PlatformEnum.SINA.getCode().equals(name)) {
            return 140;
        }
        return -1;
    }


    /**
     * 分享标题 各个平台个性化
     * @param name
     * @return
     */
    private String titleStr(DynamicContent dynamicContent,String name) {
        if (PlatformEnum.WEIXIN.getCode().equals(name)) {
            return dynamicContent.getTitle();
        } else if (PlatformEnum.WEIXIN_TIMELINE.getCode().equals(name)) {
            return dynamicContent.getText();
        } else if (PlatformEnum.QQ_TENCENT.getCode().equals(name)) {
            return dynamicContent.getTitle();
        } else if (PlatformEnum.SINA.getCode().equals(name)) {
            return dynamicContent.getTitle();
        }
        return dynamicContent.getText();
    }

    /**
     * 分享内容 各个平台个性化
     * @param name
     * @return
     */
    private String contentStr(DynamicContent dynamicContent,String name) {
        if (PlatformEnum.WEIXIN.getCode().equals(name)) {
            return dynamicContent.getText();
        } else if (PlatformEnum.WEIXIN_TIMELINE.getCode().equals(name)) {
            return dynamicContent.getText();
        } else if (PlatformEnum.QQ_TENCENT.getCode().equals(name)) {
            return dynamicContent.getText();
        } else if (PlatformEnum.SINA.getCode().equals(name)) {
            return dynamicContent.getText()+" "+dynamicContent.getUrl();
        }
        return dynamicContent.getText();
    }

    /**
     * 显示分享操作框
     */
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
        Platform.ShareParams shareParams = getParams(dynamicContent, type.getCode());
        Platform platform = ShareSDK.getPlatform(context, type.getCode());
        platform.share(shareParams);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Logger.i("sendShare onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Logger.i("sendShare onError");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Logger.i("sendShare onCancel");
            }
        });
    }
}

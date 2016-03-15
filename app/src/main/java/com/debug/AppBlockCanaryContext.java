package com.debug;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;

/**
 * Created by bin.teng on 2016/3/15.
 */
public class AppBlockCanaryContext extends BlockCanaryContext {

    private static final String TAG = "AppBlockCanaryContext";

    /**
     * 标示符，可以唯一标示该安装版本号，如版本+渠道名+编译平台
     *
     * @return apk唯一标示符
     */
    @Override
    public String getQualifier() {
        return BuildConfig.VERSION_NAME+" for android "+BuildConfig.BUILD_TYPE;
    }

    @Override
    public String getUid() {
        return "85231331";
    }

    @Override
    public String getNetworkType() {
        return App.getCurrentNetworkStatus().getDesc();
    }

    @Override
    public int getConfigDuration() {
        return 9999;
    }

    @Override
    public int getConfigBlockThreshold() {
        return 500;
    }

    @Override
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }
}
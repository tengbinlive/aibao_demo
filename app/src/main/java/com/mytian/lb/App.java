package com.mytian.lb;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;

import com.core.enums.ConfigKeyEnum;
import com.core.manager.ConfigManager;
import com.core.util.FileDataHelper;
import com.core.util.NetworkUtil;
import com.core.util.NetworkUtil.NetworkClassEnum;
import com.core.util.ProcessUtil;
import com.dao.DaoMaster;
import com.dao.DaoMaster.OpenHelper;
import com.dao.DaoSession;
import com.dao.Parent;
import com.dao.ParentDao;
import com.debug.StrictModeWrapper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mytian.lb.activity.LoginActivity;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.ActivityManager;
import com.mytian.lb.manager.ShareManager;
import com.mytian.lb.manager.UserActionDOManager;
import com.mytian.lb.push.PushHelper;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import im.fir.sdk.FIR;


/**
 * App运行时上下文.
 * <p/>
 * 约定: 1)Constant类里保存系统安装之后就一直保持不变的常量;
 * 2)App类里保存系统启动后可变的变量,变量的值一般在系统初始化时保存,和状态相关的量在过程中可变;
 * 3)SharedPeferences对象持久化App里部分的变量, 供App初始化时读取, 其他类统一读取App里的变量,
 * 不访问SharedPerferences, 如果以后更换持久化的方式,例如DB,则仅修改App类就可以.
 *
 * @author bin.teng
 */
public class App extends Application {

    private Tracker mTracker;

    private static final String TAG = App.class.getSimpleName();

    public ActivityManager activityManager = null;

    private Activity currentActivity;

    private static App instance;

    private static DaoMaster daoMaster;

    private static DaoSession daoSession;

    private BroadcastReceiver connectionReceiver;

    private UserResult userResult;

    private String cookie;

    private boolean isNoAccount;

    private String appoint_time;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public UserResult getUserResult() {
        return userResult;
    }

    public void setUserResult(UserResult userResult) {
        this.userResult = userResult;
    }

    public boolean isNoAccount() {
        return isNoAccount;
    }

    public void setIsNoAccount(boolean isNoAccount) {
        this.isNoAccount = isNoAccount;
    }

    /**
     * 获得本类的一个实例
     */
    public static App getInstance() {
        return instance;
    }


    /**
     * 取得DaoMaster
     */
    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            OpenHelper helper = new DaoMaster.DevOpenHelper(App.getInstance(), Constant.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     */
    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    //---------以下变量存储在APP(内存)中----------//

    /**
     * 当前网络状态
     */
    private static NetworkClassEnum currentNetworkStatus = NetworkClassEnum.UNKNOWN;


    /**
     * @return 返回当前网络状态枚举类(例如: 未知 / 2G/ 3G / 4G / wifi)
     */
    public static NetworkClassEnum getCurrentNetworkStatus() {
        return currentNetworkStatus;
    }

    /**
     * @param currentNetworkStatus 当前网络状态枚举类
     */
    public static void setCurrentNetworkStatus(NetworkClassEnum currentNetworkStatus) {
        App.currentNetworkStatus = currentNetworkStatus;
    }

    public static boolean isNetworkAvailable() {
        return !NetworkClassEnum.UNKNOWN.equals(currentNetworkStatus);
    }

    //---------以下变量由ConfigManager管理----------//

    /**
     * @return 返回硬件设备编号
     */
    public static String getDeviceId() {
        return ConfigManager.getConfigAsString(ConfigKeyEnum.DEVICE_ID);
    }

    /**
     * @return 返回手机型号
     */
    public static String getMobileType() {
        return ConfigManager.getConfigAsString(ConfigKeyEnum.MOBILE_TYPE);
    }


    /**
     * @return 返回屏幕 宽
     */
    public static int getScreenWidth() {
        return ConfigManager.getConfigAsInt(ConfigKeyEnum.SCREEN_WIDTH);
    }

    /**
     * @return 返回屏幕 高
     */
    public static int getScreenHeight() {
        return ConfigManager.getConfigAsInt(ConfigKeyEnum.SCREEN_HEIGHT);
    }

    /**
     * @return 返回APP版本名称
     */
    public static String getAppVersionName() {
        return ConfigManager.getConfigAsString(ConfigKeyEnum.APP_VERSION_NAME);
    }

    /**
     * @return 返回APP版本code
     */
    public static int getAppVersionCode() {
        return ConfigManager.getConfigAsInt(ConfigKeyEnum.APP_VERSION_CODE);
    }

    /**
     * @return 是否第一次启动(某版本)
     */
    public static boolean isFirstLunch() {
        return ConfigManager.getConfigAsBoolean(ConfigKeyEnum.IS_FIRST_LUNCH);
    }

    private static boolean strictModeAvailable;

    static {
        try {
            StrictModeWrapper.checkAvailable();
            strictModeAvailable = true;
        } catch (Throwable throwable) {
            strictModeAvailable = false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 多进程情况只初始化一次
        if (ProcessUtil.isCurMainProcess(getApplicationContext())) {

            instance = this;

            Constant.DEBUG = BuildConfig.CONFIG_DEBUG;

            if (strictModeAvailable) {
                int applicationFlags = getApplicationInfo().flags;
                if (BuildConfig.DEBUG && ((applicationFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0)) {
                    StrictModeWrapper.enableDefaults();
                }
            }

            if (BuildConfig.CANARY_DEBUG) {
                refWatcher = LeakCanary.install(this);
            }

            FIR.init(this);

            //初始化自定义Activity管理器
            activityManager = ActivityManager.getScreenManager();

            // CHANNEL_ID 是否已经上传成功
            PushHelper.getInstance().UPLOAD_ID_SUCCESS = SharedPreferencesHelper.getBoolean(this, PushHelper.CHANNEL_STATE, false);

            // 初始化日志类,如果不是调试状态则不输出日志
            LogLevel logLevel = BuildConfig.LOG_DEBUG ? LogLevel.FULL : LogLevel.NONE;

            Logger.init("BIN.TENG")               // default PRETTYLOGGER or use just init()
                    .setMethodCount(3)            // default 2
                    .hideThreadInfo()             // default shown
                    .setMethodOffset(2)        // default 0
                    .setLogLevel(logLevel);  // default LogLevel.FULL

            Logger.v(TAG, "成功初始化LOG日志.");

            // 初始化APP相关目录
            FileDataHelper.initDirectory();
            Logger.v(TAG, "成功初始化APP相关目录.");

            //本地数据库
            initDAOData();

            // 保存当前网络状态(在每次网络通信时可能需要判断当前网络状态)
            setCurrentNetworkStatus(NetworkUtil.getCurrentNextworkState(this));
            Logger.v(TAG, "保存当前网络状态:" + getCurrentNetworkStatus());
            //注册网络状态监听广播
            newConnectionReceiver();

            //初始分享
            ShareManager.getInstance().initShare();

        }
    }

    private void initDAOData() {
        // 系统配置业务.
        ConfigManager.init(this);
        //用户信息
        initUserData();
        //用户动作
        UserActionDOManager.getInstance().init(this);
    }

    private void initUserData() {
        userResult = new UserResult();
        ParentDao parentDao = getDaoSession().getParentDao();
        List<Parent> parents = parentDao.loadAll();
        int size = parents == null ? 0 : parents.size();
        if (size > 0) {
            Parent parent = parents.get(0);
            App.getInstance().userResult.setParent(parent);
        }
    }

    //创建并注册网络状态监听广播
    private void newConnectionReceiver() {
        connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setCurrentNetworkStatus(NetworkUtil.getCurrentNextworkState(context));
                if (PushHelper.STATE_NORMAL != PushHelper.getInstance().pushState && !PushHelper.getInstance().UPLOAD_ID_SUCCESS) {
                    PushHelper.getInstance().bindPush(getApplicationContext());
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    //销毁网络状态监听广播
    private void unConnectionReceiver() {
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
            connectionReceiver = null;
        }
    }

    //退出app
    public void exit() {
        activityManager.popAllActivityExceptOne(this.getClass());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 切换帐号
     *
     * @param isCancle true 清楚数据并跳转至登录界面  false 跳转至登录界面
     */
    public void changeAccount(boolean isCancle) {
        if (isCancle) {
            ParentDao dao = getDaoSession().getParentDao();
            dao.deleteAll();
        }
        PushHelper.getInstance().clearChannelid();
        cookie = null;
        Intent intent = new Intent(currentActivity, LoginActivity.class);
        intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TL);
        intent.putExtra("login", isCancle);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(0, 0);
    }

    @Override
    public void onTerminate() {
        unConnectionReceiver();
        super.onTerminate();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}

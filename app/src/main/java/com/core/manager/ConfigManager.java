package com.core.manager;

import android.content.Context;

import com.core.enums.ConfigKeyEnum;
import com.core.util.CommonUtil;
import com.core.util.DeviceUtil;
import com.core.util.StringUtil;
import com.dao.Config;
import com.dao.ConfigDao;
import com.dao.DaoSession;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * 系统配置业务类.
 *
 * @author bin.teng
 */
public class ConfigManager {

    private static final String TAG = ConfigManager.class.getSimpleName();

    private static HashMap<String, Object> map = new HashMap<>();

    public static Object getConfig(ConfigKeyEnum keyEnum) {
        return map.get(keyEnum.name());
    }

    public static String getConfigAsString(ConfigKeyEnum keyEnum) {
        Object obj = getConfig(keyEnum);
        return (obj == null) ? null : (String) obj;
    }

    public static int getConfigAsInt(ConfigKeyEnum keyEnum) {
        Object obj = getConfig(keyEnum);
        return (obj == null) ? 0 : (Integer) obj;
    }

    public static boolean getConfigAsBoolean(ConfigKeyEnum keyEnum) {
        Object obj = getConfig(keyEnum);
        return (obj == null) ? false : (Boolean) obj;
    }

    /**
     * 初始化配置信息.
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        // 从数据库中载入信息
        ConfigDao configDao = App.getDaoSession().getConfigDao();
        List<Config> configList = configDao.loadAll();
        for (Config record : configList) {
            map.put(record.getKey(), record.getValue());
        }

        // 补上数据库中没有, 但每次需要初始化的信息
        Object[] screenInfo = DeviceUtil.getScreenInfo(context);
        String currentAppVersionName = CommonUtil.getAppVersionName(context);
        String lastAppVersionName = (String) map.get(ConfigKeyEnum.APP_VERSION_NAME.name());

        map.put(ConfigKeyEnum.DEVICE_ID.name(), DeviceUtil.getDeviceId(context));
        map.put(ConfigKeyEnum.IMEI.name(), DeviceUtil.getIMEI(context));
        map.put(ConfigKeyEnum.MOBILE_TYPE.name(), DeviceUtil.getMobileType());
        map.put(ConfigKeyEnum.SCREEN_WIDTH.name(), screenInfo[0]);
        map.put(ConfigKeyEnum.SCREEN_HEIGHT.name(), screenInfo[1]);
        map.put(ConfigKeyEnum.APP_VERSION_CODE.name(), CommonUtil.getAppVersionCode(context));
        map.put(ConfigKeyEnum.APP_VERSION_NAME.name(), currentAppVersionName);

        //	判断数据库中保存的版本号和当前的是否一致, 如果不一致, 需要更新到数据库中, 并标记本次启动为首次启动
        if (StringUtil.isBlank(lastAppVersionName) || !currentAppVersionName.equals(lastAppVersionName)) {
            Logger.i("判断为首次启动");
            map.put(ConfigKeyEnum.IS_FIRST_LUNCH.name(), true);
            Config record = new Config(null, ConfigKeyEnum.APP_VERSION_NAME.name(), currentAppVersionName);
            Long id = configDao.insertOrReplace(record);
            Logger.i("更新数据库中的版本号,  记录ID=" + id + ",版本号=" + currentAppVersionName);
        } else {
            map.put(ConfigKeyEnum.IS_FIRST_LUNCH.name(), false);
            Logger.i("判断为非首次启动");
        }

        if (Constant.DEBUG) {
            Logger.i("初始化系统配置");
            for (ConfigKeyEnum item : ConfigKeyEnum.values()) {
                Logger.i(item.name() + "=" + map.get(item.name()));
            }
        }
    }

    /**
     * 清除缓存
     */
    public static void clearConfig() {
        DaoSession daoSession = App.getDaoSession();
        daoSession.getConfigDao().deleteAll();
    }
}

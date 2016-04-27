package com.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

/**
 * 设备相关数据获取工具类.
 *
 * @author bin.teng
 */
public class DeviceUtil {

    public static final String SPECIAL_IMEI = "000000000000000";

    private DeviceUtil() {
    }

    /**
     * 判断Network是否开启(包括移动网络和wifi)
     *
     * @param context 上下文
     * @return 只要wifi或者其他网络有一个可用返回true, 否则返回false
     */
    public static boolean isNetworkEnabled(Context context) {
        return (isNetEnabled(context) || isWifiEnabled(context));
    }

    /**
     * 判断Network是否连接成功(包括移动网络和wifi)
     *
     * @param context 上下文
     * @return 连接成功返回true, 否则返回false
     */

    public static boolean isNetworkConnected(Context context) {
        return (isWifiContected(context) || isNetContected(context));
    }

    /**
     * 判断wifi是否开启
     *
     * @param context 上下文
     * @return 开启返回true, 否则返回false
     */
    public static boolean isWifiEnabled(Context context) {
        boolean enable = false;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            enable = true;
            Logger.v("isWifiEnabled");
        }
        Logger.v("isWifiDisabled");
        return enable;
    }

    /**
     * 判断wifi是否连接成功
     *
     * @param context 上下文
     * @return 连接成功返回true, 否则返回false
     */
    public static boolean isWifiContected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断移动网络是否开启
     *
     * @param context 上下文
     * @return 开启返回true, 否则返回false
     */
    public static boolean isNetEnabled(Context context) {
        boolean enable = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                enable = true;
                Logger.v("isNetEnabled");
            }
        }
        return enable;
    }

    /**
     * 判断移动网络是否连接成功
     *
     * @param context 上下文
     * @return 连接成功返回true, 否则返回false
     */
    public static boolean isNetContected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetworkInfo.isConnected()) {
            Logger.v("isNetContected");
            return true;
        }
        Logger.v("isNetDisconnected");
        return false;
    }

    /**
     * 获得手机类型.
     *
     * @return 返回手机类型
     */
    public static String getMobileType() {
        return Build.MODEL;
    }

    /**
     * 获取手机的设备号或wifi的mac号
     *
     * @param context 上下文
     * @return 在wifi环境下只返回mac地址，否则返回手机设备号, 在模拟器情况下会返回null
     */
    public static String getDeviceId(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String macAddress = wifiInfo.getMacAddress();
        if (null != macAddress) {
            return macAddress.replace(".", "").replace(":", "").replace("-", "").replace("_", "").replace("+", "").replace("-", "").replace("=", "");
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (StringUtil.isNotBlank(imei) && !SPECIAL_IMEI.equals(imei)) {
                return imei;
            } else {
                String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                if (null != deviceId) {
                    return deviceId.replace("+", "").replace("-", "").replace("=", "");
                }
                return null;
            }
        }
    }

    public static Object[] getScreenInfo(Context context) {
        Object[] array = new Object[3];
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        boolean largeScreen;
        int densityDpi = dm.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        largeScreen = densityDpi > 240;
        array[0] = dm.widthPixels;
        array[1] = dm.heightPixels;
        array[2] = largeScreen;
        return array;
    }

}

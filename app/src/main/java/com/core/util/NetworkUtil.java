package com.core.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.List;


/**
 * GPRS 2G(2.5) General Packet Radia Service 114kbps;
 * EDGE 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps;
 * UMTS 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准;
 * CDMA 2G 电信 Code Division Multiple Access 码分多址;
 * EVDO_0 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G;
 * EVDO_A 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G;
 * 1xRTT 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,;
 * HSDPA 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps ;
 * HSUPA 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps;
 * HSPA 3G (分HSDPA,HSUPA) High Speed Packet Access ;
 * IDEN 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）;
 * EVDO_B 3G EV-DO Rev.B 14.7Mbps 下行 3.5G;
 * LTE 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G ;
 * EHRPD 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级;
 * HSPAP 3G HSPAP 比 HSDPA 快些;
 */
public class NetworkUtil {

    /**
     * 网络类型枚举类
     *
     * @author bin.teng
     */
    public enum NetworkClassEnum {
        /**
         * 未知网络
         */
        UNKNOWN(1, "未知"),
        /**
         * 2G网络
         */
        G2(2, "2G"),
        /**
         * 3G网络
         */
        G3(3, "3G"),
        /**
         * 4G网络
         */
        G4(4, "4G"),
        /**
         * Wifi
         */
        WIFI(5, "wifi");//

        private int code;
        private String desc;

        NetworkClassEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        /**
         * 根据编号值获得对应枚举对象.
         *
         * @param code 编号值
         * @return 对应枚举对象
         */
        public static NetworkClassEnum getEnumByCode(int code) {
            for (NetworkClassEnum item : values()) {
                if (item.code == code) return item;
            }
            return null;
        }

        /**
         * 根据描述获得对应枚举对象.
         *
         * @param desc 描述
         * @return 对应枚举对象
         */
        public static NetworkClassEnum getEnumByDesc(String desc) {
            for (NetworkClassEnum item : values()) {
                if (item.desc.equals(desc)) return item;
            }
            return null;
        }

        @Override
        public String toString() {
            return super.toString() + "{code:" + this.getCode() + ",desc:" + this.desc + "}";
        }
    }

    private NetworkUtil() {
    }

    private static NetworkUtil instance;

    public static synchronized NetworkUtil getInstance() {
        if (instance == null) instance = new NetworkUtil();
        return instance;
    }

    /**
     * 是否是较差的网络模式
     *
     * @return 如果不是WIFI, 不是3G, 不是4G则认为是较差的网络模式返回true, 否则返回false
     */
    public static boolean isLowMode(NetworkClassEnum networkState) {
        return networkState != NetworkClassEnum.WIFI && networkState != NetworkClassEnum.G3 && networkState != NetworkClassEnum.G4;
    }

    /**
     * 是否是2G网络.
     *
     * @param networkState 网络类型编号, 参考NetworkClassEnum的code值
     * @return 是则返回true, 否则返回false
     */
    public static boolean is2GNet(int networkState) {
        return networkState == NetworkClassEnum.G2.getCode();
    }

    /**
     * 获得当前网络状态.
     *
     * @param context
     * @return 返回当前使用的网络枚举对象, 是wifi or 2G or 3G or 4G
     */
    public static NetworkClassEnum getCurrentNextworkState(Context context) {
        if (DeviceUtil.isWifiContected(context)) {
            return NetworkClassEnum.WIFI;
        } else {
            CellIDInfoUtil manager = new CellIDInfoUtil();
            int networkType = manager.getNetworkType(context);
            return getNetworkName(networkType);
        }
    }

    /**
     * 获得当前信号强度.
     *
     * @param context 上下文
     */
    public static int getSignalStrength(Context context) {
        int dbm = -112;
        if (context == null) return dbm;
        List<CellIDInfo> CellID = null;
        CellIDInfoUtil manager = new CellIDInfoUtil();
        try {
            CellID = manager.getCellIDInfo(context, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CellID != null && CellID.size() > 0) {
            dbm = CellID.get(0).signal_strength;
        }
        return dbm;
    }

    /**
     * 获取手机网络类型名称
     *
     * @param networkType
     * @param mnc         Mobile NetworkCode，移动网络码，共2位
     * @return
     */
    public static String getNetworkName(int networkType, String mnc) {
        if (networkType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            return "Network type is unknown";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
            return "电信2G";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
            return "电信3G";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
            if ("00".equals(mnc) || "02".equals(mnc)) {
                return "移动2G";
            } else if ("01".equals(mnc)) {
                return "联通2G";
            }
        } else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS || networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
            return "联通3G";
        }
        return null;
    }

    /**
     * 根据网络类型编号获得对应的枚举类.
     *
     * @param networkType
     * @return 网络类型枚举类
     */
    public static NetworkClassEnum getNetworkName(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NetworkClassEnum.G2;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NetworkClassEnum.G3;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetworkClassEnum.G4;
            default:
                return NetworkClassEnum.UNKNOWN;

        }
    }

}
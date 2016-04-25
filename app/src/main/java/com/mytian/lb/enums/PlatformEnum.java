package com.mytian.lb.enums;


import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 第三方类型枚举.
 *
 * @author bin.teng
 */
public enum PlatformEnum {

    /**
     * 微信
     */
    WEIXIN(Wechat.NAME, "微信"),
    /**
     * 朋友圈
     */
    WEIXIN_TIMELINE(WechatMoments.NAME, "朋友圈"),
    /**
     * QQ
     */
    QQ_TENCENT(QQ.NAME, "QQ"),
    /**
     * 新浪
     */
    SINA(SinaWeibo.NAME, "新浪微博");

    private String code;
    private String desc;

    private PlatformEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
    public static PlatformEnum getEnumByCode(String code) {
        for (PlatformEnum item : values()) {
            if (item.code.equals(code))
                return item;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(",code=").append(this.getCode());
        buf.append(",desc=").append(this.getDesc());
        return buf.toString();
    }
}

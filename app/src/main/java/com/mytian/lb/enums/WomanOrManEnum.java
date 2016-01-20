package com.mytian.lb.enums;

/**
 * 男女值枚举类.
 *
 * @author
 */
public enum WomanOrManEnum {
    /**
     * 女=0
     */
    WOMAN(0),
    /**
     * 男=1
     */
    MAN(1);

    private int code;

    private WomanOrManEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 根据编号值获得对应枚举对象.
     *
     * @param code 编号值
     * @return 对应枚举对象
     */
    public static WomanOrManEnum getEnumByCode(int code) {
        for (WomanOrManEnum item : values()) {
            if (item.code == code) return item;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toString());
        buf.append(",code=").append(this.getCode());
        return buf.toString();
    }
}

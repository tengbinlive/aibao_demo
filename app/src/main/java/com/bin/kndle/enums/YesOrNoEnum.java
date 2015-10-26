package com.bin.kndle.enums;

/**
 * 是非值枚举类.
 *
 * @author
 */
public enum YesOrNoEnum {
	/** YES=1 */
	YES("1"),
	/** NO=0 */
	NO("0");

	private String code;

	private YesOrNoEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 根据编号值获得对应枚举对象.
	 *
	 * @param code 编号值
	 * @return 对应枚举对象
	 */
	public static YesOrNoEnum getEnumByCode(String code) {
		for (YesOrNoEnum item : values()) {
			if (item.code.equals(code)) return item;
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

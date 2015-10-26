package com.core;


import com.bin.kndle.App;
import com.bin.kndle.R;
import com.core.enums.CodeEnum;
import com.core.util.StringUtil;

import java.io.Serializable;

/**
 * 通用返回对象.
 * 
 * @author bin.teng
 */
public class CommonResponse implements Serializable {

	private static final long serialVersionUID = 6983821035441531996L;

	/** 返回码 */
	private String code;

	/** 返回信息 */
	private String msg;

	/** 结果对象(XML对象或者JSON对象或者List对象) */
	private Object data;

	/** 返回的原始数据 */
	private String rawData;

	/** 返回的扩展信息 */
	private Object ext;

	/** @return 返回码 */
	public String getCode() {
		return code;
	}

	/** @param code 返回码 */
	public void setCode(String code) {
		this.code = code;
	}

	/** @return 返回信息 */
	public String getMsg() {
		return msg;
	}

	/** @param msg 返回信息 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/** @return 返回结果对象 */
	public Object getData() {
		return data;
	}

	/** @param data 结果对象 */
	public void setData(Object data) {
		this.data = data;
	}

	/** @return 返回原始数据 */
	public String getRawData() {
		return rawData;
	}

	/** @param rawData 原始数据 */
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	/** @return 返回扩展信息 */
	public Object getExt() {
		return ext;
	}

	/** @param ext 扩展信息 */
	public void setExt(Object ext) {
		this.ext = ext;
	}

	/**
	 * 构造方法
	 * 
	 * @param code 返回码
	 * @param msg 信息
	 */
	public CommonResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 构造方法
	 * 
	 * @param codeEnum 返回码枚举
	 */
	public CommonResponse(CodeEnum codeEnum) {
		this.code = codeEnum.getCode();
		this.msg = codeEnum.getDesc();
	}

	/**
	 * 构造方法
	 */
	public CommonResponse() {
	}

	/**
	 * 构造方法
	 * 
	 * @param codeEnum 返回码枚举
	 */
	public void setCodeEnum(CodeEnum codeEnum) {
		this.code = codeEnum.getCode();
		this.msg = codeEnum.getDesc();
	}

	/**
	 * 构造方法
	 * 
	 * @param codeEnum 返回码枚举
	 * @param data 返回数据
	 */
	public CommonResponse(CodeEnum codeEnum, Object data) {
		this.code = codeEnum.getCode();
		this.msg = codeEnum.getDesc();
		this.data = data;
	}

	/**
	 * 是否成功返回
	 * 
	 * @return 成功返回true, 否则返回false
	 */
	public boolean isSuccess() {
		return (CodeEnum.SUCCESS.getCode().equals(this.code));
	}

	public String getErrorTip() {
		String error = this.msg;
		if (!isSuccess()) {
			CodeEnum errorEnum = CodeEnum.getEnumByCode(code);
			if (errorEnum == null) {
				error = msg;
				return error==null?getRString(R.string.error_network):error;
			}
			switch (errorEnum) {
			case CONNECT_TIMEOUT:
			case UNKNOWN_HOST:
			case NETWORK_EXCEPTION:
			case CONNECT_UNAVAILABLE:
				error = getRString(R.string.network_unavailable);
				break;
			case UNKNOWN:
				error = StringUtil.isBlank(msg) ? getRString(R.string.network_unavailable) : msg;
				break;
			default:
				error = errorEnum.getDesc();
				break;
			}
		}
		return error;
	}

	private String getRString(int rId) {
		if (rId > 0) { return App.getInstance().getResources().getString(rId); }
		return null;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("code=").append(code);
		buf.append(",msg=").append(msg);
		buf.append(",ext=").append(ext);
		return buf.toString();
	}
}

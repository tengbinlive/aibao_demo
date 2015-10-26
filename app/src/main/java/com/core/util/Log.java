package com.core.util;

/**
 * 日志类
 * 
 * 封装原因: 希望只在系统测试时输入日志
 * 
 * @author  bin.teng
 */
public class Log {

	/** 日志开关: 开启为true,否则为关闭 */
	private static boolean enabled = false;

	/** 初始化日志(设置日志开关) */
	public static void init(boolean b) {
		Log.enabled = b;
	}

	public static void SysPrintOut(String msg) {
		if (enabled) System.out.println(msg);
	}

	public static void v(String tag, String string) {
		if (enabled) android.util.Log.v(tag, string);
	}

	public static void d(String tag, String string) {
		if (enabled) android.util.Log.d(tag, string);
	}

	public static void i(String tag, String string) {
		if (enabled) android.util.Log.i(tag, string);
	}

	public static void e(String tag, String string) {
		if (enabled) android.util.Log.e(tag, string);
	}

	public static void w(String tag, String string) {
		if (enabled) android.util.Log.w(tag, string);
	}
}

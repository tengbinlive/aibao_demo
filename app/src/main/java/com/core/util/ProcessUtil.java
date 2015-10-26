/*
 * Copyright (c) 2012, Gewara Corporation, All Rights Reserved
 */
package com.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

/**
 * 处理进程类工具.
 * 
 * @author Andy.fang
 * @createDate 2014-5-21
 * @version 1.0
 */
public class ProcessUtil {

	private ProcessUtil() {
	}

	/**
	 * @description 判断是否是主进程
	 * @author Andy.fang
	 * @createDate 2014-5-21
	 * @return true(是主进程 ) false(不是主进程)
	 */
	public static boolean isCurMainProcess(Context context) {
		boolean isMainProcess = false;
		int pid = Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid && !appProcess.processName.contains(":")) {
				isMainProcess = true;
				break;
			}
		}
		return isMainProcess;
	}

	/**
	 * @description 返回当前进程的名称（一个APP有多多个进程情况）
	 * @author Andy.fang
	 * @createDate 2014-5-21
	 * @return 所有进程名称的数组
	 */
	public static String[] getCurProcessName(Context context) {
		String[] processName = new String[5];
		int pid = Process.myPid(), i = 0;
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				processName[i] = appProcess.processName;
				i++;
			}
		}
		return processName;
	}
}

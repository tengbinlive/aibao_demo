package com.core;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * 通用异常处理类.
 * 
 * @author bin.teng
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;

	/** 上下文对象 */
	private static Context context;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler defaultHandler;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		context = ctx;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 如果用户没有处理则让系统默认的异常处理器来处理
		Logger.e(ex.toString());
		if (!handleException(ex) && defaultHandler != null) {
			defaultHandler.uncaughtException(thread, ex);
		}
		// 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
		else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @return true代表处理该异常，不再向上抛异常，
	 * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
	 * 简单来说就是true不会弹出那个错误提示框，false就会弹出
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) { return false; }
		final String message = ex.getLocalizedMessage();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		return true;
	}
}
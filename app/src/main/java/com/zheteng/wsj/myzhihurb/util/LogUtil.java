package com.zheteng.wsj.myzhihurb.util;

import android.util.Log;


/**
 * Log管理类
 * @author lxj
 *
 */
public class LogUtil {
	private static String TAG = LogUtil.class.getSimpleName();
	private static boolean isDebug = true;//是否是开发模式，当开发完毕后，置为false
	
	/**
	 * 打印d级别的log
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag,String msg){
		if(isDebug){
			Log.d(tag, msg);
		}
	}
	/**
	 * 打印e级别的log
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
	
	/**
	 * 打印log，外界可以传任何对象，我们帮它生产tag
	 * @param object
	 * @param msg
	 */
	public static void d(Object object,String msg){
		if(isDebug){
			Log.d(object.getClass().getSimpleName(), msg);
		}
	}
	
	public static void e(Object object,String msg){
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
	
	public static void d(String msg){
		d(TAG, msg);
	}
	public static void e(String msg){
		e(TAG, msg);
	}
}

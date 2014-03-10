package com.easyhome.common.utils;

import android.content.Context;

import com.easyhome.sample.R;

public class ToastUtils {
	public static void showShortToast(Context context, String text) {
		ToastInstance toast = ToastInstance.getInstance(context);
		toast.showShortToast(context, text);
	}

	public static void showShortToast(Context context, int strId) {
		ToastInstance.getInstance(context).showShortToast(context, strId);
	}

	public static void showShortToast(Context context, int resid, String str) {
		String message = context.getString(resid, str);
		ToastInstance.getInstance(context).showShortToast(context, message);
	}

	/**
	 * Toast：网络未能成功连接,在线功能无法使用 建议统一使用
	 * */
	public static void showNetFailToast(Context context) {
		ToastInstance.getInstance(context).showLongToast(context,
				context.getResources().getString(R.string.online_network_connect_error));
	}

	public static void showPopToast(Context context, String text) {
		showShortToast(context, text);
	}

	public static void showPopToast(Context context, int strId) {
		showShortToast(context, strId);
	}

	public static void showPopToast(Context context, int resid, String str) {
		showShortToast(context, resid);
	}

	public static void showLongToast(Context context, int strId) {
		ToastInstance.getInstance(context).showLongToast(context, strId);
	}

	public static void showLongToast(Context context, String text) {
		ToastInstance.getInstance(context).showLongToast(context, text);
	}
}
package com.easyhome.common.modules.download;

/**
 * 下载异常类
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public class DownloadException extends Exception {

	public static final int ERROR_RUN_STATE = 0x2000;

	public DownloadException(int errorNo, String msg) {
		super("#Error[" + errorNo + "]#" + msg);
	}
}

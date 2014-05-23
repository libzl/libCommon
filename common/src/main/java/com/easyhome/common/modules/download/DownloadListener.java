package com.easyhome.common.modules.download;

/**
 * 监听下载项的下载过程
 * @see RunState
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public interface DownloadListener {
	/**
	 * 准备阶段
	 * @param item
	 */
	void onPrepare(Downloadable item);

	/**
	 * 开始阶段
	 * @param item
	 */
	void onStart(Downloadable item);

	/**
	 * 重新开始阶段
	 * @param item
	 */
	void onResume(Downloadable item);

	/**
	 * 读取进度阶段
	 * @param item
	 */
	void onProgress(Downloadable item);

	/**
	 * 暂停阶段
	 * @param item
	 */
	void onPause(Downloadable item);

	/**
	 * 完成状态
	 * @param item
	 */
	void onEnd(Downloadable item);

	/**
	 * 出错状态
	 * @param item
	 */
	void onError(Downloadable item);
}

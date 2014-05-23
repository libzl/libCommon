package com.easyhome.common.modules.download;

import java.util.List;

/**
 * 抽象下载器
 *
 * 支持断点续传，多任务，灵活线程池、队列控制和持久化配置，下载状态自由控制，批量下载，离散下载
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public interface IDownloader {

	/**
	 * 初始化下载器
	 * @param config
	 */
	public void init(IDownloadConfig config);

	/**
	 * 添加下载项到下载队列中
	 * @param item
	 */
	public void add(Downloadable item);

	/**
	 * 批量添加下载项到下载队列中
	 * @param list
	 */
	public void addAll(List<Downloadable> list);

	/**
	 * 将下载项移出下载项
	 * @param item
	 */
	public void remove(Downloadable item);

	/**
	 * 批量将下载项移出下载项
	 * @param list
	 */
	public void removeAll(List<Downloadable> list);

	/**
	 * 清空下载队列
	 */
	public void clear();

	/**
	 * 销毁下载器
	 * 做必要的释放状态，比如线程池关闭等
	 */
	public void destory();

	/**
	 * 添加下载监听
	 * @param listener
	 */
	public void addDownloadListener(DownloadListener listener);

	/**
	 * 移出下载监听
	 * @param listener
	 */
	public void removeDownloadListener(DownloadListener listener);
}

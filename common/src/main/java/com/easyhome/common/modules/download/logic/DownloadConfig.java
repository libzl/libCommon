package com.easyhome.common.modules.download.logic;

import com.easyhome.common.modules.download.IDownloadConfig;
import com.easyhome.common.modules.download.policy.CollectionPolicy;
import com.easyhome.common.modules.download.policy.HibernatePolicy;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 下载器配置
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public class DownloadConfig implements IDownloadConfig {
	/**
	 * 线程池
	 */
	private ExecutorService executorService;
	/**
	 * 持久化策略
	 */
	private HibernatePolicy hibernatePolicy;

	/**
	 * 收集排序策略
	 */
	private CollectionPolicy collectionPolicy;
	/**
	 * 可同时下载个数
	 */
	private int runningCoreCount;
	/**
	 * 下载保存目录
	 */
	private String saveDir;
	/**
	 * 临时文件后缀符
	 */
	private String tempFileSuffix;

	/**
	 * 是否开启离散下载
	 */
	private boolean isEnableScatter;

	public DownloadConfig() {
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void setHibernatePolicy(HibernatePolicy hibernatePolicy) {
		this.hibernatePolicy = hibernatePolicy;
	}

	public void setCollectionPolicy(CollectionPolicy collectionPolicy) {
		this.collectionPolicy = collectionPolicy;
	}

	public void setRunningCoreCount(int runningCoreCount) {
		this.runningCoreCount = runningCoreCount;
	}

	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}

	public void setTempFileSuffix(String tempFileSuffix) {
		this.tempFileSuffix = tempFileSuffix;
	}

	public void setEnableScatter(boolean isEnableScatter) {
		this.isEnableScatter = isEnableScatter;
	}

	@Override
	public Executor getExecutor() {
		return executorService;
	}

	@Override
	public CollectionPolicy getCollectionPolicy() {
		return collectionPolicy;
	}

	@Override
	public HibernatePolicy getHibernatePolicy() {
		return hibernatePolicy;
	}

	@Override
	public String getDefaultSaveDir() {
		return saveDir;
	}

	@Override
	public int getMaxCoreThreadCount() {
		return runningCoreCount;
	}

	@Override
	public boolean isEnableScatter() {
		return isEnableScatter;
	}

	public String getTempFileSuffix() {
		return tempFileSuffix;
	}
}

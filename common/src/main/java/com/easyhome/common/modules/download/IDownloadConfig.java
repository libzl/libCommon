package com.easyhome.common.modules.download;

import com.easyhome.common.modules.download.policy.CollectionPolicy;
import com.easyhome.common.modules.download.policy.HibernatePolicy;

import java.util.concurrent.Executor;

/**
 * 下载器配置
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public interface IDownloadConfig {

	/**
	 * 提供线程池
	 * @return
	 */
	public Executor getExecutor();

	/**
	 * 提供收集排序策略
	 * @return
	 */
	public CollectionPolicy getCollectionPolicy();

	/**
	 * 提供持久化保存策略
	 * @return
	 */
	public HibernatePolicy getHibernatePolicy();

	/**
	 * 提供默认下载目录
	 * @return
	 */
	public String getDefaultSaveDir();

	/**
	 * 提供临时文件后缀名
	 * @return
	 */
	public String getTempFileSuffix();

	/**
	 * 提供最大下载任务数
	 * 如果{@code isEnableScatter()} 设置为true，则为拆分片段数
	 * @return
	 */
	public int getMaxCoreThreadCount();

	/**
	 * 是否使用离散下载
	 * @return
	 */
	public boolean isEnableScatter();

}

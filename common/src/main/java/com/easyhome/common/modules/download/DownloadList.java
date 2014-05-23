package com.easyhome.common.modules.download;

import com.easyhome.common.modules.download.policy.CollectionPolicy;
import com.easyhome.common.modules.download.policy.HibernatePolicy;

/**
 * 管理下载队列的操作和存储
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public class DownloadList {

	private HibernatePolicy hibernatePolicy;
	private CollectionPolicy collectionPolicy;

	public void add(Downloadable item) {

	}
}

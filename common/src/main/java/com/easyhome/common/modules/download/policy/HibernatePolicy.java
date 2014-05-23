package com.easyhome.common.modules.download.policy;

import com.easyhome.common.modules.download.Downloadable;

/**
 * 持久化策略管理
 * 增加一层事务的处理
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public abstract class HibernatePolicy {
	private void performPrepare() {

	}

	private void performCommit() {

	}

	public HibernatePolicy append(Downloadable downloadable) {
		return this;
	}

	protected abstract void prepare();

	protected abstract void commit();
}

package com.easyhome.common.modules.download.policy;

import com.easyhome.common.modules.download.Downloadable;

import java.util.List;

/**
 * 持久化策略管理
 * 增加一层事务的处理
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public abstract class HibernatePolicy {
    public static final int CMD_INSERT = 0;
    public static final int CMD_UPDATE = CMD_INSERT + 1;
    public static final int CMD_DELETE = CMD_INSERT + 2;

	private void performPrepare() {

	}

	private void performCommit() {

	}

	public HibernatePolicy append(int cmd, Downloadable downloadable) {
		return this;
	}

	public HibernatePolicy appendBatch(int cmd, List<Downloadable> items) {
		return this;
	}

	public abstract void prepare();

	public abstract void commit();
}

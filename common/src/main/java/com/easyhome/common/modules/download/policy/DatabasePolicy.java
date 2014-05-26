package com.easyhome.common.modules.download.policy;

/**
 * 数据库存储策略
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public abstract class DatabasePolicy<T> extends HibernatePolicy {

	@Override
    public void prepare() {

	}

	@Override
    public void commit() {

	}

    /**
     * 获得数据库表头
     * @return
     */
    public abstract T getTable();
}

package com.easyhome.common.modules.download.logic;

import com.easyhome.common.modules.download.policy.DatabasePolicy;

/**
 * 下载数据库持久化策略
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public class DownloadTablePolicy extends DatabasePolicy {
    @Override
    public Object getTable() {
        return null;
    }
}

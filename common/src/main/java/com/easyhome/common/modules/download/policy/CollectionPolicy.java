package com.easyhome.common.modules.download.policy;

import com.easyhome.common.modules.download.Downloadable;

import java.util.List;

/**
 * 管理下载顺序策略
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public interface CollectionPolicy {

    /**
     *
     * @param item
     */
    public void add(Downloadable item);

    /**
     *
     * @param list
     */
    public void addAll(List<Downloadable> list);

    /**
     *
     * @param item
     */
    public void remove(Downloadable item);

    /**
     *
     * @param list
     */
    public void removeAll(List<Downloadable> list);

    /**
     *
     */
    public void clear();

    /**
     * @return
     */
    public Downloadable next();

    /**
     * 获得全部
     *
     * @return
     */
    public List<Downloadable> getAll();
}

package com.easyhome.common.modules.download;

import com.easyhome.common.modules.download.policy.CollectionPolicy;
import com.easyhome.common.modules.download.policy.HibernatePolicy;

import java.util.List;

/**
 * 管理下载队列的操作和存储
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public class DownloadList {

    private HibernatePolicy hibernatePolicy;
    private CollectionPolicy collectionPolicy;

    public DownloadList(IDownloadConfig config) {
        if (config != null) {
            hibernatePolicy = config.getHibernatePolicy();
            collectionPolicy = config.getCollectionPolicy();
        }
    }

    void add(Downloadable item) {

        if (hibernatePolicy != null) {
            hibernatePolicy.prepare();
            hibernatePolicy.append(HibernatePolicy.CMD_INSERT, item);
            hibernatePolicy.commit();
        }

        if (collectionPolicy != null) {
            collectionPolicy.add(item);
        }
    }

    void addAll(List<Downloadable> list) {
        if (hibernatePolicy != null) {
            hibernatePolicy.prepare();
            hibernatePolicy.appendBatch(HibernatePolicy.CMD_INSERT, list);
            hibernatePolicy.commit();
        }

        if (collectionPolicy != null) {
            collectionPolicy.addAll(list);
        }
    }

    void remove(Downloadable item) {
        if (hibernatePolicy != null) {
            hibernatePolicy.prepare();
            hibernatePolicy.append(HibernatePolicy.CMD_DELETE, item);
            hibernatePolicy.commit();
        }

        if (collectionPolicy != null) {
            collectionPolicy.remove(item);
        }
    }

    void removeAll(List<Downloadable> list) {
        if (hibernatePolicy != null) {
            hibernatePolicy.prepare();
            hibernatePolicy.appendBatch(HibernatePolicy.CMD_DELETE, list);
            hibernatePolicy.commit();
        }

        if (collectionPolicy != null) {
            collectionPolicy.removeAll(list);
        }
    }

    void clear() {

        if (hibernatePolicy != null) {
            hibernatePolicy.prepare();
            hibernatePolicy.appendBatch(HibernatePolicy.CMD_DELETE, getAll());
            hibernatePolicy.commit();
        }

        if (collectionPolicy != null) {
            collectionPolicy.clear();
        }
    }

    /**
     * 下一个下载项
     *
     * @return
     */
    public Downloadable next() {
        if (collectionPolicy != null) {
            return collectionPolicy.next();
        }
        return null;
    }

    /**
     * 得到下载队列所有下载对象
     *
     * @return
     */
    public List<Downloadable> getAll() {
        if (collectionPolicy != null) {
            return collectionPolicy.getAll();
        }
        return null;
    }

    /**
     * 针对下载状态变化更新数据
     * @param item
     */
    public void notifyStatedChanged(Downloadable item) {

    }
}

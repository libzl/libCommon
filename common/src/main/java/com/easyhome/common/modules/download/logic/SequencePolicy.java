package com.easyhome.common.modules.download.logic;

import com.easyhome.common.modules.download.Downloadable;
import com.easyhome.common.modules.download.policy.CollectionPolicy;

import java.util.List;

/**
 * 顺序策略
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public class SequencePolicy implements CollectionPolicy {
    @Override
    public void add(Downloadable item) {

    }

    @Override
    public void addAll(List<Downloadable> list) {

    }

    @Override
    public void remove(Downloadable item) {

    }

    @Override
    public void removeAll(List<Downloadable> list) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Downloadable next() {
        return null;
    }

    @Override
    public List<Downloadable> getAll() {
        return null;
    }
}

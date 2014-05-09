package com.easyhome.common.core.base.object;

/**
 * 对象池
 *
 * @author by kevin on 5/5/14.
 */
public interface ObjectPool<T> {

    public ObjectPool newPool(int capacity);
    public void add(T t);
    public void remove(T t);

}

package com.easyhome.common.modules.download;

/**
 * 可下载项
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public interface Downloadable {
    /**
     * 获得当前状态
     *
     * @return
     */
    public RunState getState();

    /**
     * 设置状态
     *
     * @param state
     */
    void setState(RunState state);

    /**
     * 下载项的网络获取链接
     *
     * @return 网络下载url
     */
    public String getUrl();

    /**
     * 下载项de名称
     *
     * @return
     */
    public String getName();

    /**
     * 下载项保存目录
     *
     * @return
     */
    public String getSaveDir();

    /**
     * 下载项当前已下载大小
     *
     * @return
     */
    public long getSize();

    /**
     * 设置当前下载的大小
     *
     * @param size
     */
    public void setSize(long size);

    /**
     * 下载项总大小
     *
     * @return
     */
    public long getTotal();

    /**
     * 设置总大小
     *
     * @param total
     */
    public void setTotal(long total);

    /**
     * 转化成json数据
     *
     * @return
     */
    public String toJson();

    /**
     * 将json数据转化成对象
     *
     * @param json
     * @return
     */
    public Downloadable fromJson(String json);


}
